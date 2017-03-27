package com.networking.semesterProject;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.sql.*;

public class LoginWindow {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setBounds(166, 65, 114, 19);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setBounds(166, 143, 114, 19);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setLabelFor(textField);
		lblUsername.setBounds(67, 41, 103, 15);
		frame.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(67, 112, 103, 15);
		frame.getContentPane().add(lblPassword);

		final JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ClientWindow clientWindow = new ClientWindow();
				clientWindow.Show();
			}
		});
		btnNewButton.setBounds(180, 202, 81, 25);
		frame.getContentPane().add(btnNewButton);

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
			public void actionPerformed(ActionEvent e) {

				AdminDialog dialog = new AdminDialog();

				Boolean isAdmin = dialog.showDialog();

				if (isAdmin) {
					btnNewButton.setText("Start Server");
				}
			}

		});
		mnHelp.add(mntmAdmin);
	}

	public void test() {

	}
}
