package com.networking.semesterProject.Client;

import java.awt.EventQueue;
import java.net.Socket;
import java.awt.Dialog.ModalityType;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.networking.semesterProject.Message;

import java.awt.BorderLayout;

public class ClientWindow {

	private JFrame frame;

	/**
	 * Create the application.
	 */
	
	//private Socket clientSocket = null;
	
	public ClientWindow(Socket clien, Message message) {
		
		//clientSocket = clien;
		initialize(clien, message);
	}
	
	public void Show() {
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Socket clientSocket, Message message) {
		frame = new JFrame();
		frame.setBounds(100, 100, 698, 393);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//JPanel panel = new JPanel();
		TaskPanel taskPanel = new TaskPanel();
		ChatPanel chatPanel = new ChatPanel(clientSocket, message);
		
		frame.getContentPane().add(taskPanel, BorderLayout.EAST);
		frame.getContentPane().add(chatPanel, BorderLayout.CENTER);
	}

}
