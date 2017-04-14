package com.networking.semesterProject.Server;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.networking.semesterProject.Message;
import com.networking.semesterProject.Message.Type;

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

	@Override
	public void run() {

		List<MessageHelper> socketList = new ArrayList<MessageHelper>();

		Connection conn = null;
		Statement statement = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=tst12368");
			statement = conn.createStatement();
			statement.executeUpdate("create database if not exists networkProject");

			statement.close();

			conn.setCatalog("networkProject");

			statement = conn.createStatement();

			String createMessageTable = "CREATE TABLE if not exists MessageTable ("
					+ "messageID INT(64) NOT NULL AUTO_INCREMENT," + "messageText VARCHAR(500) NULL, "
					+ "messageTS TIMESTAMP NULL," + "PRIMARY KEY(messageID))";

			statement.executeUpdate(createMessageTable);

			String createUserTable = "CREATE TABLE if not exists UserTable ("
					+ "userID INT(64) NOT NULL AUTO_INCREMENT, " + "userName VARCHAR(25) NOT NULL, " + "userPassword VARCHAR(25) NOT NULL, " + "userFirstName VARCHAR(25) NULL, "
					+ "userLastName VARCHAR(25) NULL, " + "PRIMARY KEY(userID))";

			statement.executeUpdate(createUserTable);

			/*String createMessageTypeTable = "CREATE TABLE if not exists MessageTypeTable ("
					+ "messageTypeID INT(64) NOT NULL AUTO_INCREMENT," + "messageTypeName VARCHAR(25) NULL, "
					+ "PRIMARY KEY(messageTypeID))";

			statement.executeUpdate(createMessageTypeTable);*/

			String createMessageToUserTable = "CREATE TABLE if not exists MessageToUserTable ("
					+ "messageToUserID INT(64) NOT NULL AUTO_INCREMENT," + "userID INT(64) NOT NULL,"
					+ "messageID INT(64) NOT NULL," + "messageTypeID INT(64) NOT NULL,"
					+ "PRIMARY KEY(messageToUserID))";

			statement.executeUpdate(createMessageToUserTable);

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
			
			statement.close();
			
			conn.close();

			welcomeSocket = new ServerSocket(9000);

			while (!welcomeSocket.isClosed()) {
				Socket connectionSocket = welcomeSocket.accept();

				MessageHelper messageHelper = new MessageHelper(stopped, socketList.size(), socketList);
				messageHelper.Start(connectionSocket);

				socketList.add(messageHelper);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				//if (statement != null)
				//	statement.close();

				for (MessageHelper messageHelper : socketList) {

					ObjectOutputStream outToServer = new ObjectOutputStream(
							messageHelper.clientSocket.getOutputStream());

					outToServer
							.writeObject(new Message(Type.Disconnect, null, null, "Server Asked You To Leave!", null));
					// messageHelper.clientSocket.close();
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
