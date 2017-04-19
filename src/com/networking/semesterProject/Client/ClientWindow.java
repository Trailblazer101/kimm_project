package com.networking.semesterProject.Client;

import java.awt.EventQueue;
import java.net.Socket;
import java.sql.SQLException;
import java.time.Instant;
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


public class ClientWindow extends JFrame {

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
			public void OnUserUpdate(Map<Integer, User> userList) {
				// TODO Auto-generated method stub
				userPanel.UpdateUserList(userList);
			}}, clien);
		
		clientHelper.Start();
		
		userPanel.UpdateUserList(message.destination);
	}
	
	/*public void Show() {
		setVisible(true);
	}*/
	

	private ChatPanel chatPanel;
	private UserPanel userPanel;
	
	public interface ChatInterface
	{
		public void OnSubmitted(String message, Instant instant);
	}
	
	private JPanel contentPane;
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Socket clientSocket, Message message) {
		
		setTitle("Welcome, " + message.source.userName + "!");
		setBounds(100, 100, 775, 524);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		contentPane = new JPanel();
		
		setContentPane(contentPane);
		
		addWindowListener(new WindowAdapter() {
			
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
		contentPane.setLayout(new BorderLayout(0, 0));
		
				JMenuBar menuBar = new JMenuBar();
				contentPane.add(menuBar, BorderLayout.NORTH);
				
				JMenu menuFile = new JMenu("File");
				menuBar.add(menuFile);
				
				JMenuItem getSchedule = new JMenuItem("Get Schedule");
				getSchedule.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {

						Message msg = new Message(Message.Type.Schedule, new User(message.source.id), null, null, null);	
						
						MessageHelper messageHelper = new MessageHelper();
						messageHelper.Start(clientSocket, msg);
					}
				});
				
				JMenuItem mntmTasks = new JMenuItem("Tasks");
				mntmTasks.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						
						//if(!LoginWindow.minimumMode)
						//{
							TaskWindow taskWindow = new TaskWindow();
						       
							taskWindow.setLocation(getX() + getWidth(), getY());
							taskWindow.setVisible(true);
							
							addWindowListener(new WindowAdapter() {
								
								@Override
					            public void windowClosing(WindowEvent evt){
									taskWindow.dispatchEvent(new WindowEvent(taskWindow, WindowEvent.WINDOW_CLOSING));
								}
							});
						//}
						
						
					}
				});
				menuFile.add(mntmTasks);
				menuFile.add(getSchedule);
				
				JMenuItem mntmClose = new JMenuItem("Close");
				mntmClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispatchEvent(new WindowEvent(ClientWindow.this, WindowEvent.WINDOW_CLOSING));
					}
				});
				menuFile.add(mntmClose);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.75);
		contentPane.add(splitPane);
		chatPanel = new ChatPanel(this, new ChatInterface(){

			@Override
			public void OnSubmitted(String gotMessage, Instant instant) {

				//
				
				
				
				MessageHelper messageHelper = new MessageHelper();
				messageHelper.Start(clientSocket, new Message(Message.Type.Send, message.source, userPanel.GetSelectedUsers(), gotMessage, instant));
				
			}
		}, clientSocket, message);
		splitPane.setLeftComponent(chatPanel);
		
		//JPanel panel = new JPanel();
		userPanel = new UserPanel(message.source);
		splitPane.setRightComponent(userPanel);
		
	
	}

}
