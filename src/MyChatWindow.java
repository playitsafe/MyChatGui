import java.awt.BorderLayout;
import java.awt.EventQueue;
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
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

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
		contentPane.add(optionPanel, "name_53529996944445");
		
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
		
		
		JLabel lblOptions = new JLabel("Options:");
		
		btnJoinPublicRoom = new JButton("Join Public Room");
		
		btnJoinPublicRoom.setEnabled(false);
		
		txtEnterPrivateroomPasscode = new JTextField();
		txtEnterPrivateroomPasscode.setText("Enter PrivateRoom Passcode...");
		txtEnterPrivateroomPasscode.setColumns(10);
		
		btnJoinPrivateRoom = new JButton("Join Private Room");
		
		btnJoinPrivateRoom.setEnabled(false);
		
		txtEnterPasscode = new JTextField();
		txtEnterPasscode.setText("Enter PassCode");
		txtEnterPasscode.setColumns(10);
		
		txtMaxMember = new JTextField();
		txtMaxMember.setText("Max member");
		txtMaxMember.setColumns(10);
		
		btnCreatePrivateRoom = new JButton("Create Private Room");
		btnCreatePrivateRoom.setEnabled(false);
		
		btnDeletePrivateRoom = new JButton("Delete Private Room");
		btnDeletePrivateRoom.setEnabled(false);
		
		txtDeleteCode = new JTextField();
		txtDeleteCode.setText("Enter PrivateRoom Passcode...");
		txtDeleteCode.setColumns(10);
		GroupLayout gl_optionPanel = new GroupLayout(optionPanel);
		gl_optionPanel.setHorizontalGroup(
			gl_optionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_optionPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSpecifyYourName, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
					.addGap(173))
				.addGroup(gl_optionPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblOptions, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(542))
				.addGroup(gl_optionPanel.createSequentialGroup()
					.addGap(62)
					.addGroup(gl_optionPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnJoinPublicRoom, GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
						.addComponent(btnConnectMeTo, GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
						.addGroup(gl_optionPanel.createSequentialGroup()
							.addComponent(txtUsername, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(txtServerip, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(txtPort))
						.addGroup(gl_optionPanel.createSequentialGroup()
							.addGroup(gl_optionPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(txtDeleteCode, GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
								.addGroup(gl_optionPanel.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_optionPanel.createSequentialGroup()
										.addComponent(txtEnterPasscode)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(txtMaxMember, GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
									.addComponent(txtEnterPrivateroomPasscode, GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)))
							.addGap(18)
							.addGroup(gl_optionPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnCreatePrivateRoom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnJoinPrivateRoom, GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
								.addComponent(btnDeletePrivateRoom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
					.addGap(113))
		);
		gl_optionPanel.setVerticalGroup(
			gl_optionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_optionPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSpecifyYourName, GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_optionPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtServerip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnConnectMeTo, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblOptions)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnJoinPublicRoom)
					.addGap(18)
					.addGroup(gl_optionPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtEnterPrivateroomPasscode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnJoinPrivateRoom))
					.addGap(18)
					.addGroup(gl_optionPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtEnterPasscode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtMaxMember, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCreatePrivateRoom))
					.addGap(18)
					.addGroup(gl_optionPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDeletePrivateRoom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(txtDeleteCode))
					.addGap(73))
		);
		optionPanel.setLayout(gl_optionPanel);
		
		chatPanel = new JPanel();
		contentPane.add(chatPanel, "name_53538330000359");
		
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
	                		            ClientRead = new ClientRead(clientSocket);
	                		            ClientRead.start();
	                		            // run client write thread
	                		            ClientWrite =new ClientWrite(clientSocket,clientName);
	                		            ClientWrite.start();
	                		            
	                		            //ClientRead cRead = new ClientRead(s1);
	                		            //s1In = clientSocket.getInputStream();
	                		            //DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
	                		            //String st = new String (dis.readUTF());		
	                		            
	                		            //JOptionPane.showMessageDialog(null, "how to check the name");
	                		            
	                		            //enable the option buttons below
	                		            btnConnectMeTo.setEnabled(false);
		    	                        btnJoinPublicRoom.setEnabled(true);
		    	                    	btnJoinPrivateRoom.setEnabled(true);
		    	                    	btnCreatePrivateRoom.setEnabled(true);
		    	                    	btnDeletePrivateRoom.setEnabled(true);
	                		            
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
		        
	            
		        //JOptionPane.showMessageDialog(null, txtUsername.getText());
				/*
				btnJoinPublicRoom.setEnabled(true);
				btnJoinPrivateRoom.setEnabled(true);
				btnCreatePrivateRoom.setEnabled(true);
				btnDeletePrivateRoom.setEnabled(true);
				btnConnectMeTo.setEnabled(false);
				*/
			}
		});
		
		btnJoinPublicRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionPanel.setVisible(false);
				chatPanel.setVisible(true);
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
}
