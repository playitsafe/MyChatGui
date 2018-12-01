

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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

/**
 *
 * @author Mutaz Barika
 */
//ClientRead thread to read data written by others
public class ClientRead extends Thread {
   Socket s1;
   JPanel optionPanel;
   JPanel chatPanel;
   JViewport viewPort;

  public ClientRead(Socket s1) {
	super();
	this.s1 = s1;
  }
  
  public ClientRead(Socket s1, JPanel optionPanel, JPanel chatPanel, JViewport viewPort) {
	super();
	this.s1 = s1;
	this.optionPanel = optionPanel;
	this.chatPanel = chatPanel;
	this.viewPort = viewPort;
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
                Component[] viewPortComp = viewPort.getComponents();
                
                if(st.toUpperCase().equals("SHUTDOWN"))
                {
                    MyChatClient.terminateClientConecction();
                    break;
                }                
                else if(st.equals("ChatSysInfo_Connected"))
                {
                	System.out.println(st);
                	for (int i = 0; i < optionComp.length; i++) 
                	{
                		if (optionComp[i].getName()!=null) {
                			
                			if (optionComp[i].getName().equals("btnConnectMeTo")) {
                				optionComp[i].setEnabled(false);
                				((JButton) optionComp[i]).setText("You are Connected!");
    						}
                			
                			if (optionComp[i].getName().equals("comboBox")) {
                				optionComp[i].setEnabled(true);
							}
						}
                	}
                }
                else if (st.equals("ChatSysInfo_NameExisted")) 
                {
					MyChatWindow.popUpAlert("THE ENTERED NAME IS ALREADY USED, PLEASE ENTER ANOTHER NAME");
				}
                else
                {
                	//System.out.println(st);
                	for (int i = 0; i < viewPortComp.length; i++) 
                	{
                		if (viewPortComp[i].getName()!=null) {                			
                			if (viewPortComp[i].getName().equals("chatArea")) {
                				((JTextArea) viewPortComp[i]).append(st+"\n");                 				
    						}                			
						}
                	}
                }                               
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
