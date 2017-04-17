package com.networking.semesterProject.Client;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import com.networking.semesterProject.Message;
import com.networking.semesterProject.Message.Type;
import com.networking.semesterProject.User;

public class BatchMessageHelper implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement preparedStatement = null;

		//String[] userNamePassword = message.message.split(":", 2);

		try {
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost/networkProject?user=root&password=tst12368");
			
			
			
			
			String statementString = "SELECT messagetable.userID, messagetable.messageText FROM messagetable "
			+ "INNER JOIN messagetousertable ON messagetable.userID = ? or (messagetable.messageID = messagetousertable.messageID and messagetousertable.userID = ?) "
			+ "WHERE messagetable.messageTS > now() - INTERVAL 1 WEEK "
			+ "ORDER BY messagetable.messageTS ASC";
			
			preparedStatement = conn.prepareStatement(statementString);
			
			Integer userID = messageInfo.getValue().id;
			
			preparedStatement.setInt(1, userID);//userNamePassword[0]);
			preparedStatement.setInt(2, userID);//userNamePassword[0]);
			//preparedStatement.setString(2, message.source.userPassword);//userNamePassword[1]);

			ResultSet resultSet = preparedStatement.executeQuery();

			//ObjectOutputStream outToServer = new ObjectOutputStream(messageInfo.getKey().getOutputStream());

			// ObjectInputStream inFromServer = new
			// ObjectInputStream(connectionSocket.getInputStream());

			List<Message> messageList = new ArrayList<Message>();
			while (resultSet.next()) {
				
				//Message message = new Message(Type.Send, new User(), null, statementString, startInstant)
			}
			
			
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
	
	public BatchMessageHelper(AbstractMap.SimpleEntry<Socket, User> messageInfo)
	{
		this.messageInfo = messageInfo;
	}
	
	private AbstractMap.SimpleEntry<Socket, User> messageInfo = null;
	Instant startInstant = null;

	public void Start(Instant instant) {

		// stopped = stopCallback;
		//clientSocket = socket;
		this.startInstant = instant;
		
		new Thread(this).start();
	}
}
