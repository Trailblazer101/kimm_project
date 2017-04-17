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
	private JButton btnNewButton;
	/**
	 * Create the panel.
	 */
	//CHANGE TEXT AREA TO TABLE, SO THAT WE CAN SORT BY TIME, AND DYNAMICALLY ADD ITEMS TO TOP OF IT! 
	private Socket clientSocket;
	
	public ChatPanel(Socket clien, Message message)
	{
		initialize(message);
		
		if(message.type == Type.Init)
		{
			textArea.append(message.message + "\n");
		}
		
		clientSocket = clien;
		
		ClientHelper clientHelper = new ClientHelper(new ClientInterface(){

			@Override
			public void OnConnected(Socket clientSocket, Message message) {
				// TODO Auto-generated method stub
				textArea.append(message.message);
			}

			@Override
			public void OnFailed() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnDisconnected(String message) {
				// TODO Auto-generated method stub
				textArea.setText(message);
				
				btnNewButton.setEnabled(false);
			}}, clien);
		
		clientHelper.Start();
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
		
		btnNewButton = new JButton("Submit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(message.type == Type.Init)
				{
					String gotMessage = textField.getText();
					
					Instant instant = Instant.now();
					
					ZoneId zoneId = ZoneId.of( "America/New_York" );
					ZonedDateTime zdt = ZonedDateTime.ofInstant( instant , zoneId );
									
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm");

					//textArea.append(zdt.format(formatter) + ": " + gotMessage + "\n");	
					textArea.append(message.source.userName + " (" + zdt.format(formatter) + "): " + gotMessage + "\n");	
					
					MessageHelper messageHelper = new MessageHelper();
					messageHelper.Start(clientSocket, new Message(Type.Send, message.source, null, gotMessage, instant));
					
					
					textField.setText("");
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
