import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ChatClientGui extends JFrame {

	private JPanel contentPane;
	private JTextField txtPrivateroomPasscode;
	private JTextField txtPasscode;
	private JTextField txtMaxmenber;
	private JTextField txtEnterPasscode;
	private JTextField txtUsername;
	private JTextField txtSeverIp;
	private JTextField txtPort;
	private JButton btnConnectMeTo;
	private JButton btnJoinPublicRoom;
	private JButton btnJoinPrivateRoom;
	private JButton btnCreatePrivateRoom;
	private JButton btnDeletePrivateRoom;

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
					ChatClientGui frame = new ChatClientGui();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChatClientGui() 
	{
		setTitle("MyChat");
		initComponets();
		createEvents();	
		
	}
	
	private void initComponets() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 325);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnJoinPublicRoom = new JButton("Join Public Room");
		btnJoinPublicRoom.setEnabled(false);
		
		
		JLabel lblOptions = new JLabel("Options:");
		
		txtPrivateroomPasscode = new JTextField();
		txtPrivateroomPasscode.setText("Enter PrivateRoom Passcode...");
		txtPrivateroomPasscode.setColumns(10);
		
		btnJoinPrivateRoom = new JButton("Join Private Room");
		btnJoinPrivateRoom.setEnabled(false);
		
		txtPasscode = new JTextField();
		txtPasscode.setText("Enter Passcode");
		txtPasscode.setColumns(10);
		
		JSeparator separator = new JSeparator();
		
		txtMaxmenber = new JTextField();
		txtMaxmenber.setText("MaxMember");
		txtMaxmenber.setColumns(10);
		
		btnCreatePrivateRoom = new JButton("Create Private Room");
		btnCreatePrivateRoom.setEnabled(false);
		
		txtEnterPasscode = new JTextField();
		txtEnterPasscode.setText("Enter PassCode");
		txtEnterPasscode.setColumns(10);
		
		btnDeletePrivateRoom = new JButton("Delete Private Room");
		btnDeletePrivateRoom.setEnabled(false);
		
		JLabel lblGetConnectedFirst = new JLabel("Get Connected First");
		
		txtUsername = new JTextField();
		txtUsername.setText("Username");
		txtUsername.setColumns(10);
		
		txtSeverIp = new JTextField();
		txtSeverIp.setText("Sever IP");
		txtSeverIp.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setText("Port");
		txtPort.setColumns(10);
		
		btnConnectMeTo = new JButton("Connect Me to Server!");
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(408))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(txtPrivateroomPasscode, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(btnJoinPrivateRoom, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(2)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(btnConnectMeTo, GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(txtUsername, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(txtSeverIp, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(txtPort, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
										.addComponent(btnJoinPublicRoom, GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(6)
											.addComponent(lblOptions))
										.addComponent(lblGetConnectedFirst))))
							.addGap(41))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(txtEnterPasscode, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtPasscode, GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
									.addGap(12)
									.addComponent(txtMaxmenber, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnDeletePrivateRoom, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
								.addComponent(btnCreatePrivateRoom, GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
							.addGap(40)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblGetConnectedFirst)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtUsername)
						.addComponent(txtSeverIp)
						.addComponent(txtPort))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnConnectMeTo, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
					.addGap(12)
					.addComponent(lblOptions)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnJoinPublicRoom, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
					.addGap(6)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
							.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
							.addGap(60))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(12)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtPrivateroomPasscode)
								.addComponent(btnJoinPrivateRoom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnCreatePrivateRoom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(txtMaxmenber)
								.addComponent(txtPasscode))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtEnterPasscode)
								.addComponent(btnDeletePrivateRoom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(4)))
					.addGap(38))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	private void createEvents() {
		btnConnectMeTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//new Test();	
				btnJoinPublicRoom.setEnabled(true);
				btnJoinPrivateRoom.setEnabled(true);
				btnCreatePrivateRoom.setEnabled(true);
				btnDeletePrivateRoom.setEnabled(true);	
				btnConnectMeTo.setEnabled(false);	
			}
		});
		
		btnJoinPublicRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChatWindow();	
				btnJoinPublicRoom.setEnabled(false);
				btnJoinPrivateRoom.setEnabled(false);
				btnCreatePrivateRoom.setEnabled(false);
				btnDeletePrivateRoom.setEnabled(false);			
			}
		});
		
	}
}
