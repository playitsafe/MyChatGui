

import java.net.*;
import java.util.LinkedList;
import java.io.*;

/**
 *
 * @author Mutaz Barika
 */

import java.util.Vector;
import javax.swing.JOptionPane;

public class MyChatServer {
    static BroadCastThread bthread;
    static LinkedList<ClientMessInfo> publicClientMessagQueue=new LinkedList<ClientMessInfo>();
    static LinkedList<Socket> clients=new LinkedList<Socket>();
    static LinkedList<ClientInfo> clientsInfo=new LinkedList<ClientInfo>();
    static LinkedList<Socket> PublicRoom=new LinkedList<Socket>();
    
    ////For private rooms
    static LinkedList<PrivateRoom> PrivateRooms=new LinkedList<PrivateRoom>();
    static LinkedList<PrivateRoomThread> privateThreads=new LinkedList<PrivateRoomThread>();

    public static void main(String args[]) throws IOException
    {
        /*
        //For test
        args=new String[1];
        args[0]="12345";
        */

        //Check the entered argument 
        int port = 0;
        if(args.length==1)
        {
            //Check input argument format - PortNumber
            String pattern="[0-9]+[0-9]*";
            String Stringport=args[0];
            if(Stringport.matches(pattern))
            {
                //Vaild port format, assign it to variable
                port=Integer.parseInt(Stringport);
            }
            else
            {
                System.out.println("WRONG PORT FORMAT (Not Number) - PortNumber");
                System.exit(0);
            }            
        }
        else
        {
            System.out.println("YOU DO NOT PROVIDE THE REQUIRED ARGUMENT");
            System.exit(0);
        }

        // Register service on the entered port
        //Run the chat server
        ServerSocket server = new ServerSocket(port);
        System.out.println("Chat Server is running");
        System.out.println("Waiting connections ...");
        
        //Run the broadcast thread
        bthread=new BroadCastThread();
        bthread.start();
        
        //Accept the connection
        while(true)
        {
            //Accept client connection
            Socket ClientSocket=server.accept(); // Wait and accept a connection
            new SerClientThread(ClientSocket).start();
        }
    }
    
    //check name is unique or not
    public synchronized static boolean checkClientName(String name)
    {
        boolean check=false;
        for(int i=0;i<clientsInfo.size();i++)
        {
            if(clientsInfo.get(i).getcName().toUpperCase().equals(name.toUpperCase()))
            {
                check=true;
                break;
            }
        }
        return check;
    }


    public synchronized static void addclients(Socket s)
    {
    	//List of Clients socket
		clients.add(s);
    }
    
    public synchronized static void addClientsInfo(ClientInfo cinfo)
    {
		clientsInfo.add(cinfo);
    }
    
    public synchronized static void removeclient(Socket s,String name)
    {
        for(int i=0;i<clientsInfo.size();i++)
        {
            if(clientsInfo.get(i).getcName().equals(name) && clientsInfo.get(i).getcSocket().equals(s))
            {
                clientsInfo.remove(i);
                break;
            }
        }
    }

    
//Public room code
    public synchronized static void addPublicMessage(ClientMessInfo s)
    {
        publicClientMessagQueue.add(s);
    }
    public synchronized static ClientMessInfo removePublicMessage()
    {
        return publicClientMessagQueue.removeFirst();
    }


    //Code for Private room
    public synchronized static PrivateRoomThread createPrivateRoom(String creatorName,int passcode, Socket creator, int maxmem)
    {
        boolean ret=false;
        try
        {
            boolean check=isPasscodeUnique(passcode);
            if(check)
            {
                PrivateRoom proom=new PrivateRoom(creatorName, passcode, creator, maxmem);
                PrivateRooms.add(proom);
                PrivateRoomThread tPrivateRoom=new PrivateRoomThread(proom);
                tPrivateRoom.start();
                privateThreads.add(tPrivateRoom);
                return tPrivateRoom;
            }
            //ret=true;
        }catch(Exception ex)
        {
            return null;
        }
        return null;
        //return ret;
    }

    public synchronized static boolean isPasscodeUnique(int passcode)
    {
        boolean check=true;
        for(int i=0;i<PrivateRooms.size();i++)
            if(PrivateRooms.get(i).passcode==passcode)
            {
                check=false;
                break;
            }

        return check;
    }

    public synchronized static void addPrivateRoomThread(PrivateRoomThread tPRIV)
    {
            privateThreads.add(tPRIV);
    }

    public synchronized static PrivateRoomThread retrivePrivateRoom(int passcode)
    {
        PrivateRoomThread temp=null;
        for(int i=0;i<privateThreads.size();i++)
            if(privateThreads.get(i).proom.passcode==passcode)
                temp=privateThreads.get(i);

        return temp;
    }
    
    public synchronized static void removePrivateRoom(int passcode)
    {
        //Remove private room
        for(int i=0;i<PrivateRooms.size();i++)
            if(PrivateRooms.get(i).getPasscode()==passcode)
            {
                PrivateRooms.remove(i);
                break;
            }

        //Remove private room thread
        for(int i=0;i<privateThreads.size();i++)
            if(privateThreads.get(i).proom.getPasscode()==passcode)
            {
                //privateThreads.get(i).interrupt();
                privateThreads.get(i).stop();
                privateThreads.remove(i);
                break;
            }
    }
    public synchronized static boolean isCreatorOfPrivateRoom(int passcode, String cname)
    {
        boolean temp=false;
        for(int i=0;i<privateThreads.size();i++)
            if(privateThreads.get(i).proom.getPasscode()==passcode)
                if(privateThreads.get(i).proom.getCreatorName().equals(cname))
                    temp=true;
        return temp;
    }
    
    public static void stopBroadcastThread()
    {
        bthread.stop();
    }

}
