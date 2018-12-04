

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.plaf.multi.MultiViewportUI;

/**
 *
 * @author Mutaz Barika
 */

public class SerClientThread extends Thread{
   Socket s1;
   InputStream s1In;
   OutputStream s1out;
   DataOutputStream dos;
   DataInputStream dis;
   String clientName;
   
   public SerClientThread(Socket s1) {
       super();
       this.s1 = s1;
       
       try {
            s1out = s1.getOutputStream();
            dos = new DataOutputStream(s1out);
            s1In = s1.getInputStream();
            dis = new DataInputStream(s1In);
        } catch (IOException ex) {
            Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

   }

   public void run()
   {
       try {
       
            //Check and set client name from client console
            checkAndSetClientName();

            dos.writeUTF("ChatSysInfo_Connected");
            //Immediate Interaction after Connection
            int attempts=3;
            String option="";
            int fromClosedPrivateRoom=0;
            //dos.writeUTF("THE CHAT OPTIOS ARE:\n 1- Join Public Chat, enter JPUB \n 2- Join Private Chat Room, enter JPRIV <passcode> \n 3- Create Private Chat Room, enter Create <passcode> <MaxMember> \n 4- Delete Private Chat Room. enter Delete <passcode>");
            
            //for chat option
            while(true) 
            {
                if(fromClosedPrivateRoom==0)
                    option= new String(dis.readUTF());
                else if(fromClosedPrivateRoom==1)
                {
                    fromClosedPrivateRoom=0;
                }

                
                if(option.toUpperCase().equals("JPUB"))
                {
                    //dos.writeUTF("YOU ARE NOW IN PUBLIC CHAT ROOM");
                    MyChatServer.PublicRoom.add(s1);
                    //Send message to all members notify them this client is joined the chat room
                    MyChatServer.addPublicMessage(new ClientMessInfo(s1,"Control",clientName+" is joined\n"));
                    MyChatServer.bthread.startmessage();
                    //System.out.println("recieved:"+newmess);
                    while(true)
                    {
                        String newmess=new String(dis.readUTF());
                        //JOptionPane.showMessageDialog(null, "Server Thread Read");
                        if(newmess.toUpperCase().equals("ECCR"))
                        {
                            //Quit message for this client
                            //dos.writeUTF("YOU ARE EXIT FROM PUBLIC CHAT ROOM");
                            //Send control message indicate that this client is left
                            MyChatServer.addPublicMessage(new ClientMessInfo(s1,"Control",clientName+" is left\n"));
                            MyChatServer.bthread.startmessage();

                            MyChatServer.PublicRoom.remove(s1);
                            //dos.writeUTF("THE CHAT OPTIOS ARE:\n 1- Join Public Chat, enter JPUB \n 2- Join Private Chat Room, enter JPRIV <passcode> \n 3- Create Private Chat Room, enter Create <passcode> <MaxMember> \n 4- Delete Private Chat Room. enter Delete <passcode>");
                            attempts=3;
                            break;
                        }
                        else //normal public message
                        {
                            MyChatServer.addPublicMessage(new ClientMessInfo(s1,clientName,newmess));
                            MyChatServer.bthread.startmessage();
                        }
                    }
                }
                else if(option.toUpperCase().startsWith("JPRIV"))
                {
                    //split entered option and then check it
                    String[] a=option.split(" ");
                    if(checkFormatOfJPRIV(a))
                    {
                        int passcode=Integer.parseInt(a[1]);
                        PrivateRoomThread tPrivateRoom=MyChatServer.retrivePrivateRoom(passcode);
                        if(tPrivateRoom!=null)
                        {
                            if(tPrivateRoom.getNumOfMemebers()<tPrivateRoom.getPrivateRoomLimt())
                            {
                                tPrivateRoom.joinPrivateRoom(s1);
                                dos.writeUTF("ChatSysInfo_ProomJoined");
                                //dos.writeUTF("Control: YOU ARE NOW IN PRIVATE CHAT ROOM <" + passcode + ">");
                                //Send private message to all private room members notify them this client is joined the chat room
                                tPrivateRoom.addmessage(new ClientMessInfo(s1,"Control",clientName+" is joined\n"));
                                tPrivateRoom.startmessage();

                                while(true)
                                {
                                    String newmess=new String(dis.readUTF());
                                    if(tPrivateRoom.isAlive())
                                    {
                                        //Exit from current private room
                                        if(newmess.toUpperCase().equals("ECCR"))
                                        {
                                            //Quit message for this client
                                            //dos.writeUTF("YOU ARE EXIT FROM PRIVATE CHAT ROOM " + passcode);
                                            //Send control message indicate that this client is left from private room
                                            tPrivateRoom.addmessage(new ClientMessInfo(s1,"Control",clientName+" is left\n"));
                                            tPrivateRoom.startmessage();

                                            tPrivateRoom.leavePrivateRoom(s1);
                                            //dos.writeUTF("THE CHAT OPTIOS ARE:\n 1- Join Public Chat, enter JPUB \n 2- Join Private Chat Room, enter JPRIV <passcode> \n 3- Create Private Chat Room, enter Create <passcode> <MaxMember> \n 4- Delete Private Chat Room. enter Delete <passcode>");
                                            attempts=3;
                                            break;
                                        }
                                        else //normal private message
                                        {
                                            tPrivateRoom.addmessage(new ClientMessInfo(s1,clientName,newmess));
                                            tPrivateRoom.startmessage();
                                        }
                                    }
                                    else
                                    {
                                        option=newmess;
                                        fromClosedPrivateRoom=1;
                                        break;
                                    }
                                }
                            }
                            else
                            {
                                dos.writeUTF("ChatSysInfo_RoomFull");
                                //diconnect client
                                //String reason="Private room " + passcode + " is full";
                                //shutdownClientConnection(reason);
                                //MyChatServer.removeclient(s1,clientName);
                                //break;
                            }
                        }
                        else
                        {
                            //dos.writeUTF("WRONG <PASSCODE>!! THERE IS NO PRIVATE ROOM WITH PASSCODE = "+ passcode +". PLEASE SPECIFY CORRECT PASSCODE");
                        	dos.writeUTF("ChatSysInfo_NoSuchCode");
                        	attempts--;
                        }
                    //break;
                    }
                    else
                    {
                        attempts--;
                    }
                }
                else if(option.toUpperCase().startsWith("CREATE"))
                {
                    
                    String[] a=option.split(" ");
                    if(checkFormatOfCreate(a))
                    {
                        int passcode=Integer.parseInt(a[1]);
                        int limit=Integer.parseInt(a[2]);

                        //check the uniqueness of passcode
                        boolean checkPasscode=MyChatServer.isPasscodeUnique(passcode);

                        if(checkPasscode)
                        {
                            PrivateRoomThread t=MyChatServer.createPrivateRoom(clientName, passcode, s1, limit);
                            if(t!=null)
                            {
                            	dos.writeUTF("ChatSysInfo_ProomCreated");
                                //dos.writeUTF("PRIVATE CHAT ROOM " + passcode + " IS CREATED.\n");
                                //dos.writeUTF("YOU ARE NOW IN PRIVATE CHAT ROOM " + passcode);
                                PrivateRoomThread tPrivateRoom=MyChatServer.retrivePrivateRoom(passcode);

                                while(true)
                                {
                                    String newmess=new String(dis.readUTF());    
                                    
                                    //Exit from current private room
                                    if(newmess.toUpperCase().equals("ECCR"))
                                    {
                                        //Quit message for this client
                                        //dos.writeUTF("YOU ARE EXIT FROM PRIVATE CHAT ROOM " + passcode);
                                        //Send control message indicate that this client is left from private room
                                        tPrivateRoom.addmessage(new ClientMessInfo(s1,"Control",clientName+" is left\n"));
                                        tPrivateRoom.startmessage();

                                        tPrivateRoom.leavePrivateRoom(s1);
                                        //dos.writeUTF("THE CHAT OPTIOS ARE:\n 1- Join Public Chat, enter JPUB \n 2- Join Private Chat Room, enter JPRIV <passcode> \n 3- Create Private Chat Room, enter Create <passcode> <MaxMember> \n 4- Delete Private Chat Room. enter Delete <passcode>");
                                        attempts=3;
                                        break;
                                    }
                                    else //normal private message
                                    {
                                        tPrivateRoom.addmessage(new ClientMessInfo(s1,clientName,newmess));
                                        tPrivateRoom.startmessage();
                                    }

                                }
                            }
                        }
                        else
                        {
                            attempts--;
                            dos.writeUTF("CreateProomFail_CodeExisted");
                        }
                    }
                    else
                    {
                        attempts--;
                    }
                }
                else if(option.toUpperCase().startsWith("DELETE"))
                {
                    //split entered option and then check it
                    String[] a=option.split(" ");
                    
                    if(checkFormatOfDelete(a))
                    {
                        int passcode=Integer.parseInt(a[1]);

                        PrivateRoomThread tPrivateRoom=MyChatServer.retrivePrivateRoom(passcode);
                        if(tPrivateRoom!=null)
                        {
                            //Check if the client is the owner
                            boolean check=MyChatServer.isCreatorOfPrivateRoom(passcode, clientName);
                            if(check)
                            {
                                // notify all clients and make them exit from that room forcedly

                                for(int i=0;i<tPrivateRoom.getMembersSocket().size();i++)
                                {
                                    Socket cs=tPrivateRoom.getMembersSocket().get(i);
                                    if(cs!=null)
                                    {
                                        OutputStream os;
                                        os = cs.getOutputStream();
                                        DataOutputStream d = new DataOutputStream (os);;
                                        //Message = the owner is deleted this private room, so you are exit from that room forcedly
                                        d.writeUTF("ChatSysInfo_ForceLeave");
                                        //d.writeUTF("Control: THIS PRICATE ROOM IS DELETED BY CREATOR, SO YOU ARE FORCEDLY EXIT FROM IT");
                                        //d.writeUTF("THE CHAT OPTIOS ARE:\n 1- Join Public Chat, enter JPUB \n 2- Join Private Chat Room, enter JPRIV <passcode> \n 3- Create Private Chat Room, enter Create <passcode> <MaxMember> \n 4- Delete Private Chat Room. enter Delete <passcode>");
                                    }
                                }
                                //Request from server to delete and interept such thread
                                MyChatServer.removePrivateRoom(passcode);
                                //dos.writeUTF("PRIVATE CHAT ROOM " + passcode+" IS DELETED");
                                dos.writeUTF("ChatSysInfo_ProomDeleted");
                                attempts=3;
                                //dos.writeUTF("THE CHAT OPTIOS ARE:\n 1- Join Public Chat, enter JPUB \n 2- Join Private Chat Room, enter JPRIV <passcode> \n 3- Create Private Chat Room, enter Create <passcode> <MaxMember> \n 4- Delete Private Chat Room. enter Delete <passcode>");
                            }
                            else
                            {
                                //dos.writeUTF("YOU ARE NOT A CREATOR OF THIS PRIVATE ROOM");
                            	dos.writeUTF("ChatSysInfo_NotCreator");
                                attempts--;
                            }
                        }
                        else
                        {
                        	dos.writeUTF("DeleteInfo_CodeNotFound");
                            //dos.writeUTF("WRONG <PASSCODE> !! THERE IS NO PRIVATE ROOM WITH PASSCODE = "+ passcode +". PLEASE SPECIFY CORRECT PASSCODE");
                            //attempts--;
                        }
                    }
                    else
                    {
                        attempts--;
                    }                    
                }
                else if(option.toUpperCase().equals("SHUTDOWN"))
                {
                    String reason="Client request";
                    shutdownClientConnection(reason);
                    MyChatServer.removeclient(s1,clientName);
                    break;
                }
                else
                {
                    attempts--;
                    dos.writeUTF("WRONG OPTION(" + attempts + ")!! PLEASE SPECIFY OPTION AGAIN");                    
                }

                if(attempts==0)
                {
                    dos.writeUTF("SORRY YOU ARE DISCONNECTED DUE TO THREE WRONG ATTEMPTS");
                    String reason="Three wrong attempts";
                    shutdownClientConnection(reason);
                    MyChatServer.removeclient(s1,clientName);
                    break;
                }
            }
	} catch (IOException e ) {
		// TODO Auto-generated catch block
            //Remove client
            MyChatServer.removeclient(s1, clientName);
            MyChatServer.PublicRoom.remove(s1);
            for(int i=0;i<MyChatServer.PrivateRooms.size();i++)
                MyChatServer.PrivateRooms.get(i).removeMemeberSockets(s1);
            
            forceClientConnection();
            MyChatServer.removeclient(s1,clientName);
            //e.printStackTrace();
	} 
}

   private void checkAndSetClientName()
   {
       //Read and check if the entered name is unique name - not exist otherwise, ask client to enter new name
        while(true)
        {
           //Read client name and check if it is exist or not
        	clientName=readClientName();
            if(!MyChatServer.checkClientName(clientName)) {
            	if (clientName!="") {            		
            		System.out.println("Server connected to "+ clientName + " on port " + s1.getPort());
            		
				}            	
            	break;
            }
            else
            {
                try
                {
                    OutputStream sout = s1.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(sout);
                    dos.writeUTF("ChatSysInfo_NameExisted");
                    //dos.writeUTF("ChatSystemInfo: THE ENTERED NAME("+clientName+ ") IS ALREADY USED, PLEASE ENTER ANOTHER NAME");
                   
                }catch(IOException ex)
                {

                }
            }     
        }

        //System.out.println("Server connected to "+ clientName + " on port " + s1.getPort());

        //Create client infomration object
        ClientInfo cInfor=new ClientInfo(clientName, s1);

        //Add client to clients infomration queue
        MyChatServer.addClientsInfo(cInfor);
   }
   
   //ask user to input new name if it's in use
   public String readClientName()
    {
        String name="";
        try
        {
            InputStream s1In = s1.getInputStream();
            DataInputStream dis = new DataInputStream(s1In);
            name = new String (dis.readUTF());
        }catch(IOException ex)
        {

        }

        return name;
    }

   private void shutdownClientConnection(String reason)
   {
        try {
            //Send shoutdown request to client to showdown its clientRead and clientWrite threads
            dos.writeUTF("SHUTDOWN");

            //Stop this thread
            this.interrupt();

            //Close client communication streams and socket
            dis.close();
            dos.close();
            s1.close();

            System.out.println("Server disconnected "+ clientName + " on port "+s1.getPort() + " [Reason: " + reason + "]");
            
            
        } catch (IOException ex) {
            Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
   }


   private void forceClientConnection()
   {
       //This method called when the client forcedly closes its application
        try {

            //Check if client
            try
            {
                dos.writeUTF("SHUTDOWN");
            }catch (IOException ex)
            {
            }

            //Stop this thread
            this.interrupt();

            //Close client communication streams and socket
            dis.close();
            dos.close();
            s1.close();

            //Send shoutdown request to server client to showdown its clientRead and clientWrite threads

        } catch (IOException ex) {
            Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
   }

   private boolean checkFormatOfCreate(String[] a)
   {
       boolean ret=false;
       if(a.length==1)
       {
           try {
               dos.writeUTF("PASSCODE AND maxMEM ARGUMENTS ARE MISSING!! PLEASE ENTER THE OPTION WITH PASSCODE AND maxMEM ARGUEMTNS");
               //dos.writeUTF("INVALID FORMAT FOR CREATING SPECIFIC PRIVATE CHAT ROOM");
               } catch (IOException ex1) {
                Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex1);
               }
       }
       else if(a.length==2)
       {
           try {
               dos.writeUTF("maxMEM ARGUMENT IS MISSING!! PLEASE ENTER THE OPTION WITH PASSCODE AND maxMEM ARGUEMTNS");
               //dos.writeUTF("INVALID FORMAT FOR CREATING SPECIFIC PRIVATE CHAT ROOM");
               } catch (IOException ex1) {
                Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex1);
               }
       }
       else if(a.length==3)
       {
           try
           {
               //Check passcode in correct format (number
               String pattern="[0-9]+[0-9]*";
               if(a[1].matches(pattern))
               {
                   if(a[2].matches(pattern))
                   {
                       int temp=Integer.parseInt(a[2]);
                       if(temp>1)
                           ret=true;
                       else
                           dos.writeUTF("INVALID maxMEM VALUE!! PLEASE REENTER THE OPTION WITH VAILD maxMEM VALUE =>2");
                   }
                   else
                       dos.writeUTF("INVALID maxMEM FORMAT (Not Number or negative number)!! PLEASE REENTER THE OPTION WITH VAILD maxMEM VALUE");
               }
               else
                   dos.writeUTF("INVALID PASSCODE FORMAT (Not Number)!! PLEASE REENTER THE OPTION WITH VAILD PASSCODE VALUE");
               //int i=Integer.parseInt(a[1]);
               //int ii=Integer.parseInt(a[2]);

           }catch(IOException ex)
           {
                try {
                    dos.writeUTF("WRONG <OPTION>!! PLEASE SPECIFY IT AGAIN");
                    //dos.writeUTF("INVALID FORMAT FOR CREATING SPECIFIC PRIVATE CHAT ROOM");
                } catch (IOException ex1) {
                    Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex1);
                }
           }
       }
       else
       {
           try {
               dos.writeUTF("WRONG <OPTION>!! PLEASE SPECIFY IT AGAIN");
               //dos.writeUTF("INVALID FORMAT FOR CREATING SPECIFIC PRIVATE CHAT ROOM");
               } catch (IOException ex1) {
                Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex1);
               }
       }

