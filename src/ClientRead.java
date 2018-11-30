

import java.awt.Component;
import java.awt.Container;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Mutaz Barika
 */
//ClientRead thread to read data written by others
public class ClientRead extends Thread {
   Socket s1;
   JPanel optionPanel;
   JPanel chatPanel;
   

  public ClientRead(Socket s1) {
	super();
	this.s1 = s1;
  }
  
  public ClientRead(Socket s1, JPanel optionPanel, JPanel chatPanel) {
	super();
	this.s1 = s1;
	this.optionPanel = optionPanel;
	this.chatPanel = chatPanel;
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
                Component[] optionComp = optionPanel.getComponents();
                Component[] chatComp = chatPanel.getComponents();
                
                if(st.toUpperCase().equals("SHUTDOWN"))
                {
                    MyChatClient.terminateClientConecction();
                    break;
                }
                else
                {
                	////////////////////////////////////////////////////
                	System.out.println(st);
                	if (st.startsWith("System: Welcome")) {
                		for (int i = 0; i < optionComp.length; i++) {
                    		if (optionComp[i].getName()!=null) {
                    			if (optionComp[i].getName().equals("btnConnectMeTo")) {
                    				optionComp[i].setEnabled(false);
                    				((JButton) optionComp[i]).setText("Connected!");
        						}
                    			
                    			if (optionComp[i].getName().equals("comboBox")) {
                    				optionComp[i].setEnabled(true);
								}
    						}
                    	}
					}
                	
                	                	                	
                	
                	
                	
                	MyChatWindow.popUpAlert(st);
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
