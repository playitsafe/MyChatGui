import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
/**
 *
 * @author Mutaz Barika
 */

public class BroadCastThread extends Thread{

public BroadCastThread() {
	super();
}

public synchronized void sendMessage()
{
	OutputStream s1out;
	try {
	if(MyChatServer.publicClientMessagQueue.isEmpty())
	{
		wait();
	}
        System.out.println("Got a public message");
        ClientMessInfo mess = MyChatServer.removePublicMessage();
        
        //for(Socket clsocket:Server.clients)
        for(Socket clsocket:MyChatServer.PublicRoom)
        {
            s1out = clsocket.getOutputStream();
	        DataOutputStream dos = new DataOutputStream (s1out);
	        //if mess from other port
	        if(clsocket.getPort()!=mess.getSc().getPort())
	           dos.writeUTF(mess.getSenderName()+": "+mess.getMessage());
	        
	    }
	} catch (IOException e) {
               // TODO Auto-generated catch block
		//e.printStackTrace();
                MyChatServer.stopBroadcastThread();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
                MyChatServer.stopBroadcastThread();
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

