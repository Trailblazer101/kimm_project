package com.networking.semesterProject.Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				
			String statementString = "(select messagetable.userID, messagetable.messageText, messagetable.messageTS "
					+ "from messagetable "
					+ "join messagetousertable on messagetousertable.messageID = messagetable.messageID and messagetousertable.userID = ? "
					+ "where messagetable.messageTS > ?) "
					+ "union (select messagetable.userID, messagetable.messageText, messagetable.messageTS "
					+ "from messagetable  where messagetable.messageTS > ? and messagetable.userID = ?) "
					+ "ORDER BY messageTS ASC";
			
			preparedStatement = conn.prepareStatement(statementString);
			
			Integer userID = messageInfo.getValue().id;
			Timestamp startTimestamp = Timestamp.from(startInstant);
			
			preparedStatement.setInt(1, userID);//userNamePassword[0]);
			preparedStatement.setTimestamp(2, startTimestamp);
			preparedStatement.setTimestamp(3, startTimestamp);
			preparedStatement.setInt(4, userID);//userNamePassword[0]);
			//preparedStatement.setString(2, message.source.userPassword);//userNamePassword[1]);

			ResultSet resultSet = preparedStatement.executeQuery();

			//ObjectOutputStream outToServer = new ObjectOutputStream(messageInfo.getKey().getOutputStream());

			// ObjectInputStream inFromServer = new
			// ObjectInputStream(connectionSocket.getInputStream());

			List<Message> messageList = new ArrayList<Message>();
			while (resultSet.next()) {
				
				Message message = new Message(Type.Send, new User(resultSet.getInt(1)), null, resultSet.getString(2), resultSet.getTimestamp(3).toInstant());
				messageList.add(message);
			}
			
			preparedStatement.close();
			
			Map<Integer, User> parsedUsers = new HashMap<Integer, User>();
			parsedUsers.put(messageInfo.getValue().id, messageInfo.getValue());
			
			for(Message message : messageList)
			{
				User user = message.source;
				
				if(!parsedUsers.containsKey(message.source.id))
				{				
					preparedStatement = conn.prepareStatement("SELECT userName, userFirstName, userLastName FROM usertable WHERE userID = ?");
					preparedStatement.setInt(1, message.source.id);
					
					resultSet = preparedStatement.executeQuery();
					
					if (resultSet.next()) {
						
						user = new User(message.source.id, resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
					}
					
					parsedUsers.put(message.source.id, user);

					preparedStatement.close();
				} else 
				{
					user = parsedUsers.get(message.source.id);
				}
				
				message.source = user;
			}
			
			ObjectOutputStream outToServer = new ObjectOutputStream(messageInfo.getKey().getOutputStream());
			outToServer.writeObject(messageList);
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
