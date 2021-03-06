import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;

import java.awt.CardLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
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
import javax.swing.JScrollPane;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.Toolkit;

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
	private JTextField txtCreatePasscode;
	private JTextField txtMaxMember;
	private JTextField txtDeleteCode;
	private JButton btnJoinPublicRoom;
	private JButton btnJoinPrivateRoom;
	private JButton btnCreatePrivateRoom;
	private JButton btnDeletePrivateRoom;
	private JPanel optionPanel;
	private JPanel chatPanel;
	private JLabel lblWelcomeHeader;
	private JButton btnLeaveChatroom;
	private JButton btnSend;
	private JComboBox comboBox;
	private JTextArea chatArea;
	private JScrollPane scrollPane;
	private JViewport viewPort;
	private JScrollPane inputScrollPane;
	private JTextArea txtInputmessage;
	private JScrollBar inputVerticalBar;
	private JScrollBar inputHorizontalBar;
	private JScrollBar chatAreaBar;

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
		setIconImage(Toolkit.getDefaultToolkit().getImage(MyChatWindow.class.getResource("/resource/icon.png")));
		
		setTitle("MyChat");
		
		//a method hold all Gui components
		initComponets();
		//a method hold all event listeners
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
		TextPrompt namePrompt = new TextPrompt("UserName", txtUsername);
		namePrompt.changeStyle(Font.BOLD+Font.ITALIC);
		namePrompt.changeAlpha(0.4f);		
		txtUsername.setColumns(10);
		txtUsername.setName("txtUsername");
		
		txtServerip = new JTextField();
		TextPrompt ipPrompt = new TextPrompt("Server IP", txtServerip);
		txtServerip.setText("127.0.0.1");
		ipPrompt.changeStyle(Font.BOLD+Font.ITALIC);
		ipPrompt.changeAlpha(0.4f);
		txtServerip.setColumns(10);
		txtServerip.setName("txtServerip");
		
		txtPort = new JTextField();
		txtPort.setText("12345");
		TextPrompt portPrompt = new TextPrompt("Server Port", txtPort);
		portPrompt.changeStyle(Font.BOLD+Font.ITALIC);
		portPrompt.changeAlpha(0.4f);
		txtPort.setColumns(10);
		txtPort.setName("txtPort");
		
		btnConnectMeTo = new JButton("Connect Me to Server!");
		btnConnectMeTo.setName("btnConnectMeTo");
		
		
		JLabel lblOptions = new JLabel("Options:");
		
		btnJoinPublicRoom = new JButton("Join Public Room");
		
		btnJoinPublicRoom.setVisible(false);
		
		txtEnterPrivateroomPasscode = new JTextField();
		//txtEnterPrivateroomPasscode.setText("111");
		TextPrompt jprivPrompt = new TextPrompt("PrivateRoom PassCode", txtEnterPrivateroomPasscode);
		jprivPrompt.changeStyle(Font.BOLD+Font.ITALIC);
		jprivPrompt.changeAlpha(0.4f);
		txtEnterPrivateroomPasscode.setColumns(10);
		txtEnterPrivateroomPasscode.setVisible(false);
		
		btnJoinPrivateRoom = new JButton("Join Private Room");
		
		btnJoinPrivateRoom.setVisible(false);
		
		txtCreatePasscode = new JTextField();
		//txtCreatePasscode.setText("111");
		TextPrompt createPrompt = new TextPrompt("Create PassCode", txtCreatePasscode);
		createPrompt.changeStyle(Font.BOLD+Font.ITALIC);
		createPrompt.changeAlpha(0.4f);
		txtCreatePasscode.setColumns(10);
		txtCreatePasscode.setVisible(false);
		
		txtMaxMember = new JTextField();
		//txtMaxMember.setText("11");
		TextPrompt maxMemPrompt = new TextPrompt("Max Member", txtMaxMember);
		maxMemPrompt.changeStyle(Font.BOLD+Font.ITALIC);
		maxMemPrompt.changeAlpha(0.4f);
		txtMaxMember.setColumns(10);
		txtMaxMember.setVisible(false);
		
		btnCreatePrivateRoom = new JButton("Create Private Room");
		
		btnCreatePrivateRoom.setVisible(false);
		
		btnDeletePrivateRoom = new JButton("Delete Private Room");		
		btnDeletePrivateRoom.setVisible(false);
		
		txtDeleteCode = new JTextField();
		//txtDeleteCode.setText("Enter Passcode to be deleted...");
		TextPrompt deletePrompt = new TextPrompt("Passcode to be deleted", txtDeleteCode);
		deletePrompt.changeStyle(Font.BOLD+Font.ITALIC);
		deletePrompt.changeAlpha(0.4f);
		txtDeleteCode.setColumns(10);
		txtDeleteCode.setVisible(false);
		
		comboBox = new JComboBox();		
		comboBox.setName("comboBox");
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Please select an option...", "Join Public Chat Room", "Join Private Chat Room", "Create Private Chat Room", "Delete Private Chat Room", "Disconnect and SHUTDOWN" }));
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
									.addComponent(txtCreatePasscode, GroupLayout.PREFERRED_SIZE, 118, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtMaxMember, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
								.addComponent(txtEnterPrivateroomPasscode, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED, 18, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_optionPanel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(btnDeletePrivateRoom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnCreatePrivateRoom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnJoinPrivateRoom, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)))
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
						.addComponent(txtCreatePasscode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
		lblWelcomeHeader = new JLabel("Welcome. You are now in Chat Room");
		
		btnLeaveChatroom = new JButton("Leave ChatRoom");
		
		btnSend = new JButton("Send");		
		
		scrollPane = new JScrollPane();
		
		inputScrollPane = new JScrollPane();
		GroupLayout gl_chatPanel = new GroupLayout(chatPanel);
		gl_chatPanel.setHorizontalGroup(
			gl_chatPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_chatPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_chatPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_chatPanel.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_chatPanel.createSequentialGroup()
							.addComponent(lblWelcomeHeader, GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
							.addGap(188)
							.addComponent(btnLeaveChatroom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(7))
						.addGroup(Alignment.LEADING, gl_chatPanel.createSequentialGroup()
							.addComponent(inputScrollPane, GroupLayout.PREFERRED_SIZE, 470, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(btnSend, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_chatPanel.setVerticalGroup(
			gl_chatPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_chatPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_chatPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLeaveChatroom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblWelcomeHeader, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(gl_chatPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnSend, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(inputScrollPane, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
					.addGap(0))
		);
		
		txtInputmessage = new JTextArea();
		inputScrollPane.setViewportView(txtInputmessage);
		
		chatArea = new JTextArea();
		
		chatArea.setEditable(false);
		chatArea.setName("chatArea");
		scrollPane.setViewportView(chatArea);
		
		viewPort = scrollPane.getViewport();
		viewPort.setName("viewPort");
		chatPanel.setLayout(gl_chatPanel);
		
		DefaultCaret caret = (DefaultCaret)chatArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

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
	                		            ClientRead = new ClientRead(clientSocket, optionPanel, chatPanel, viewPort);
	                		            ClientRead.start();
	                		        
	                		            // run client write thread
	                		            ClientWrite =new ClientWrite(clientSocket,clientName);
	                		            ClientWrite.start();
	                		            
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
		
		//action listener for dropdown list
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedIndex = comboBox.getSelectedIndex();
				switch (selectedIndex) {
				case 0:
					btnJoinPublicRoom.setVisible(false);
					
					txtEnterPrivateroomPasscode.setVisible(false);
					txtEnterPrivateroomPasscode.setText("");
					btnJoinPrivateRoom.setVisible(false);
					
					txtCreatePasscode.setVisible(false);
					txtCreatePasscode.setText("");
					txtMaxMember.setVisible(false);
					txtMaxMember.setText("");
					btnCreatePrivateRoom.setVisible(false);
					
					txtDeleteCode.setVisible(false);
					txtDeleteCode.setText("");
					btnDeletePrivateRoom.setVisible(false);
					
					break;
				case 1:
					btnJoinPublicRoom.setVisible(true);
					
					txtEnterPrivateroomPasscode.setVisible(false);
					btnJoinPrivateRoom.setVisible(false);
					
					txtCreatePasscode.setVisible(false);
					txtMaxMember.setVisible(false);
					btnCreatePrivateRoom.setVisible(false);
					
					txtDeleteCode.setVisible(false);
					btnDeletePrivateRoom.setVisible(false);
					
					break;
				
				case 2:
					btnJoinPublicRoom.setVisible(false);
					
					txtEnterPrivateroomPasscode.setVisible(true);
					txtEnterPrivateroomPasscode.setText("");
					btnJoinPrivateRoom.setVisible(true);
					
					txtCreatePasscode.setVisible(false);
					txtMaxMember.setVisible(false);
					btnCreatePrivateRoom.setVisible(false);
					
					txtDeleteCode.setVisible(false);
					btnDeletePrivateRoom.setVisible(false);
					
					break;
					
				case 3:
					btnJoinPublicRoom.setVisible(false);
					
					txtEnterPrivateroomPasscode.setVisible(false);
					btnJoinPrivateRoom.setVisible(false);
					
					txtCreatePasscode.setVisible(true);
					txtCreatePasscode.setText("");
					txtMaxMember.setVisible(true);
					txtMaxMember.setText("");
					btnCreatePrivateRoom.setVisible(true);
					
					txtDeleteCode.setVisible(false);
					btnDeletePrivateRoom.setVisible(false);
					
					break;
					
				case 4:
					btnJoinPublicRoom.setVisible(false);
					
					txtEnterPrivateroomPasscode.setVisible(false);
					btnJoinPrivateRoom.setVisible(false);
					
					txtCreatePasscode.setVisible(false);
					txtMaxMember.setVisible(false);
					btnCreatePrivateRoom.setVisible(false);
					
					txtDeleteCode.setVisible(true);
					txtDeleteCode.setText("");
					btnDeletePrivateRoom.setVisible(true);
					
					break;
					
				case 5:
					
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog (null, "Do you really want to Disconnect and EXIT?","Warning",dialogButton);
					if(dialogResult == JOptionPane.YES_OPTION){
						try {
							DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
							dos.writeUTF("SHUTDOWN");
							
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
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
				lblWelcomeHeader.setText("Hi "+txtUsername.getText()+", you are now in Public Chat Room!");
				
				try {
					DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
					dos.writeUTF("JPUB");
				} catch (IOException e1) {
					e1.printStackTrace();
				}		
			}
		});
		
		btnCreatePrivateRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String passCode = txtCreatePasscode.getText();
				String maxMem = txtMaxMember.getText();
				String pattern="[0-9]+[0-9]*";
				
				if (passCode.equals("")||maxMem.equals("")) {
					popUpAlert("Please specify both PassCode & Max Number of Members");
				} else if (passCode.matches(pattern)) {
					if (maxMem.matches(pattern)) {
						int temp = Integer.parseInt(maxMem);
						if (temp>1) {
							//send the code
							try {
								DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
								dos.writeUTF("CREATE "+passCode+" "+maxMem);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
							//set welcome header
							lblWelcomeHeader.setText("Hi, "+txtUsername.getText()+", you are now in Private Chat Room "+txtCreatePasscode.getText());
							//int passCodeInt = Integer.parseInt(passCode);
							//boolean checkPasscode=MyChatServer.isPasscodeUnique(passCodeInt);								
							
							/*
							if (checkPasscode) {
								//if matched,send the code
								try {
									DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
									dos.writeUTF("CREATE "+passCode+" "+maxMem);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								popUpAlert(Integer.toString(passCodeInt));
								//switch to chat window
								optionPanel.setVisible(false);
								chatPanel.setVisible(true);
								lblWelcomeHeader.setText("Welcome, "+txtUsername.getText()+". You are now in Private ChatRoom "+passCode);
							} else {
								popUpAlert("Private ChatRoom with code "+passCode+" already existed!");
							}
							*/
						} else {
							popUpAlert("INVALID maxMEM VALUE!!");
						}
					} else {
						popUpAlert("INVALID maxMEM VALUE!!");
					}
				} else {
					popUpAlert("Wrong PassCode Format!");
				}
			}
		});
		
		btnJoinPrivateRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String passCode = txtEnterPrivateroomPasscode.getText();				
				String pattern="[0-9]+[0-9]*";
				//optionPanel.setVisible(false);
				//chatPanel.setVisible(true);
				
				if (passCode.equals("")) {
					popUpAlert("PassCode Missing!");
				} else if (!passCode.matches(pattern)) {
					popUpAlert("Wrong PassCode Format!");
				} else {
					try {
						DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
						dos.writeUTF("JPRIV "+passCode);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					lblWelcomeHeader.setText("Hi, "+txtUsername.getText()+", you joined in Private Chat Room "+txtEnterPrivateroomPasscode.getText());
				}
				
			}
		});
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String input = txtInputmessage.getText();
				if (input.equals("")) {
					popUpAlert("Please say something!");
				} else {
					try {
						DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
						dos.writeUTF(input+"\n");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					chatArea.append("Me: "+input+"\n");
					txtInputmessage.setText("");
				}
				
			}
		});
		
		btnLeaveChatroom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Do you really want to exit current ChatRoom?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					try {
						DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
						dos.writeUTF("ECCR");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					chatArea.setText("");
					optionPanel.setVisible(true);
					chatPanel.setVisible(false);
					//comboBox.setSelectedIndex(0);
					chatPanel.setOpaque(false);
					
					//chatPanel.setLayout(null);
					optionPanel.setVisible(true);   
					//optionPanel.revalidate();
					//optionPanel.repaint();
					optionPanel.setOpaque(true);
				
				}
			}
		});
		
		btnDeletePrivateRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String passCode = txtDeleteCode.getText();
				String pattern="[0-9]+[0-9]*";
				if (passCode.equals("")) {
					popUpAlert("PassCode missing!");
				} else if (!passCode.matches(pattern)) {
					popUpAlert("Wrong PassCode Format!");
				} else {
					try {
						DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
						dos.writeUTF("DELETE "+passCode);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
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
            Logger.getLogger(MyChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }catch (SecurityException ex2)
        {
            Logger.getLogger(MyChatWindow.class.getName()).log(Level.SEVERE, null, ex2);
        }

    }
	
	public static void popUpAlert(String s) {
		JOptionPane.showMessageDialog(null, s);
		
	}
}
