package com.networking.semesterProject.Server;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

import com.networking.semesterProject.LoginWindow;
import com.networking.semesterProject.Message;
import com.networking.semesterProject.Message.Type;
import com.networking.semesterProject.User;

public class ServerHelper implements Runnable {

	// String clientSentence;
	// String capitalizedSentence;
	// ServerSocket welcomeSocket;

	private ServerInterface stopped = null;

	public void Stop(ServerInterface stopCallback) throws IOException {
		stopped = stopCallback;
		welcomeSocket.close();
	}

	ServerSocket welcomeSocket;
	
	public enum MessageType {Send, Receive}
	
	//private List<MessageHelper> socketList = new ArrayList<MessageHelper>();

	private Map<Integer, SimpleEntry<Socket, User>> socketList = new HashMap<Integer, SimpleEntry<Socket, User>>();
	private Map<Integer, User> allUsers = new HashMap<Integer, User>();
	
	
	public void UserListUpdate()
	{
		UserListHelper helper = new UserListHelper(allUsers, socketList);
		helper.Start();
	}
	
	@Override
	public void run() {

		Connection conn = null;
		Statement statement = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=tst12368");
			statement = conn.createStatement();
			statement.executeUpdate("create database if not exists networkProject");

			statement.close();

			conn.setCatalog("networkProject");

			statement = conn.createStatement();
			
			String createUserTable = "CREATE TABLE if not exists UserTable ("
					+ "userID INT(64) NOT NULL AUTO_INCREMENT, " + "userName VARCHAR(25) NOT NULL, " + "userPassword VARCHAR(25) NOT NULL, " + "userFirstName VARCHAR(25) NULL, "
					+ "userLastName VARCHAR(25) NULL, " + "loggedIn BOOLEAN NULL, " + "PRIMARY KEY(userID))";

			statement.executeUpdate(createUserTable);

			String createMessageTable = "CREATE TABLE if not exists MessageTable ("
					+ "messageID INT(64) NOT NULL AUTO_INCREMENT, " + "userID INT(64) NOT NULL, " + "messageText VARCHAR(500) NULL, "
					+ "messageTS TIMESTAMP NULL, " + "PRIMARY KEY(messageID), "
					+ "CONSTRAINT FK_Message_UserID FOREIGN KEY (userID) REFERENCES UserTable(userID)"
					+ ")";

			statement.executeUpdate(createMessageTable);

			/*String createMessageTypeTable = "CREATE TABLE if not exists MessageTypeTable ("
					+ "messageTypeID INT(64) NOT NULL AUTO_INCREMENT," + "messageTypeName VARCHAR(25) NULL, "
					+ "PRIMARY KEY(messageTypeID))";

			statement.executeUpdate(createMessageTypeTable);*/

			String createMessageToUserTable = "CREATE TABLE if not exists MessageToUserTable ("
					+ "messageToUserID INT(64) NOT NULL AUTO_INCREMENT, " + "userID INT(64) NOT NULL, "
					+ "messageID INT(64) NOT NULL, "
					+ "PRIMARY KEY(messageToUserID), " + "CONSTRAINT FK_MToU_UserID FOREIGN KEY (userID) REFERENCES UserTable(userID), "
					+ "CONSTRAINT FK_MToU_MessageID FOREIGN KEY (messageID) REFERENCES MessageTable(messageID)" + ")";

			statement.executeUpdate(createMessageToUserTable);
			
			String createScheduleTable = "CREATE TABLE if not exists Schedule ("
					  + "userID INT(64) NOT NULL, "
					  + "Monday VARCHAR(45) NULL, "
					  + "Tuesday VARCHAR(45) NULL, "
					  + "Wednesday VARCHAR(45) NULL, "
					  + "Thursday VARCHAR(45) NULL, "
					  + "Friday VARCHAR(45) NULL, "
					  + "Saturday VARCHAR(45) NULL, "
					  + "Sunday VARCHAR(45) NULL, "
					  + "CONSTRAINT FK_Schedule_UserID FOREIGN KEY (userID) REFERENCES UserTable(userID))";
					  //+ "PRIMARY KEY (userID))";
			
			statement.executeUpdate(createScheduleTable);
			
			String createTaskTable = "CREATE TABLE if not exists TaskTable (" 
		            + "taskID INT(64) NOT NULL AUTO_INCREMENT, "  
		            + "taskName VARCHAR(50) NOT NULL, " 
		            + "taskDesc VARCHAR(255) NULL, "
		            + "taskStartTS TIMESTAMP NULL, "  
		            + "taskEndTS TIMESTAMP NULL, "
		            + "PRIMARY KEY(taskID))";  
			
			
			statement.executeUpdate(createTaskTable);
			
			//String insertDefaultMessageTypes = "INSERT INTO MessageTypeTable " + "(messageTypeName)"
			//		+ " SELECT ('Send'), ('Receive')" + " WHERE 0 = (SELECT COUNT(*) FROM MessageTypeTable );";
			
			
			/*String insertDefaultMessageTypes = "INSERT INTO MessageTypeTable (messageTypeName)"
		    + " select t.*"
		    + " from ((SELECT 'Send' as messageTypeName"
		          + ") union all"
		          + " (SELECT 'Receive')"
		         + ") t"
		    + " WHERE NOT EXISTS (SELECT * FROM MessageTypeTable)";

			statement.executeUpdate(insertDefaultMessageTypes);*/
			
			/*
			 * PreparedStatement prepState = conn.prepareStatement("SELECT * FROM UserTable");
			 */
			ResultSet result = statement.executeQuery("SELECT * FROM UserTable");
			
			while(result.next())
			{
				Integer userID = result.getInt(1);
				
				User user = new User(userID, result.getString(2), result.getString(4), result.getString(5));
				user.loggedIn = result.getBoolean(6);
				
				allUsers.put(userID, user);
			}
			
			statement.close();
			
			conn.close();

			welcomeSocket = new ServerSocket(LoginWindow.serverPort);

			while (!welcomeSocket.isClosed()) {
				Socket connectionSocket = welcomeSocket.accept();

				MessageHelper messageHelper = new MessageHelper(allUsers, socketList);
				messageHelper.Start(connectionSocket);

				//socketList.add(messageHelper);
				//ADD IN MESSAGE HELPER!!!
			}

		} catch (SocketException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				//if (statement != null)
				//	statement.close();

				for (SimpleEntry<Socket, User> messageHelper : socketList.values()) {

					ObjectOutputStream outToServer = new ObjectOutputStream(
							messageHelper.getKey().getOutputStream());

					outToServer
							.writeObject(new Message(Type.Disconnect, null, null, "Server Asked You To Leave!", null));
				
					//messageHelper.getKey().close();
				} // REPLACE WITH CLOSE FROM CLIENT SIDE, TO BE NICE!

				// if(!welcomeSocket.isClosed())
				welcomeSocket.close();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			/*} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			*/}

			stopped.OnStopped();
		}
	}

	public void Start(ServerInterface stopCallback) {

		stopped = stopCallback;
		new Thread(this).start();
	}
}
