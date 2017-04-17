package com.networking.semesterProject.Client;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import com.networking.semesterProject.Message;
import com.networking.semesterProject.Message.Type;

import java.awt.Adjustable;
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
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

public class ChatPanel extends JPanel {
	private JTextField textField;
	private JButton btnNewButton;
	private JScrollPane scrollPane;
	/**
	 * Create the panel.
	 */
	//CHANGE TEXT AREA TO TABLE, SO THAT WE CAN SORT BY TIME, AND DYNAMICALLY ADD ITEMS TO TOP OF IT! 
	private Socket clientSocket;
	private JTable table;
	private DefaultTableModel model;
	
	public ChatPanel(Socket clien, Message message)
	{
		initialize(message);
		//}
		
		clientSocket = clien;
		
		ClientHelper clientHelper = new ClientHelper(new ClientInterface(){

			@Override
			public void OnConnected(Socket clientSocket, Message message) {
				// TODO Auto-generated method stub
				//textArea.append(message.message);		
				
				ZoneId zoneId = ZoneId.of("America/New_York");
				ZonedDateTime zdt = ZonedDateTime.ofInstant(message.timestamp, zoneId);

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm");
				
				model.addRow(new Object[]{message.source.userName, message.message, zdt.format(formatter)});
				
				ScrollToBottom();	
			}

			@Override
			public void OnFailed() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnDisconnected(Message message) {
				// TODO Auto-generated method stub
	
				model.setRowCount(0);
				
				//textArea.setText(message);

				model.addRow(new Object[]{"Disconnected With Message:", message.message, null});
				
				ScrollToBottom();
				
				btnNewButton.setEnabled(false);
			}}, clien);
		
		clientHelper.Start();
	}
	
	private void ScrollToBottom()
	{
		JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
	    AdjustmentListener downScroller = new AdjustmentListener() {
	        @Override
	        public void adjustmentValueChanged(AdjustmentEvent e) {
	            Adjustable adjustable = e.getAdjustable();
	            adjustable.setValue(adjustable.getMaximum());
	            verticalBar.removeAdjustmentListener(this);
	        }
	    };
	    verticalBar.addAdjustmentListener(downScroller);
	}
	
	public void initialize(final Message message) {
		setLayout(new BorderLayout(0, 0));
		
		model = new DefaultTableModel();
		
		model.addColumn("From");
		model.addColumn("Message");
		model.addColumn("When");
		
		model.addRow(new Object[]{"Server Says:", message.message, null});
		
		
		JPanel newPanel = new JPanel();
		newPanel.setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		
		newPanel.add(table);
		
		scrollPane = new JScrollPane(newPanel);
		//pane.add(table);
		
		add(scrollPane, BorderLayout.CENTER);
		
		table.setShowVerticalLines(false);
		table.setModel(model);
		
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
				
				//if(message.type == Type.Init)
				//{
					String gotMessage = textField.getText();
					
					Instant instant = Instant.now();
					
					ZoneId zoneId = ZoneId.of( "America/New_York" );
					ZonedDateTime zdt = ZonedDateTime.ofInstant( instant , zoneId );
									
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm");

					//textArea.append(message.source.userName + " (" + zdt.format(formatter) + "): " + gotMessage + "\n");	
					
					model.addRow(new Object[]{message.source.userName, gotMessage, zdt.format(formatter)});
					
					ScrollToBottom();
					
					MessageHelper messageHelper = new MessageHelper();
					messageHelper.Start(clientSocket, new Message(Type.Send, message.source, null, gotMessage, instant));
					
					
					textField.setText("");
				//}
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 0;
		panel.add(btnNewButton, gbc_btnNewButton);

	}

}
