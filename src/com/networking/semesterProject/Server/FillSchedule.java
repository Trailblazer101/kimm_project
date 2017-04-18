package com.networking.semesterProject.Server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;

public class FillSchedule extends JDialog {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FillSchedule dialog = new FillSchedule();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public FillSchedule() {
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 0};
		gridBagLayout.rowHeights = new int[]{203, 62, 35, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		{
			JPanel panel = new JPanel();
			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.gridwidth = 2;
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			getContentPane().add(panel, gbc_panel);
			panel.setLayout(new GridLayout(0, 3, 0, 0));
			{
				JLabel label = new JLabel("Monday");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				textField = new JTextField();
				panel.add(textField);
				textField.setColumns(10);
			}
			{
				JLabel label = new JLabel("Tuesday");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				textField_1 = new JTextField();
				panel.add(textField_1);
				textField_1.setColumns(10);
			}
			{
				JLabel label = new JLabel("Wednesday");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				textField_2 = new JTextField();
				panel.add(textField_2);
				textField_2.setColumns(10);
			}
			{
				JLabel label = new JLabel("Thursday");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				textField_3 = new JTextField();
				panel.add(textField_3);
				textField_3.setColumns(10);
			}
			{
				JLabel label = new JLabel("Friday");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				textField_4 = new JTextField();
				panel.add(textField_4);
				textField_4.setColumns(10);
			}
			{
				JLabel label = new JLabel("Saturday");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				textField_5 = new JTextField();
				panel.add(textField_5);
				textField_5.setColumns(10);
			}
			{
				JLabel label = new JLabel("Sunday");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				textField_6 = new JTextField();
				panel.add(textField_6);
				textField_6.setColumns(10);
			}
			{
				JLabel lblUserId = new JLabel("User ID");
				panel.add(lblUserId);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				textField_7 = new JTextField();
				panel.add(textField_7);
				textField_7.setColumns(10);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.anchor = GridBagConstraints.NORTH;
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.gridx = 0;
			gbc_buttonPane.gridy = 2;
			getContentPane().add(buttonPane, gbc_buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						
						
						Connection conn = null;
						//PreparedStatement preparedStatement = null;
						Statement stmt = null;

						
							try {
								conn = DriverManager.getConnection(
										"jdbc:mysql://localhost/networkProject?user=root&password=tst12368&rewriteBatchedStatements=true");
								
								
								stmt =  conn.createStatement();

								
								String sql,sql2;
								
								int userID = Integer.parseInt(textField_7.getText());
								
								
								sql = "INSERT INTO Schedule (userID, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday) VALUES "; 
								
								sql2 =" ("+ userID +", "+textField.getText() +","+ textField_1.getText() +", "+textField_2.getText() +", "
										+textField_3.getText() +", "+textField_4.getText() +", "+textField_5.getText() +", "+textField_6.getText()+")";
								
								sql+=sql2;
								
								stmt.executeUpdate(sql);
								
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						
												
						
						
						
						
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
	
				
				
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
