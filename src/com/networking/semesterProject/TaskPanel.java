package com.networking.semesterProject;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.Color;
import java.sql.*;

public class TaskPanel extends JPanel {
	private JTable table;

	/**
	 * Create the panel.
	 */
	public TaskPanel() {
		
		table = new JTable();
		table.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		add(table);
		
		DefaultTableModel model = new DefaultTableModel();
		table.setModel(model);
		
		model.addColumn("Task");
		model.addColumn("Assigned");
		model.addColumn("Completed");
		model.addColumn("Description");
		
		//model.addRow(new Object[]{"Row1", "Row2"});
		
		Connection conn = null;
		Statement statement = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=tst12368");
			statement = conn.createStatement();
			statement.executeUpdate("create database if not exists networkProject");
			
			statement.close();
			
			conn.setCatalog("networkProject");		
			
			statement = conn.createStatement();
			
			String createTaskTable = "CREATE TABLE if not exists TaskTable (" 
		            + "taskID INT(64) NOT NULL AUTO_INCREMENT,"  
		            + "taskName VARCHAR(50) NOT NULL," 
		            + "taskDesc VARCHAR(255) NULL, "
		            + "taskStartTS TIMESTAMP NULL,"  
		            + "taskEndTS TIMESTAMP NULL,"
		            + "PRIMARY KEY(taskID))";  
			
			
			statement.executeUpdate(createTaskTable);
			
			ResultSet resultSet = statement.executeQuery("select * from TaskTable");
			
			while(resultSet.next())
			{
				//Object startTSObj = 
				//Object endTSObj = 
				
				Timestamp startTS = resultSet.getTimestamp(4);
				Timestamp endTS = resultSet.getTimestamp(5);;
				
				//resultSet.get
				
				/*if((startTSObj instanceof Timestamp))
				{
					startTS = (Timestamp)startTSObj;
					System.out.println("Start is good!");
				}
				
				if((endTSObj instanceof Timestamp))
				{
					endTS = (Timestamp)endTSObj;
					System.out.println("End is good!");
				}*/
				
				//Timestamp completedTS = resultSet.getTimestamp(5);
				
				model.addRow(new Object[]{resultSet.getString(2), startTS, endTS, resultSet.getString(3)});
			}
			
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally
		{
			if(statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		//table.addColumn(column2);
		
		//table.add(comp)

	}

}
