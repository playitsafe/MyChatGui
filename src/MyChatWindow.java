import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class MyChatWindow extends JFrame {
	
	//chatroom properties
	public static String clientName;
    public static byte[] serverAddress;
    public static int serverPort;
    static Thread ClientRead;
    static Thread ClientWrite;
    static Socket clientSocket;

    //JFrame components
	private JPanel contentPane;
	private JTextField txtUsername;
	private JTextField txtServerip;
	private JTextField txtPort;
	private JButton btnConnectMeTo;
	private JTextField txtEnterPrivateroomPasscode;
	private JTextField txtEnterPasscode;
	private JTextField txtMaxMember;
	private JTextField txtDeleteCode;
	private JButton btnJoinPublicRoom;
	private JButton btnJoinPrivateRoom;
	private JButton btnCreatePrivateRoom;
	private JButton btnDeletePrivateRoom;
	private JPanel optionPanel;
	private JPanel chatPanel;
	private JLabel lblWelcomeXxxYou;
	private JButton btnLeaveChatroom;
	private JTextArea txtrChatMessages;
	private JTextArea txtrInputmessage;
	private JButton btnSend;
	private JComboBox comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyChatWindow frame = new MyChatWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * Create the frame.
	 */
	public MyChatWindow() {
		
		setTitle("MyChat");
		initComponets();
		createEvents();			
	}

	private void initComponets() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 620, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		optionPanel = new JPanel();
		contentPane.add(optionPanel, "optionPanel");
		optionPanel.setName("optionPanel");
		
		JLabel lblSpecifyYourName = new JLabel("Specify Your Name and Sever Info to Get Connected First");
		
		txtUsername = new JTextField();
		txtUsername.setText("Username");
		txtUsername.setColumns(10);
		
		txtServerip = new JTextField();
		txtServerip.setText("127.0.0.1");
		txtServerip.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setText("12345");
		txtPort.setColumns(10);
		
		btnConnectMeTo = new JButton("Connect Me to Server!");
		btnConnectMeTo.setName("btnConnectMeTo");
		
		
		JLabel lblOptions = new JLabel("Options:");
		
		btnJoinPublicRoom = new JButton("Join Public Room");
		
		btnJoinPublicRoom.setVisible(false);
		
		txtEnterPrivateroomPasscode = new JTextField();
		txtEnterPrivateroomPasscode.setText("Enter PrivateRoom Passcode...");
		txtEnterPrivateroomPasscode.setColumns(10);
		txtEnterPrivateroomPasscode.setVisible(false);
		
		btnJoinPrivateRoom = new JButton("Join Private Room");
		
		btnJoinPrivateRoom.setVisible(false);
		
		txtEnterPasscode = new JTextField();
		txtEnterPasscode.setText("Enter PassCode");
		txtEnterPasscode.setColumns(10);
		txtEnterPasscode.setVisible(false);
		
		txtMaxMember = new JTextField();
		txtMaxMember.setText("Max member");
		txtMaxMember.setColumns(10);
		txtMaxMember.setVisible(false);
		
		btnCreatePrivateRoom = new JButton("Create Private Room");
		btnCreatePrivateRoom.setVisible(false);
		
		btnDeletePrivateRoom = new JButton("Delete Private Room");
		btnDeletePrivateRoom.setVisible(false);
		
		txtDeleteCode = new JTextField();
		txtDeleteCode.setText("Enter Passcode to be deleted...");
		txtDeleteCode.setColumns(10);
		txtDeleteCode.setVisible(false);
		
		comboBox = new JComboBox();		
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Please select an option...", "Join Public Chat Room", "Join Private Chat Room", "Create Private Chat Room", "Delete Private Chat Room"}));
		comboBox.setEnabled(false);
		comboBox.setSelectedIndex(0);
		GroupLayout gl_optionPanel = new GroupLayout(optionPanel);
		gl_optionPanel.setHorizontalGroup(
			gl_optionPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_optionPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSpecifyYourName, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
					.addGap(173))
				.addGroup(gl_optionPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblOptions, GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
					.addGap(12)
					.addComponent(comboBox, 0, 415, Short.MAX_VALUE)
					.addGap(115))
				.addGroup(gl_optionPanel.createSequentialGroup()
					.addGap(62)
					.addGroup(gl_optionPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_optionPanel.createSequentialGroup()
							.addGroup(gl_optionPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(txtDeleteCode, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
								.addGroup(gl_optionPanel.createSequentialGroup()
									.addComponent(txtEnterPasscode, GroupLayout.PREFERRED_SIZE, 118, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtMaxMember, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
								.addComponent(txtEnterPrivateroomPasscode, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
							.addGap(18)
							.addGroup(gl_optionPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnCreatePrivateRoom, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnJoinPrivateRoom, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
								.addComponent(btnDeletePrivateRoom, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)))
						.addComponent(btnJoinPublicRoom, GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
						.addComponent(btnConnectMeTo, GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
						.addGroup(gl_optionPanel.createSequentialGroup()
							.addComponent(txtUsername, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(txtServerip, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(txtPort)))
					.addGap(113))
		);
		gl_optionPanel.setVerticalGroup(
			gl_optionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_optionPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSpecifyYourName, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_optionPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtServerip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnConnectMeTo, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_optionPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblOptions))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnJoinPublicRoom)
					.addGap(26)
					.addGroup(gl_optionPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnJoinPrivateRoom)
						.addComponent(txtEnterPrivateroomPasscode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_optionPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtMaxMember, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCreatePrivateRoom)
						.addComponent(txtEnterPasscode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_optionPanel.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(btnDeletePrivateRoom)
						.addComponent(txtDeleteCode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(37))
		);
		optionPanel.setLayout(gl_optionPanel);
		
		chatPanel = new JPanel();
		
		contentPane.add(chatPanel, "chatPanel");
		chatPanel.setName("chatPanel");		
		lblWelcomeXxxYou = new JLabel("Welcome, XXX. You are now in Public Chat Room");
		
		btnLeaveChatroom = new JButton("Leave ChatRoom");
		
		
		txtrChatMessages = new JTextArea();
		txtrChatMessages.setText("Chat Messages");
		
		txtrInputmessage = new JTextArea();
		txtrInputmessage.setText("InputMessage");
		
		btnSend = new JButton("Send");
		GroupLayout gl_chatPanel = new GroupLayout(chatPanel);
		gl_chatPanel.setHorizontalGroup(
			gl_chatPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_chatPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_chatPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_chatPanel.createSequentialGroup()
							.addComponent(lblWelcomeXxxYou, GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
							.addGap(188)
							.addComponent(btnLeaveChatroom, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
							.addGap(7))
						.addGroup(gl_chatPanel.createSequentialGroup()
							.addComponent(txtrChatMessages, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
							.addGap(8))
						.addGroup(gl_chatPanel.createSequentialGroup()
							.addComponent(txtrInputmessage, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSend, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_chatPanel.setVerticalGroup(
			gl_chatPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_chatPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_chatPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLeaveChatroom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblWelcomeXxxYou, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtrChatMessages, GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_chatPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(txtrInputmessage, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
						.addComponent(btnSend, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
					.addContainerGap())
		);
		chatPanel.setLayout(gl_chatPanel);
		
	}

	private void createEvents() {
		
		//////////////When <<connect me>> button is clicked/////////////////////
		btnConnectMeTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String cName = txtUsername.getText();
		        String cServerIP = txtServerip.getText();
		        String port = txtPort.getText();
		       
		      //Check and get entered info
		        if (cName.equals("")||cServerIP.equals("")||port.equals("")) {
		        	JOptionPane.showMessageDialog(null, "You must fill all blanks above!");
		        	//System.exit(0);
				} else {
					try {
						
						String pattern = "[a-zA-Z]+[a-zA-Z]*+[0-9]*";
						
						if (cName.matches(pattern)) {
							
							//Valid Client name, assign it to variable
	                        clientName=cName;
	                        
	                        //Now check Server IP Address format
	                        pattern="^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	                        if (cServerIP.matches(pattern)) {
	                        	//Valid IP address format, assign it to variable
	                            String a[]=cServerIP.split("\\.");
	                            serverAddress=new byte[a.length];
	                            
	                            for(int i=0;i<a.length;i++)
	                                serverAddress[i]=(byte) Integer.parseInt(a[i]);
	                            
	                          //Now check server port
	                            pattern="[0-9]+[0-9]*";
	                            if(port.matches(pattern)) {
	                                //Valid port format, assign it to variable
	                                serverPort=Integer.parseInt(port);
	                                
	                                ////////////if the server info is right////////////
	                                
	                                try {
	                	            	// Open connection to a server, at port specified port
	                					clientSocket = new Socket(InetAddress.getByAddress(serverAddress),serverPort);
	                					
	                					
										// run client read thread
	                		            ClientRead = new ClientRead(clientSocket, optionPanel, chatPanel);
	                		            ClientRead.start();
	                		        
	                		            // run client write thread
	                		            ClientWrite =new ClientWrite(clientSocket,clientName);
	                		            ClientWrite.start();
	                		            
	                		            /*
	                		            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
	                		            String st = new String (dis.readUTF());
	                		            
	                		            if (st.startsWith("Welcome")) {
	                		            	JOptionPane.showMessageDialog(null, "yes");
										} else {
											JOptionPane.showMessageDialog(null, "nooooo");
										}
										*/
	                		            
	                		            comboBox.setEnabled(true);
	                		            
	                				} catch (SocketException ex1) {
	                					
	                					//Connection Refused
	                		            if(clientSocket==null)
	                		            	JOptionPane.showMessageDialog(null, "CONNECTION REFUSED - Check Server IP Address and Port then try again");
	                		            else
	                		            {
	                		            	JOptionPane.showMessageDialog(null, "ERROR");
	                		                terminateClientConecction();
	                		            }	                		            
	                				} catch(Exception ex2){
	                		            if(clientSocket==null)
	                		            	JOptionPane.showMessageDialog(null, "ERROR");
	                		            else
	                		            {
	                		            	JOptionPane.showMessageDialog(null, "ERROR");
	                		                terminateClientConecction();
	                		            }
	                		        }	    	                        
	                            }else{
	                            	JOptionPane.showMessageDialog(null, "WRONG PORT FORMAT - PortNumber");
	                                //System.exit(0);
	                            }
	                            
							}else{
                            	JOptionPane.showMessageDialog(null, "WRONG IP ADDRESS WORNG - ChatServerIP");
                                //System.exit(0);
                            }
	                        
						} else {
							JOptionPane.showMessageDialog(null, "INVAILD NAME FORMAT - ClientName");
							//System.exit(0);
						} 
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "NUMBER/TYPE OF CONTENT IS NOT CORRECT");
						//System.exit(0);
					}
				}		        
			}
		});
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedIndex = comboBox.getSelectedIndex();
				switch (selectedIndex) {
				case 0:
					btnJoinPublicRoom.setVisible(false);
					
					txtEnterPrivateroomPasscode.setVisible(false);
					btnJoinPrivateRoom.setVisible(false);
					
					txtEnterPasscode.setVisible(false);
					txtMaxMember.setVisible(false);
					btnCreatePrivateRoom.setVisible(false);
					
					txtDeleteCode.setVisible(false);
					btnDeletePrivateRoom.setVisible(false);
					
					break;
				case 1:
					btnJoinPublicRoom.setVisible(true);
					
					txtEnterPrivateroomPasscode.setVisible(false);
					btnJoinPrivateRoom.setVisible(false);
					
					txtEnterPasscode.setVisible(false);
					txtMaxMember.setVisible(false);
					btnCreatePrivateRoom.setVisible(false);
					
					txtDeleteCode.setVisible(false);
					btnDeletePrivateRoom.setVisible(false);
					
					break;
				
				case 2:
					btnJoinPublicRoom.setVisible(false);
					
					txtEnterPrivateroomPasscode.setVisible(true);
					btnJoinPrivateRoom.setVisible(true);
					
					txtEnterPasscode.setVisible(false);
					txtMaxMember.setVisible(false);
					btnCreatePrivateRoom.setVisible(false);
					
					txtDeleteCode.setVisible(false);
					btnDeletePrivateRoom.setVisible(false);
					
					break;
					
				case 3:
					btnJoinPublicRoom.setVisible(false);
					
					txtEnterPrivateroomPasscode.setVisible(false);
					btnJoinPrivateRoom.setVisible(false);
					
					txtEnterPasscode.setVisible(true);
					txtMaxMember.setVisible(true);
					btnCreatePrivateRoom.setVisible(true);
					
					txtDeleteCode.setVisible(false);
					btnDeletePrivateRoom.setVisible(false);
					
					break;
					
				case 4:
					btnJoinPublicRoom.setVisible(false);
					
					txtEnterPrivateroomPasscode.setVisible(false);
					btnJoinPrivateRoom.setVisible(false);
					
					txtEnterPasscode.setVisible(false);
					txtMaxMember.setVisible(false);
					btnCreatePrivateRoom.setVisible(false);
					
					txtDeleteCode.setVisible(true);
					btnDeletePrivateRoom.setVisible(true);
					
					break;
				default:
					break;
				}
			}
		});
		
		btnJoinPublicRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionPanel.setVisible(false);
				chatPanel.setVisible(true);
				
				try {
					DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
					dos.writeUTF("JPUB");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				/*
				String cServerIP = txtServerip.getText();
		        String port = txtPort.getText();
		        String a[]=cServerIP.split("\\.");
                serverAddress=new byte[a.length];
                
                for(int i=0;i<a.length;i++)
                    serverAddress[i]=(byte) Integer.parseInt(a[i]);
                
                serverPort=Integer.parseInt(port);
				
				try {
					clientSocket = new Socket(InetAddress.getByAddress(serverAddress),serverPort);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				MyChatServer.PublicRoom.add(clientSocket);
				//Send message to all members notify them this client is joined the chat room
                MyChatServer.addPublicMessage(new ClientMessInfo(clientSocket,"Control",clientName+" is joined"));
                MyChatServer.bthread.startmessage();
                */
                
			}
		});
		
		btnJoinPrivateRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		btnLeaveChatroom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				optionPanel.setVisible(true);
				chatPanel.setVisible(false);
			}
		});
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
        }catch (SecurityException ex2)
        {
            Logger.getLogger(MyChatClient.class.getName()).log(Level.SEVERE, null, ex2);
        }

    }
	
	public static void popUpAlert(String s) {
		JOptionPane.showMessageDialog(null, s);
		
	}
}