       return ret;

   }


   private boolean checkFormatOfJPRIV(String[] a)
   {
       boolean ret=false;
       if(a.length==1)
       {
           try {
                //dos.writeUTF("INVALID FORMAT FOR JOIING SPECIFIC PRIVATE CHAT ROOM");
               dos.writeUTF("PASSCODE MISSING!! PLEASE EENTER THE OPTION WITH PASSCODE");
            } catch (IOException ex1) {
                Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
       }
       else if(a.length==2)
       {
           if(a[0].toUpperCase().equals("JPRIV"))
           {
               try
               {
                   int i=Integer.parseInt(a[1]);
                   if(i>-1)
                        ret=true;
                   else
                       dos.writeUTF("INVALID PASSCODE VALUE (Negative Number)!! PLEASE ENTER THE OPTION WITH VALID PASSCODE");
               }catch(Exception ex)
               {
                    try {
                        dos.writeUTF("INVALID PASSCODE FORMAT (Not Number)!! PLEASE ENTER THE OPTION WITH VALID PASSCODE");
                    } catch (IOException ex1) {
                        Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex1);
                    }
               }
           }
       }
       else
       {
           try {
                
               dos.writeUTF("WRONG <OPTION>!! PLEASE SPECIFY IT AGAIN");
            } catch (IOException ex1) {
                Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
       }

       return ret;

   }

    private boolean checkFormatOfDelete(String[] a)
   {
       boolean ret=false;
       if(a.length==1)
       {
           try {
               dos.writeUTF("PASSCODE MISSING!! PLEASE EENTER THE OPTION WITH PASSCODE");
            } catch (IOException ex1) {
                Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
       }
       else if(a.length==2)
       {
           if(a[0].toUpperCase().equals("DELETE"))
           {
               try
               {
                   int i=Integer.parseInt(a[1]);
                   if(i>-1)
                       ret=true;
                   else
                       dos.writeUTF("INVALID PASSCODE VALUE (Negative Number)!! PLEASE ENTER THE OPTION WITH VALID PASSCODE");
               }catch(Exception ex)
               {
                    try {
                        dos.writeUTF("INVALID PASSCODE FORMAT (Not Number)!! PLEASE ENTER THE OPTION WITH VALID PASSCODE");
                    } catch (IOException ex1) {
                        Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex1);
                    }
               }
           }
       }
       else
       {
           try {
               dos.writeUTF("WRONG <OPTION>!! PLEASE SPECIFY IT AGAIN");
            } catch (IOException ex1) {
                Logger.getLogger(SerClientThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
       }

       return ret;

   }
}
