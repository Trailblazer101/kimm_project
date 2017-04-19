package com.networking.semesterProject.Server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class UserManager {

	private JFrame frame;
	private JTable table;

	public void Show() {
		frame.setVisible(true);
	}

	/**
	 * Create the application.
	 * @param serverHelper 
	 */
	ServerHelper serverHelper;
	
	public UserManager(ServerHelper serverHelper) {
		
		this.serverHelper = serverHelper;
		initialize();
	}
	
	public class ExtendedTableModel extends DefaultTableModel
	{
		
		public ExtendedTableModel() 
		{
			super(new Object[][] {}, new String[] { "", "ID", "Username", "Password", "First Name", "Last Name", "Logged In" });
		}
		
		
	Class[] columnTypes = new Class[] { Boolean.class, Integer.class, String.class, String.class, String.class,
			String.class, Boolean.class };

	public Class getColumnClass(int columnIndex) {
		return columnTypes[columnIndex];
	}

	boolean[] columnEditables = new boolean[] { true, false, true, true, true, true, true };

	public boolean isCellEditable(int row, int column) {
		return columnEditables[column];
	}
	
	boolean[] isNeeded = new boolean[] { true, true, true, true, false, false, true };
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 537, 430);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		table = new JTable();
		table.setFillsViewportHeight(true);
		table.putClientProperty("terminateEditOnFocusLost", true);
		
		JScrollPane scrollPane = new JScrollPane(table);
		// pane.add(table);

		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		ExtendedTableModel model = new ExtendedTableModel();

		table.setModel(model);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Submit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

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
								.getConnection("jdbc:mysql://localhost/networkProject?user=root&password=tst12368&rewriteBatchedStatements=true");
						preparedStatement = conn.prepareStatement(
								"INSERT INTO UserTable(userID, userName, userPassword, userFirstName, userLastName, loggedIn) VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE userName = ?, userPassword = ?, userFirstName = ?, userLastName = ?, loggedIn = ?");

						int i = 0;

						for (Object[] data : tableData) {
							preparedStatement.setInt(1, (Integer) data[1]);

							preparedStatement.setString(2, (String) data[2]);
							preparedStatement.setString(3, (String) data[3]);
							preparedStatement.setString(4, (String) data[4]);
							preparedStatement.setString(5, (String) data[5]);
							preparedStatement.setBoolean(6, (Boolean) data[6]);

							preparedStatement.setString(7, (String) data[2]);
							preparedStatement.setString(8, (String) data[3]);
							preparedStatement.setString(9, (String) data[4]);
							preparedStatement.setString(10, (String) data[5]);
							preparedStatement.setBoolean(11, (Boolean) data[6]);

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
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						serverHelper.UserListUpdate();
						frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
					}
				}

			}
		});
		panel.add(btnNewButton);

		JButton btnNewButton_3 = new JButton("Insert User");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.addRow(new Object[] { false, (int)model.getValueAt(model.getRowCount() - 1, 1) + 1, null, null, null, null, false });
			}
		});
		panel.add(btnNewButton_3);

		JButton btnNewButton_2 = new JButton("Delete Selected");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Connection conn = null;

				PreparedStatement preparedStatement = null;

				// String[] userNamePassword = message.message.split(":", 2);

				try {
					conn = DriverManager
							.getConnection("jdbc:mysql://localhost/networkProject?user=root&password=tst12368&rewriteBatchedStatements=true");
					preparedStatement = conn.prepareStatement("DELETE FROM UserTable WHERE userID = ?");

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
		panel.add(btnNewButton_2);

		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		JButton button = new JButton("Schedule");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				FillSchedule sch = new FillSchedule();
				sch.setVisible(true);
				
			}
		});
		panel.add(button);
		panel.add(btnNewButton_1);

		Connection conn = null;

		Statement preparedStatement = null;

		// String[] userNamePassword = message.message.split(":", 2);

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/networkProject?user=root&password=tst12368");
			preparedStatement = conn.createStatement();

			ResultSet resultSet = preparedStatement.executeQuery("SELECT * FROM UserTable ORDER BY userID ASC");

			int highestIndex = 0;

			while (resultSet.next()) {

				Integer userID = resultSet.getInt(1);
				String userName = resultSet.getString(2);
				String userPassword = resultSet.getString(3);
				String userFirstName = resultSet.getString(4);
				String userLastName = resultSet.getString(5);
				Boolean loggedIn = resultSet.getBoolean(6);
				
				if(userID > highestIndex)
					highestIndex = userID;

				model.addRow(
						new Object[] { false, userID, userName, userPassword, userFirstName, userLastName, loggedIn });
				//i++;
			}

			model.addRow(new Object[] { false, highestIndex + 1, null, null, null, null, false });
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

}
