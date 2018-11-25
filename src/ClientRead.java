

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Mutaz Barika
 */
//ClientRead thread to read data written by others
public class ClientRead extends Thread {
   Socket s1;

  public ClientRead(Socket s1) {
	super();
	this.s1 = s1;
  }
  
  public void run()
  {
      InputStream s1In=null;
      DataInputStream dis=null;
	  try {
		  
             s1In = s1.getInputStream();
             dis = new DataInputStream(s1In);
	         while(true)
	         { 
                String st = new String (dis.readUTF());
                
                if(st.toUpperCase().equals("SHUTDOWN"))
                {
                    MyChatClient.terminateClientConecction();
                    break;
                }
                else
                {
                	////////////////////////////////////////////////////
                	System.out.println(st);
                	//String ss = new String(System.out.println("haha"));
                	//System.out.println("haha");
                	MyChatWindow.testPop(st);
                }
                    
                
                //dis.close();
	         }
	  } catch (IOException e) {
              try {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                  //System.out.println("SERVER IS SHUTDOWN");
                  //MyChatClient.terminateClientConecction();
            	  MyChatWindow.terminateClientConecction();
                //s1In.close();
                //dis.close();
                //s1.close();
                //this.interrupt();
            } catch (AbstractMethodError ex) {
            }
		}
  }
}
