package com.networking.semesterProject.Client;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.networking.semesterProject.User;

import java.awt.Color;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class UserPanel extends JPanel {
	private JTable table;

	/**
	 * Create the panel.
	 */

	public class ExtendedTableModel extends DefaultTableModel {

		public ExtendedTableModel() {
			super(new Object[][] {}, new Object[] { "Send To", /*"Logged In",*/ "Username", "Full Name" });
		}

		Class[] columnTypes = new Class[] { Boolean.class, /*Boolean.class,*/ String.class, String.class };

		public Class getColumnClass(int columnIndex) {
			return columnTypes[columnIndex];
		}

		boolean[] columnEditables = new boolean[] { true, /*false,*/ false, false };

		public boolean isCellEditable(int row, int column) {
			return columnEditables[column];
		}

		// boolean[] isNeeded = new boolean[] { true, true, true, true, false,
		// false, true };
	}
	
	void UpdateUserList(Map<Integer, User> destination)
	{
		model.setRowCount(0);
		

		if(destination != null && !destination.isEmpty())
		for(User user : destination.values())
		{
			model.addRow(new Object[]{true, /*user.loggedIn,*/ user.userName, user.firstName + " " + user.lastName});
		}
	}
	
	private ExtendedTableModel model;

	public UserPanel() {
		setLayout(new BorderLayout(0, 0));

		model = new ExtendedTableModel();

		/*
		 * model.addColumn("Task"); model.addColumn("Assigned");
		 * model.addColumn("Completed"); model.addColumn("Description");
		 */

		table = new JTable();
		table.setFillsViewportHeight(true);
		// scrollPane.setViewportView(table);

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);

		table.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		table.setModel(model);
	}

}
