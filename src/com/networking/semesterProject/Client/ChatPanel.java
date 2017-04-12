package com.networking.semesterProject.Client;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.networking.semesterProject.Message;
import com.networking.semesterProject.Message.Type;

import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.io.*;
import java.net.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChatPanel extends JPanel {
	private JTextField textField;
	private JTextArea textArea;
	/**
	 * Create the panel.
	 */
	
	private Socket clientSocket;
	
	public ChatPanel(Socket clien, Message message)
	{
		initialize(message);
		
		if(message.type == Type.Init)
		{
			textArea.append(message.message + "\n");
		}
		
		clientSocket = clien;
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try {
					
					//BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					
					
					while(!clientSocket.isClosed())
					{
						ObjectInputStream inFromServer = new
								ObjectInputStream(clientSocket.getInputStream());
						
						//BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

						Message message = (Message)inFromServer.readObject();
						
						if(message.type == Type.Send)
						{
							ZoneId zoneId = ZoneId.of( "America/New_York" );
							ZonedDateTime zdt = ZonedDateTime.ofInstant( message.timestamp , zoneId );
											
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "uuuu.MM.dd.HH.mm.ss" );

							textArea.append(zdt.format(formatter) + ": " + message.message + "\n");	
						}
						else if(message.type == Type.Disconnect)
						{
							textArea.setText("Disconnected With Message:\n\t" + message.message + "\n");
							clientSocket.close();
						}
						
						//inFromServer.close();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}).start();
	}
	
	public void initialize(final Message message) {
		setLayout(new BorderLayout(0, 0));
		
		
		textArea = new JTextArea();
		add(textArea, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{188, 73, 0};
		gbl_panel.rowHeights = new int[]{25, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 0;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(message.type == Type.Init)
				{
					String gotMessage = textField.getText();
					
					Instant instant = Instant.now();
					
					ZoneId zoneId = ZoneId.of( "America/New_York" );
					ZonedDateTime zdt = ZonedDateTime.ofInstant( instant , zoneId );
									
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "uuuu.MM.dd.HH.mm.ss" );

					textArea.append(zdt.format(formatter) + ": " + gotMessage + "\n");	
							
					MessageHelper messageHelper = new MessageHelper();
					messageHelper.Start(clientSocket, new Message(Type.Send, message.source, null, gotMessage, instant));
				}
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 0;
		panel.add(btnNewButton, gbc_btnNewButton);

	}

}
