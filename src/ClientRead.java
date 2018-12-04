

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
                    MyChatWindow.terminateClientConecction();
                    break;
                }                
                else if(st.equals("ChatSysInfo_Connected"))
                {
                	
                	for (int i = 0; i < optionComp.length; i++) 
                	{
                		String compName = optionComp[i].getName();
                		if (compName!=null) {
                			
                			if (compName.equals("btnConnectMeTo")) {
                				optionComp[i].setEnabled(false);
                				((JButton) optionComp[i]).setText("You are Connected!");
    						}
                			
                			if (compName.equals("txtUsername")||
                				compName.equals("txtServerip")||
                				compName.equals("txtPort")) 
                			{
                				optionComp[i].setEnabled(false);                				
    						}
                			
                			if (compName.equals("comboBox")) {
                				optionComp[i].setEnabled(true);
							}
						}
                	}
                }
                else if (st.equals("ChatSysInfo_NameExisted")) 
                {
					MyChatWindow.popUpAlert("THE ENTERED NAME IS ALREADY USED, PLEASE ENTER ANOTHER NAME");
				}
                else if (st.equals("ChatSysInfo_ProomCreated")) 
                {
                	optionPanel.setVisible(false);
                	chatPanel.setVisible(true);
                	
                	for (int i = 0; i < viewPortComp.length; i++) 
                	{
                		if (viewPortComp[i].getName()!=null) {                			
                			if (viewPortComp[i].getName().equals("chatArea")) {
                				((JTextArea) viewPortComp[i]).append("Control: Private Chat Room Created!\n");                 				
    						}                			
						}
                	}
				}
                else if (st.equals("CreateProomFail_CodeExisted"))
                {
                	MyChatWindow.popUpAlert("PassCode existed! Try another code!");
				}
                else if (st.equals("ChatSysInfo_RoomFull")) 
                {
                	MyChatWindow.popUpAlert("ChatRoom is Full! Try another code!");
				}
                else if (st.equals("ChatSysInfo_NoSuchCode")) 
                {
                	MyChatWindow.popUpAlert("No ChatRoom with that Code! Try another code!");
				}
                else if (st.equals("ChatSysInfo_ProomJoined")) 
                {
                	optionPanel.setVisible(false);
                	chatPanel.setVisible(true);                	
				}
                else if (st.equals("ChatSysInfo_NotCreator")) 
				{
					MyChatWindow.popUpAlert("YOU ARE NOT A CREATOR OF THIS PRIVATE ROOM!");
				} 
				else if (st.equals("DeleteInfo_CodeNotFound"))
				{
					MyChatWindow.popUpAlert("THERE IS NO PRIVATE ROOM WITH THAT PASSCODE!");
				}
				else if (st.equals("ChatSysInfo_ForceLeave")) 
				{
					MyChatWindow.popUpAlert("THIS PRIVATE ROOM IS DELETED BY CREATOR, YOU ARE FORCEDLY EXIT FROM IT");
					optionPanel.setVisible(true);
                	chatPanel.setVisible(false);
				}
				else if (st.equals("ChatSysInfo_ProomDeleted")) 
				{
					MyChatWindow.popUpAlert("Private Room is deleted!");
				}
                else
                {
                	//System.out.println(st);
                	for (int i = 0; i < viewPortComp.length; i++) 
                	{
                		if (viewPortComp[i].getName()!=null) {                			
                			if (viewPortComp[i].getName().equals("chatArea")) {
                				((JTextArea) viewPortComp[i]).append(st);                 				
    						}                			
						}
                	}
                }                               
             }
	  } catch (IOException e) {
              try {
                
                  //System.out.println("SERVER IS SHUTDOWN");
                  //MyChatClient.terminateClientConecction();
            	  MyChatWindow.terminateClientConecction();
                
            } catch (AbstractMethodError ex) {
            }
		}
  }

}
