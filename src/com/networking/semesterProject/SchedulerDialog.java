package com.networking.semesterProject;

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
import java.sql.SQLException;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;

public class SchedulerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();


	/**
	 * Create the dialog.
	 */
	public SchedulerDialog(Scheduler msgObject)throws SQLException {
		setTitle("Schedule");
	
		
		
		List<String> workWeek = msgObject.getSchedule();
		
		Integer workCount = 0;
		
		if(workWeek != null)
			workCount = workWeek.size();
			
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JLabel label = new JLabel("Monday: " + ((workCount > 0) ? workWeek.get(0) : "NOT SET"));
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("Tuesday: "+ ((workCount > 1) ? workWeek.get(1) : "NOT SET"));
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("Wednesday: "+ ((workCount > 2) ? workWeek.get(2) : "NOT SET"));
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("Thursday: "+ ((workCount > 3) ? workWeek.get(3) : "NOT SET"));
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("Friday: "+ ((workCount > 4) ? workWeek.get(4) : "NOT SET"));
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("Saturday: "+ ((workCount > 5) ? workWeek.get(5) : "NOT SET"));
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("Sunday: " + ((workCount > 6) ? workWeek.get(6) : "NOT SET"));
			contentPanel.add(label);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						dispose();
						
					}
				});
				buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
