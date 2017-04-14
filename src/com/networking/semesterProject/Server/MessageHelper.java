package com.networking.semesterProject.Server;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

import com.networking.semesterProject.Message;
import com.networking.semesterProject.Message.Type;


public class MessageHelper implements Runnable {

	// String clientSentence;
	// String capitalizedSentence;
	// ServerSocket welcomeSocket;

	private ServerInterface stopped = null;
	
	private List<MessageHelper> socketList;
	
	public MessageHelper(ServerInterface stop, Integer i, List<MessageHelper> list)
	{
		stopped = stop;
		id = i;
		socketList = list;
	}
	
	private Integer id;

	public void Stop() throws IOException {
		//stopped = stopCallback;
		clientSocket.close();
	}

	public Socket clientSocket;

	@Override
	public void run() {

		//List<Socket> socketList = new ArrayList<Socket>();
	
		try {			
				//Socket connectionSocket = welcomeSocket.accept();

				// connectionSocket.
				//socketList.add(connectionSocket);

				// ObjectOutputStream outToServer = new
				// ObjectOutputStream(connectionSocket.getOutputStream());
			while (!clientSocket.isClosed()) {
				ObjectInputStream inFromServer = new
				ObjectInputStream(clientSocket.getInputStream());
				
				
				Message message = (Message)inFromServer.readObject();
				
				//inFromServer.close();
				
				if(message.type == Message.Type.Send)
				{
					//List<Integer> pushToTable = new ArrayList<Integer>();
									
					for(MessageHelper messageHelper : socketList)
					{
						if(!message.source.equals(messageHelper.id) && (message.destination == null || message.destination.contains(messageHelper.id)))
						{
							ObjectOutputStream outToServer = new
									 ObjectOutputStream(messageHelper.clientSocket.getOutputStream());
							
							//maybe clean message up before send?
							outToServer.writeObject(message);
							
							//pushToTable.add(messageHelper.id);
							//DataOutputStream outToClient = new DataOutputStream(messageHelper.clientSocket.getOutputStream());
							//outToClient.writeBytes(message.message);
							//outToClient.close();
						}
					}
					
					Connection conn = null;
					PreparedStatement preparedStatement = null;
					
					try {
					conn = DriverManager.getConnection("jdbc:mysql://localhost/networkProject?user=root&password=tst12368&rewriteBatchedStatements=true");
					preparedStatement = conn.prepareStatement("INSERT INTO MessageTable(messageText, messageTS) VALUES(?,?)",  Statement.RETURN_GENERATED_KEYS);
					
					preparedStatement.setString(1, message.message);
					preparedStatement.setTimestamp(2, Timestamp.from(message.timestamp));
					
					preparedStatement.executeUpdate();
					
					int messageID = -1;

					ResultSet rs = preparedStatement.getGeneratedKeys();

					rs.next();
					
					messageID = rs.getInt(1);
					
					preparedStatement.close();
					
					preparedStatement = conn.prepareStatement("INSERT INTO MessageToUserTable(userID, messageID, messageTypeID) VALUES(?,?,?)");

					preparedStatement.setInt(1, message.source);
					preparedStatement.setInt(2, messageID);
					preparedStatement.setInt(3, ServerHelper.MessageType.Send.ordinal());
					
					preparedStatement.addBatch();
					
					int i = 1;
					
					for(Integer dest : message.destination)
					{
						preparedStatement.setInt(1, dest);
						preparedStatement.setInt(2, messageID);
						preparedStatement.setInt(3, ServerHelper.MessageType.Receive.ordinal());
						
						preparedStatement.addBatch();
						
						i++;
						
						if(i % 1000 == 0 || i == message.destination.size())
						{
							preparedStatement.executeBatch();
						}
					}
					
					preparedStatement.close();
					
					} catch (SQLException ex) {
						ex.printStackTrace();
					} finally
					{
						try {
							preparedStatement.close();
							conn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
					}
				} else if(message.type == Type.Init)
				{
					// connectionSocket.
					
					
					Connection conn = null;
					PreparedStatement preparedStatement = null;
					
					
					String[] userNamePassword = message.message.split(":", 2);
					
					try {
					conn = DriverManager.getConnection("jdbc:mysql://localhost/networkProject?user=root&password=tst12368");
					preparedStatement = conn.prepareStatement("SELECT userID FROM UserTable WHERE userName = ? AND userPassword = ?");
					
					preparedStatement.setString(1, userNamePassword[0]);
					preparedStatement.setString(2, userNamePassword[1]);
					
					ResultSet resultSet = preparedStatement.executeQuery();
					
					ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());

					// ObjectInputStream inFromServer = new
					// ObjectInputStream(connectionSocket.getInputStream());


					if(resultSet.next()){
						outToServer.writeObject(new Message(Type.Init, resultSet.getInt(1), null, "Welcome!", null));
						} else 
						{
							outToServer.writeObject(new Message(Type.Disconnect, null, null, "Couldn't Authenticate, Please Check Your Username and Password!", null));
						}
					
					
					} catch (SQLException ex) {
						ex.printStackTrace();
					} finally
					{
						try {
							preparedStatement.close();
							conn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
					}

					// outToServer.close();
				}

				// BufferedReader inFromClient = new BufferedReader(
				// new InputStreamReader(connectionSocket.getInputStream()));

				//DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

				// clientSentence = inFromClient.readLine();

				// capitalizedSentence = clientSentence.toUpperCase() + '\n';
				//outToClient.writeBytes("Welcome!");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				//if(!welcomeSocket.isClosed())

				clientSocket.close();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			stopped.OnStopped();
		}
	}

	public void Start(Socket socket) {
		
		//stopped = stopCallback;
		clientSocket = socket;
		new Thread(this).start();
	}
}
