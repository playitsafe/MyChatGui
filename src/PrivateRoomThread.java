

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author Mutaz Barika
 */

public class PrivateRoomThread extends Thread
{

        PrivateRoom proom;

    public PrivateRoomThread(PrivateRoom proom)
    {
        this.proom=proom;

    }

    public synchronized String getCreatorName()
    {
        return proom.getCreatorName();
    }

    public synchronized int getPasscode()
    {
        return proom.getPasscode();
    }

    public synchronized LinkedList<Socket> getMembersSocket()
    {
        return proom.getMemeberSockets();
    }


    public synchronized void addmessage(ClientMessInfo s)
    {
        proom.addPrivateMessag(s);
    }

    public synchronized ClientMessInfo removemessage(){
                    return proom.removePrivateMessag();
    }

    public synchronized void joinPrivateRoom(Socket s){
                    proom.addMemeberSockets(s);
    }

    public synchronized void leavePrivateRoom(Socket s){
                    proom.removeMemeberSockets(s);
    }

    public synchronized int getPrivateRoomLimt() {
            return proom.getPrivateRoomLimt();
    }
    public synchronized int getNumOfMemebers() {
            return proom.memeberSockets.size();
    }


    public synchronized void sendMessage()
    {
        OutputStream s1out = null;

            try {
            if(proom.privateMessagQueue.isEmpty())
            {
                    wait();
            }

            System.out.println("Got a private message");

            ClientMessInfo mess=removemessage();
            for(Socket clsocket:proom.memeberSockets)
            {
                s1out = clsocket.getOutputStream();
                DataOutputStream dos = new DataOutputStream (s1out);
                    if(clsocket.getPort()!=mess.getSc().getPort())
                       dos.writeUTF(mess.getSenderName()+": "+mess.getMessage());
            }
            } catch (IOException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                //s1out.close();
            } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
            }

    }
    public void run()
    {

        while(true)
        {
            sendMessage();
        }



    }

        public synchronized void startmessage() {
                // TODO Auto-generated method stub
                notify();
        }
}

