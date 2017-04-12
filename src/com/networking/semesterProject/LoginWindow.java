package com.networking.semesterProject;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.CardLayout;
import javax.swing.JPanel;
import com.networking.semesterProject.Client.ClientHelper;
import com.networking.semesterProject.Client.ClientInterface;
import com.networking.semesterProject.Client.ClientWindow;
import com.networking.semesterProject.Server.ServerHelper;
import com.networking.semesterProject.Server.ServerInterface;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class LoginWindow {
	
	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private ServerHelper serverHelper;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					LoginWindow window = new LoginWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 */
	public LoginWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 460, 367);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final CardLayout cardLayout = new CardLayout(0, 0);
		frame.getContentPane().setLayout(cardLayout);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, "name_238221794140412");
												GridBagLayout gbl_panel = new GridBagLayout();
												gbl_panel.columnWidths = new int[] {61, 70, 0, 92};
												gbl_panel.rowHeights = new int[]{45, 0, 0, 0, 0, 0, 0};
												gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0};
												gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
												panel.setLayout(gbl_panel);
																
																		JLabel lblUsername = new JLabel("Username:");
																		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
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
																						
																								final JButton loginButton = new JButton("Login");
																								GridBagConstraints gbc_loginButton = new GridBagConstraints();
																								gbc_loginButton.fill = GridBagConstraints.VERTICAL;
																								gbc_loginButton.insets = new Insets(0, 0, 5, 0);
																								gbc_loginButton.gridwidth = 5;
																								gbc_loginButton.gridx = 0;
																								gbc_loginButton.gridy = 4;
																								panel.add(loginButton, gbc_loginButton);
																								
																								
																								
														loginButton.addActionListener(new ActionListener() {
															@Override
															public void actionPerformed(ActionEvent arg0) {
																
																loginButton.setEnabled(false);

																ClientHelper clientHelper = new ClientHelper(new ClientInterface(){

																	Boolean isFailed = false;
																	@Override
																	public void OnConnected(Socket clientSocket, Message message) {
																		// TODO Auto-generated method stub
																		ClientWindow clientWindow = new ClientWindow(clientSocket, message);
																		clientWindow.Show();
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
																	public void OnDisconnected() {
																		// TODO Auto-generated method stub
																		
																		if(!isFailed)
																		{
																			ErrorDialog dialog = new ErrorDialog("Disconnected From Server...");
	
																			dialog.showDialog();
																			
																			loginButton.setEnabled(true);
																		}
																	}});	
																
																clientHelper.Start();
															}
														});
												
												JPanel panel_1 = new JPanel();
												frame.getContentPane().add(panel_1, "name_238616311173393");
												GridBagLayout gbl_panel_1 = new GridBagLayout();
												gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0, 0};
												gbl_panel_1.rowHeights = new int[]{190, 0, 0, 0};
												gbl_panel_1.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
												gbl_panel_1.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
												panel_1.setLayout(gbl_panel_1);
												
												final JButton stopServerButton = new JButton("Stop Server");
												final JButton startServerButton = new JButton("Start Server");
												startServerButton.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent arg0) {
																							
														serverHelper = new ServerHelper();
														
														startServerButton.setEnabled(false);
														
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
												GridBagConstraints gbc_startServerButton = new GridBagConstraints();
												gbc_startServerButton.fill = GridBagConstraints.VERTICAL;
												gbc_startServerButton.insets = new Insets(0, 0, 5, 5);
												gbc_startServerButton.gridx = 1;
												gbc_startServerButton.gridy = 1;
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
												gbc_stopServerButton.fill = GridBagConstraints.VERTICAL;
												gbc_stopServerButton.insets = new Insets(0, 0, 5, 5);
												gbc_stopServerButton.gridx = 2;
												gbc_stopServerButton.gridy = 1;
												panel_1.add(stopServerButton, gbc_stopServerButton);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

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
					cardLayout.next(frame.getContentPane());
				}
			}

		});
		mnHelp.add(mntmAdmin);
	}
}
