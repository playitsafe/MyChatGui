/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Mutaz Barika
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ClientWrite extends Thread {
   Socket s1;
   String name;

  public ClientWrite(Socket s1,String name) {
	super();
	this.s1 = s1;
    this.name=name;
  }
  
  public void run()
  {
      OutputStream s1out = null;
      DataOutputStream dos= null;
	  try {
		  
           // Get an input file handle from the socket and read the input
           s1out = s1.getOutputStream();
           dos = new DataOutputStream (s1out);
           BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
           //Write client name at startup to allow server read it
           dos.writeUTF(name);

	       while(true)
	       {
              //System.out.println("writing rmessage");
              String message = in.readLine();
              dos.writeUTF(message);
	       }
	  } catch (IOException e) {

                // TODO Auto-generated catch block
                //e.printStackTrace();
              try
              {
                  System.out.println("SERVER IS SHUTDOWN");
                  MyChatClient.terminateClientConecction();
              //s1out.close();
                //dos.close();
                //s1.close();
                //this.interrupt();
              }catch(AbstractMethodError ee)
              {
                  
              }
          }
  }
}

