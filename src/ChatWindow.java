import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.UIManager;

public class ChatWindow extends JFrame {

	private JPanel contentPane;
	private JButton btnSendMessage;
	private JButton btnLeavechatroom;
	private JTextArea txtrShowchatmessge;
	private JTextArea txtrInputmessage;

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
					ChatWindow frame = new ChatWindow();
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
	public ChatWindow() {
		setTitle("ChatWindow");			
		initComponets();
		createEvents();			
	}

	private void initComponets() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 624, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblWelcomeXxxYou = new JLabel("Welcome, XXX. You are now in Public Chat Room");
		
		txtrShowchatmessge = new JTextArea();
		txtrShowchatmessge.setText("ShowChatMessge");
		
		txtrInputmessage = new JTextArea();
		txtrInputmessage.setText("InputMessage");
		
		btnSendMessage = new JButton("SendMessage");
		
		btnLeavechatroom = new JButton("Leave ChatRoom");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(txtrShowchatmessge, GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(txtrInputmessage, GroupLayout.PREFERRED_SIZE, 468, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(btnSendMessage))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblWelcomeXxxYou, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
									.addGap(205)
									.addComponent(btnLeavechatroom, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
									.addGap(17)))
							.addGap(0))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWelcomeXxxYou, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
						.addComponent(btnLeavechatroom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtrShowchatmessge, GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSendMessage, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
						.addComponent(txtrInputmessage, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		setVisible(true);
		
	}
	
	private void createEvents() {
		// TODO Auto-generated method stub
		
	}
}
