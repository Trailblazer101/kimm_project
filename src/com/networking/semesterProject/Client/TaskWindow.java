package com.networking.semesterProject.Client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.networking.semesterProject.LoginWindow;
import com.networking.semesterProject.Server.UserManager.ExtendedTableModel;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EventObject;
import java.sql.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class TaskWindow extends JFrame {

	private JPanel contentPane;
	private JTable table;
	
	public class ExtendedTableModel extends DefaultTableModel
	{
		
		
		public ExtendedTableModel() 
		{
			super(new Object[][] {}, new String[] { "", "ID", "Name", "Description", "Started", "Completed" });
		}
		
		
	Class[] columnTypes = new Class[] { Boolean.class, String.class, String.class, String.class, String.class, String.class };

	public Class getColumnClass(int columnIndex) {
		return columnTypes[columnIndex];
	}

	boolean[] columnEditables = new boolean[] { true, false, true, true, true, true};

	public boolean isCellEditable(int row, int column) {
		return columnEditables[column];
	}
	
	boolean[] isNeeded = new boolean[] { true, true, true, false, false, false};
	}

	
	private void RefreshTasks()
	{
		model.setRowCount(0);
		
		Connection conn = null;

		Statement preparedStatement = null;

		// String[] userNamePassword = message.message.split(":", 2);

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + LoginWindow.serverIP + "/networkProject?user=root&password=tst12368");
			preparedStatement = conn.createStatement();

			ResultSet resultSet = preparedStatement.executeQuery("SELECT * FROM TaskTable ORDER BY taskEndTS, taskStartTS ASC");

			int highestIndex = 0;
			
			 ZoneId zoneId = ZoneId.of("America/New_York");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm");

			while (resultSet.next()) {

				Integer taskID = resultSet.getInt(1);
				String taskName = resultSet.getString(2);
				String taskDescription = resultSet.getString(3);
				
				
				
				String taskStartString = null;
				try
				{
					ZonedDateTime zdt = ZonedDateTime.ofInstant((Instant)resultSet.getTimestamp(4).toInstant(), zoneId);
					taskStartString = zdt.format(formatter);
				}
				catch (SQLException e) {}
				catch (NullPointerException e) {}
				
				String taskEndString = null;
				try
				{
					ZonedDateTime zdt = ZonedDateTime.ofInstant((Instant)resultSet.getTimestamp(5).toInstant(), zoneId);
					taskEndString = zdt.format(formatter);
				}
				catch (SQLException e) {}
				catch (NullPointerException e) {}
				
				if(taskID > highestIndex)
					highestIndex = taskID;

				model.addRow(
						new Object[] { false, taskID, taskName, taskDescription, taskStartString, taskEndString });
				//i++;
			}

			model.addRow(new Object[] { false, highestIndex + 1, null, null, null, null });
		} catch (SQLException ex) {
			ex.printStackTrace();

		} finally {
			try {
				preparedStatement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private ExtendedTableModel model;
	/**
	 * Create the frame.
	 */
	public TaskWindow() {
		setTitle("Tasks");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 415, 470);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JMenuBar menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmRefresh = new JMenuItem("Refresh");
		mntmRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RefreshTasks();
			}
		});
		mnFile.add(mntmRefresh);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispatchEvent(new WindowEvent(TaskWindow.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		mnFile.add(mntmClose);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setFillsViewportHeight(true);
		
		
		model = new ExtendedTableModel();

		table.setModel(model);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnAddTask = new JButton("Submit");
		btnAddTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int rowCount = model.getRowCount(), columnCount = model.getColumnCount();

				// Object[][] tableData = new Object[rowCount][columnCount];

				List<Object[]> tableData = new ArrayList<Object[]>();

				Object tempCell = null;
				Object[] tempRow = null;
				int j;

				for (int i = 0; i < rowCount; i++) {
					tempRow = new Object[columnCount];

					for (j = 0; j < columnCount; j++) {
						tempCell = model.getValueAt(i, j);
						if (!model.isNeeded[j] || tempCell != null)
							tempRow[j] = tempCell;
						else
							break;
					}

					if (j >= columnCount) {
						tableData.add(tempRow);
					}
				}

				if (!tableData.isEmpty()) {
					Connection conn = null;

					PreparedStatement preparedStatement = null;

					// String[] userNamePassword = message.message.split(":",
					// 2);

					try {
						conn = DriverManager
								.getConnection("jdbc:mysql://" + LoginWindow.serverIP + "/networkProject?user=root&password=tst12368&rewriteBatchedStatements=true");
						preparedStatement = conn.prepareStatement(
								"INSERT INTO TaskTable(taskID, taskName, taskDesc, taskStartTS, taskEndTS) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE taskName = ?, taskDesc = ?, taskStartTS = ?, taskEndTS = ?");

						int i = 0;
						

					      SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
					     

						for (Object[] data : tableData) {
							preparedStatement.setInt(1, (Integer) data[1]);

							preparedStatement.setString(2, (String) data[2]);
							preparedStatement.setString(3, (String) data[3]);
							
							 
							Timestamp startTS = null;
							 
							try {
								
								startTS = new Timestamp(dateFormat.parse(((String) data[4])).getTime());
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (NullPointerException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							Timestamp endTS = null;
							try {
								endTS = new Timestamp(dateFormat.parse(((String) data[5])).getTime());
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}catch (NullPointerException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						     //java.sql.Timestamp timeStampDate = DateFormat.parse((String) data[4]);
							
							
							preparedStatement.setTimestamp(4, startTS);
							preparedStatement.setTimestamp(5, endTS);

							preparedStatement.setString(6, (String) data[2]);
							preparedStatement.setString(7, (String) data[3]);
							preparedStatement.setTimestamp(8, startTS);
							preparedStatement.setTimestamp(9, endTS);
							
							preparedStatement.addBatch();

							i++;

							if (i % 1000 == 0 || i == tableData.size()) {
								// i = 0;
								preparedStatement.executeBatch();
							}
						}

						preparedStatement.close();

					} catch (SQLException ex) {
						ex.printStackTrace();

					} finally {
						try {
							preparedStatement.close();
							conn.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						RefreshTasks();
						//dispatchEvent(new WindowEvent(TaskWindow.this, WindowEvent.WINDOW_CLOSING));
					}
				}
			}
		});
		panel.add(btnAddTask);
		
		JButton btnInsertTask = new JButton("Insert Task");
		btnInsertTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.addRow(new Object[] { false, (int)model.getValueAt(model.getRowCount() - 1, 1) + 1, null, null, null });
			}
		});
		panel.add(btnInsertTask);
		
		JButton btnNewButton = new JButton("Delete Selected");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Connection conn = null;

				PreparedStatement preparedStatement = null;

				// String[] userNamePassword = message.message.split(":", 2);

				try {
					conn = DriverManager
							.getConnection("jdbc:mysql://" + LoginWindow.serverIP + "/networkProject?user=root&password=tst12368&rewriteBatchedStatements=true");
					preparedStatement = conn.prepareStatement("DELETE FROM TaskTable WHERE taskID = ?");

					int statementCount = 0;

					for (int i = 0; i < model.getRowCount(); i++) {
						if ((boolean) model.getValueAt(i, 0)) {
							
							
							preparedStatement.setInt(1, (int) model.getValueAt(i, 1));
							model.removeRow(i);
							
							preparedStatement.addBatch();

							if (statementCount == 1000) {
								preparedStatement.executeBatch();

								statementCount = 0;
							} else
								statementCount++;
						}
					}

					if (statementCount > 0)
						preparedStatement.executeBatch();

				} catch (SQLException ex) {
					ex.printStackTrace();

				} finally {
					try {
						preparedStatement.close();
						conn.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		panel.add(btnNewButton);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispatchEvent(new WindowEvent(TaskWindow.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		panel.add(btnClose);
		
		RefreshTasks();
	}

}
