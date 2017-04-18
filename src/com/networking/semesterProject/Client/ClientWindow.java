package com.networking.semesterProject.Client;

import java.awt.EventQueue;
import java.net.Socket;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Dialog.ModalityType;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.networking.semesterProject.Message.Type;

import com.networking.semesterProject.*;


import java.awt.BorderLayout;

import java.util.*;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JSplitPane;


public class ClientWindow {

	JFrame frame;

	/**
	 * Create the application.
	 */
	
	//private Socket clientSocket = null;
	
	JFrame parentFrame;
	
	public ClientWindow(JFrame parentFrame, Socket clien, Message message) {
		this.parentFrame = parentFrame;
		//clientSocket = clien;
		initialize(clien, message);
		
		
		
		ClientHelper clientHelper = new ClientHelper(new ExtendedClientInterface(){

			@Override
			public void OnConnected(Socket clientSocket, Message message) {

				chatPanel.OnConnected(message);
			}

			@Override
			public void OnFailed() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnDisconnected(Message message) {
				// TODO Auto-generated method stub
	
				chatPanel.OnDisconnected(message);
			}

			@Override
			public void OnSchedule(Scheduler schedule) {
				// TODO Auto-generated method stub
				SchedulerDialog dialog;
				try {
					dialog = new SchedulerDialog(schedule);
					dialog.setVisible(true);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void OnUserList(Map<Integer, User> userList) {
				// TODO Auto-generated method stub
				userPanel.UpdateUserList(userList);
			}}, clien);
		
		clientHelper.Start();
		
		userPanel.UpdateUserList(message.destination);
	}
	
	public void Show() {
		frame.setVisible(true);
	}
	

	private ChatPanel chatPanel;
	private UserPanel userPanel;
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Socket clientSocket, Message message) {
		frame = new JFrame();
		frame.setTitle("Welcome, " + message.source.userName + "!");
		frame.setBounds(100, 100, 698, 393);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
            public void windowClosing(WindowEvent evt){
                //formLoginWindowClosed(evt);
				
				try {
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				parentFrame.setVisible(true);
            }
        });
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.75);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		chatPanel = new ChatPanel(this, clientSocket, message);
		splitPane.setLeftComponent(chatPanel);
		
		//JPanel panel = new JPanel();
		userPanel = new UserPanel();
		splitPane.setRightComponent(userPanel);

		JMenuBar menuBar = new JMenuBar();
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		JMenuItem getSchedule = new JMenuItem("Get Schedule");
		getSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Message msg = new Message(Type.Schedule, new User(message.source.id), null, null, null);	
				
				MessageHelper messageHelper = new MessageHelper();
				messageHelper.Start(clientSocket, msg);
			}
		});
		menuFile.add(getSchedule);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		menuFile.add(mntmClose);
		
	}

}
