package com.networking.semesterProject;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.AbstractMap;
import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.CardLayout;
import javax.swing.JPanel;
import com.networking.semesterProject.Client.LoginHelper;
import com.networking.semesterProject.Client.TaskWindow;
import com.networking.semesterProject.Client.ClientInterface;
import com.networking.semesterProject.Client.ClientWindow;
import com.networking.semesterProject.Server.ServerHelper;
import com.networking.semesterProject.Server.ServerInterface;
import com.networking.semesterProject.Server.UserManager;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class LoginWindow {
	
	private JFrame frmLoginWindow;
	private JTextField textField;
	private JTextField textField_1;
	private ServerHelper serverHelper;
	private JTextField clientServerIP;
	private JTextField clientServerPort;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				try {	
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					
				} catch (Exception e) {
					e.printStackTrace();
					
					new ErrorDialog("You Don't Have MySQL!").showDialog();
					
					minimumMode = true;
				}
				
					LoginWindow window = new LoginWindow();
					window.frmLoginWindow.setVisible(true);
				
			}
		});
	}
	
	public static String serverIP;
	public static Integer serverPort;
	
	
	private JTextField adminServerPort;

	/**
	 * Create the application.
	 * 
	 */
	
	public static boolean minimumMode = false;
	
	public LoginWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLoginWindow = new JFrame();
		frmLoginWindow.setTitle("Login Window");
		frmLoginWindow.setBounds(100, 100, 548, 411);
		frmLoginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final CardLayout cardLayout = new CardLayout(0, 0);
		frmLoginWindow.getContentPane().setLayout(cardLayout);
		
		JPanel panel = new JPanel();
		frmLoginWindow.getContentPane().add(panel, "name_238221794140412");
												GridBagLayout gbl_panel = new GridBagLayout();
												gbl_panel.columnWidths = new int[] {61, 70, 137, 92};
												gbl_panel.rowHeights = new int[]{45, 0, 0, 89, 0, 0, 0, 0, 0, 0};
												gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0};
												gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
												panel.setLayout(gbl_panel);
																
																		JLabel lblUsername = new JLabel("Username:");
																		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
																		gbc_lblUsername.anchor = GridBagConstraints.WEST;
																		gbc_lblUsername.fill = GridBagConstraints.VERTICAL;
																		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
																		gbc_lblUsername.gridx = 1;
																		gbc_lblUsername.gridy = 1;
																		panel.add(lblUsername, gbc_lblUsername);
																
																		textField_1 = new JTextField();
																		textField_1.setColumns(10);
																		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
																		gbc_textField_1.fill = GridBagConstraints.BOTH;
																		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
																		gbc_textField_1.gridx = 3;
																		gbc_textField_1.gridy = 1;
																		panel.add(textField_1, gbc_textField_1);
														
																JLabel lblPassword = new JLabel("Password:");
																GridBagConstraints gbc_lblPassword = new GridBagConstraints();
																gbc_lblPassword.anchor = GridBagConstraints.WEST;
																gbc_lblPassword.fill = GridBagConstraints.VERTICAL;
																gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
																gbc_lblPassword.gridx = 1;
																gbc_lblPassword.gridy = 2;
																panel.add(lblPassword, gbc_lblPassword);
																								lblUsername.setLabelFor(textField);
																								
																										textField = new JTextField();
																										textField.setColumns(10);
																										GridBagConstraints gbc_textField = new GridBagConstraints();
																										gbc_textField.fill = GridBagConstraints.BOTH;
																										gbc_textField.insets = new Insets(0, 0, 5, 5);
																										gbc_textField.gridx = 3;
																										gbc_textField.gridy = 2;
																										panel.add(textField, gbc_textField);
																								
																								JLabel lblServerIp = new JLabel("Server IP:");
																								GridBagConstraints gbc_lblServerIp = new GridBagConstraints();
																								gbc_lblServerIp.anchor = GridBagConstraints.WEST;
																								gbc_lblServerIp.insets = new Insets(0, 0, 5, 5);
																								gbc_lblServerIp.gridx = 1;
																								gbc_lblServerIp.gridy = 4;
																								panel.add(lblServerIp, gbc_lblServerIp);
																								
																								clientServerIP = new JTextField();
																								clientServerIP.setText("127.0.0.1");
																								clientServerIP.setColumns(10);
																								GridBagConstraints gbc_clientServerIP = new GridBagConstraints();
																								gbc_clientServerIP.insets = new Insets(0, 0, 5, 5);
																								gbc_clientServerIP.fill = GridBagConstraints.HORIZONTAL;
																								gbc_clientServerIP.gridx = 3;
																								gbc_clientServerIP.gridy = 4;
																								panel.add(clientServerIP, gbc_clientServerIP);
																								
																								JLabel lblServerPort = new JLabel("Server Port:");
																								GridBagConstraints gbc_lblServerPort = new GridBagConstraints();
																								gbc_lblServerPort.anchor = GridBagConstraints.WEST;
																								gbc_lblServerPort.insets = new Insets(0, 0, 5, 5);
																								gbc_lblServerPort.gridx = 1;
																								gbc_lblServerPort.gridy = 5;
																								panel.add(lblServerPort, gbc_lblServerPort);
																								
																								clientServerPort = new JTextField();
																								clientServerPort.setText("9000");
																								clientServerPort.setColumns(10);
																								GridBagConstraints gbc_clientServerPort = new GridBagConstraints();
																								gbc_clientServerPort.insets = new Insets(0, 0, 5, 5);
																								gbc_clientServerPort.fill = GridBagConstraints.HORIZONTAL;
																								gbc_clientServerPort.gridx = 3;
																								gbc_clientServerPort.gridy = 5;
																								panel.add(clientServerPort, gbc_clientServerPort);
																						
																								final JButton loginButton = new JButton("Login");
																								GridBagConstraints gbc_loginButton = new GridBagConstraints();
																								gbc_loginButton.fill = GridBagConstraints.VERTICAL;
																								gbc_loginButton.insets = new Insets(0, 0, 5, 0);
																								gbc_loginButton.gridwidth = 5;
																								gbc_loginButton.gridx = 0;
																								gbc_loginButton.gridy = 7;
																								panel.add(loginButton, gbc_loginButton);
																								
																								
																								
														loginButton.addActionListener(new ActionListener() {
															@Override
															public void actionPerformed(ActionEvent arg0) {
																
																loginButton.setEnabled(false);
																
																serverIP = clientServerIP.getText();
																serverPort = Integer.parseInt(clientServerPort.getText());

																LoginHelper clientHelper = new LoginHelper(new ClientInterface(){

																	Boolean isFailed = false;
																	@Override
																	public void OnConnected(Socket clientSocket, Message message) {
																		// TODO Auto-generated method stub
																		
																		ClientWindow clientWindow = new ClientWindow(frmLoginWindow, clientSocket, message);
																		clientWindow.setVisible(true);
																		
																		//if(!minimumMode)
																		//{
																		TaskWindow taskWindow = new TaskWindow();
																	       
																		taskWindow.setLocation(clientWindow.getX() + clientWindow.getWidth(), clientWindow.getY());
																		taskWindow.setVisible(true);
																		
																		clientWindow.addWindowListener(new WindowAdapter() {
																			
																			@Override
																            public void windowClosing(WindowEvent evt){
																				taskWindow.dispatchEvent(new WindowEvent(taskWindow, WindowEvent.WINDOW_CLOSING));
																			}
																		});
																		//}
																		

																        
																		
																		LoginWindow.this.frmLoginWindow.setVisible(false);
																		loginButton.setEnabled(true);
																	}

																	@Override
																	public void OnFailed() {
																		// TODO Auto-generated method stub
																		ErrorDialog dialog = new ErrorDialog("Couldn't Connect To Server...");

																		dialog.showDialog();
																		
																		loginButton.setEnabled(true);
																		
																		isFailed = true;
																	}

																	@Override
																	public void OnDisconnected(Message message) {
																		// TODO Auto-generated method stub
																		
																		if(!isFailed)
																		{
																			ErrorDialog dialog = new ErrorDialog(message.message);
	
																			dialog.showDialog();
																			
																			loginButton.setEnabled(true);
																		}
																	}}, new AbstractMap.SimpleEntry<String, String>(textField_1.getText(), textField.getText()));	
																
																clientHelper.Start();
															}
														});
														
														
												frmLoginWindow.getRootPane().setDefaultButton(loginButton);
												
												JPanel panel_1 = new JPanel();
												frmLoginWindow.getContentPane().add(panel_1, "name_238616311173393");
												GridBagLayout gbl_panel_1 = new GridBagLayout();
												gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
												gbl_panel_1.rowHeights = new int[]{80, 0, 190, 0, 0, 0};
												gbl_panel_1.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
												gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
												panel_1.setLayout(gbl_panel_1);
												
												final JButton stopServerButton = new JButton("Stop Server");
												final JButton startServerButton = new JButton("Start Server");
												startServerButton.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent arg0) {
																							
														serverHelper = new ServerHelper();
														
														startServerButton.setEnabled(false);
														
														serverPort = Integer.parseInt(adminServerPort.getText());
														
														serverHelper.Start(new ServerInterface(){

															@Override
															public void OnStopped() {
																// TODO Auto-generated method stub
																//startServerButton.setEnabled(true);
																ErrorDialog dialog = new ErrorDialog("Couldn't Start Server...");

																dialog.showDialog();
															}});
														
														stopServerButton.setEnabled(true);
													}
												});
												
												JLabel lblServerPort_1 = new JLabel("Server Port:");
												GridBagConstraints gbc_lblServerPort_1 = new GridBagConstraints();
												gbc_lblServerPort_1.anchor = GridBagConstraints.EAST;
												gbc_lblServerPort_1.insets = new Insets(0, 0, 5, 5);
												gbc_lblServerPort_1.gridx = 1;
												gbc_lblServerPort_1.gridy = 1;
												panel_1.add(lblServerPort_1, gbc_lblServerPort_1);
												
												adminServerPort = new JTextField();
												adminServerPort.setText("9000");
												GridBagConstraints gbc_adminServerPort = new GridBagConstraints();
												gbc_adminServerPort.fill = GridBagConstraints.HORIZONTAL;
												gbc_adminServerPort.insets = new Insets(0, 0, 5, 5);
												gbc_adminServerPort.gridx = 3;
												gbc_adminServerPort.gridy = 1;
												panel_1.add(adminServerPort, gbc_adminServerPort);
												adminServerPort.setColumns(10);
												GridBagConstraints gbc_startServerButton = new GridBagConstraints();
												gbc_startServerButton.anchor = GridBagConstraints.WEST;
												gbc_startServerButton.fill = GridBagConstraints.VERTICAL;
												gbc_startServerButton.insets = new Insets(0, 0, 5, 5);
												gbc_startServerButton.gridx = 2;
												gbc_startServerButton.gridy = 3;
												panel_1.add(startServerButton, gbc_startServerButton);
												
												
												stopServerButton.setEnabled(false);
												stopServerButton.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent arg0) {
														
														try {
															serverHelper.Stop(new ServerInterface(){

																@Override
																public void OnStopped() {
																	// TODO Auto-generated method stub
																	startServerButton.setEnabled(true);
																}});
														} catch (IOException e) {
															// TODO Auto-generated catch block
															System.out.println("Already Stopped");
														}
														stopServerButton.setEnabled(false);
													}
												});
												GridBagConstraints gbc_stopServerButton = new GridBagConstraints();
												gbc_stopServerButton.fill = GridBagConstraints.BOTH;
												gbc_stopServerButton.insets = new Insets(0, 0, 5, 5);
												gbc_stopServerButton.gridx = 3;
												gbc_stopServerButton.gridy = 3;
												panel_1.add(stopServerButton, gbc_stopServerButton);

		JMenuBar menuBar = new JMenuBar();
		frmLoginWindow.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		final JMenuItem mntmManageUsers = new JMenuItem("Manage Users");
		mntmManageUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				UserManager userManager = new UserManager(serverHelper);
				userManager.Show();
			}
		});
		
		mntmManageUsers.setVisible(false);
		mnFile.add(mntmManageUsers);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmLoginWindow.dispatchEvent(new WindowEvent(frmLoginWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		mnFile.add(mntmClose);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		// Code Starts here
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////

		JMenuItem mntmAdmin = new JMenuItem("Admin"); // Checks to see if user
														// is admin
		mntmAdmin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				AdminDialog dialog = new AdminDialog();

				Boolean isAdmin = dialog.showDialog();

				if (isAdmin) {
						
						//btnNewButton.setText("Start Server");
						cardLayout.next(frmLoginWindow.getContentPane());
						mntmManageUsers.setVisible(true);

				}
			}

		});
		mnHelp.add(mntmAdmin);
	}
}
