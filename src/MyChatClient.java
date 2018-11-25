

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mutaz Barika
 */

public class MyChatClient {

    public static String clientName;
    public static byte[] serverAddress;
    public static int serverPort;
    static Thread ClientRead;
    static Thread ClientWrite;
    static Socket clientSocket;

    public static void main(String args[]) throws IOException 
    {
        /*
        //For test
        args=new String[3];
        args[0]="Mutaz";
        args[1]="127.0.0.1";
        args[2]="12345";
        */
        
        MyChatClient client=new MyChatClient();
        client.runClientApp(args);
    }
    
    private void runClientApp(String[] args)
    {
        try
        {
            //Call check method to check entered arguments
            checkInputArguments(args);

            // Open connection to a server, at port specified port
            clientSocket = new Socket(InetAddress.getByAddress(serverAddress),serverPort);

            // run client read thread
            ClientRead =new ClientRead(clientSocket);
            ClientRead.start();
            // run client write thread
            ClientWrite =new ClientWrite(clientSocket,clientName);
            ClientWrite.start();
        
        }catch(SocketException ex1) {
            //Connection Refused
            if(clientSocket==null)
                System.out.println("CONNECTION REFUSED - Check Server IP Address and Port then try again");
            else
            {
                System.out.println("ERROR");
                terminateClientConecction();
            }
        //}catch(InterruptedException ex2){}
        }catch(Exception ex2){
            if(clientSocket==null)
                System.out.println("ERROR");
            else
            {
                System.out.println("ERROR");
                terminateClientConecction();
            }

        }
    }


    private static void checkInputArguments(String[] args)
    {
        //Check and get entered arguments
        String cName=null;
        String cServerIP=null;
        String port = null;

        if(args.length==3)
        {
            //Parse input and check if all arguments are in correct format
            try{
                    //Check client name format
                    cName=args[0];
                    String pattern = "[a-zA-Z]+[a-zA-Z]*+[0-9]*";
                    
                    
                    if(cName.matches(pattern))
                    {
                        //Vaild Client name, assign it to variable
                        clientName=cName;

                        //Now check Server IP Address format
                        pattern="^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
                        cServerIP=args[1];
                        if(cServerIP.matches(pattern))
                        {

                            //Vaild IP address format, assign it to variable
                            String a[]=cServerIP.split("\\.");
                            serverAddress=new byte[a.length];
                            for(int i=0;i<a.length;i++)
                                serverAddress[i]=(byte) Integer.parseInt(a[i]);

                            //Now check serverp port
                            pattern="[0-9]+[0-9]*";
                            port=args[2];
                            if(port.matches(pattern))
                            {
                                //Vaild port format, assign it to variable
                                serverPort=Integer.parseInt(port);
                            }
                            else
                            {
                                System.out.println("WRONG PORT FORMAT - PortNumber");
                                System.exit(0);
                            }
                        }
                        else
                        {
                            System.out.println("WRONG IP ADDRESS WORNG - ChatServerIP");
                            System.exit(0);
                        }
                    }
                    else
                    {
                        System.out.println("INVAILD NAME FORMAT - ClientName");
                        System.exit(0);
                    }
                }catch(NumberFormatException ex)
                {
                    System.out.println("NUMBER/TYPE OF ARGUMENTS IS NOT CORRECT");
                    System.exit(0);
                }

        }
        else
        {
            System.out.println("YOU DO NOT PROVIDE THE REQUIRED ARGUMENTS");
            System.exit(0);
        }
    }

    public static void terminateClientConecction()
    {
        try {
            ClientRead.interrupt();
            ClientWrite.interrupt();
            clientSocket.close();
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(MyChatClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex2) {
            Logger.getLogger(MyChatClient.class.getName()).log(Level.SEVERE, null, ex2);
        }

    }
}
