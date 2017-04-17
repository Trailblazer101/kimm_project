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
import java.util.AbstractMap.SimpleEntry;

import com.networking.semesterProject.Message;
import com.networking.semesterProject.Message.Type;
import com.networking.semesterProject.User;

public class MessageHelper implements Runnable {

	// String clientSentence;
	// String capitalizedSentence;
	// ServerSocket welcomeSocket;

	// private ServerInterface stopped = null;

	//private List<MessageHelper> socketList;
	private Map<Integer, AbstractMap.SimpleEntry<Socket, User>> socketList = new HashMap<Integer, AbstractMap.SimpleEntry<Socket, User>>();
	
	
	public MessageHelper(Map<Integer, AbstractMap.SimpleEntry<Socket, User>> list) {
		// stopped = stop;
		// id = i;
		socketList = list;
	}
	
	
	private AbstractMap.SimpleEntry<Socket, User> messageInfo = null;
	//private Integer userID = null;
	//private User userInfo = null;

	public void Stop() throws IOException {
		// stopped = stopCallback;
		clientSocket.close();
	}

	private Socket clientSocket;

	@Override
	public void run() {

		// List<Socket> socketList = new ArrayList<Socket>();

		try {
			// Socket connectionSocket = welcomeSocket.accept();

			// connectionSocket.
			// socketList.add(connectionSocket);

			// ObjectOutputStream outToServer = new
			// ObjectOutputStream(connectionSocket.getOutputStream());
			while (!clientSocket.isClosed()) {
				ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

				Message message = (Message) inFromServer.readObject();

				// inFromServer.close();

				if (message.type == Message.Type.Send) {
					List<Integer> pushToTable = new ArrayList<Integer>();

					
					
					for (SimpleEntry<Socket, User> messageHelper : socketList.values()) {
						
						Integer id = messageHelper.getValue().id;
						
						if (!message.source.id.equals(id) && (message.destination == null
								|| message.destination.containsKey(id))) {
							ObjectOutputStream outToServer = new ObjectOutputStream(
									messageHelper.getKey().getOutputStream());

							// maybe clean message up before send?
							
							outToServer.writeObject(message);

							pushToTable.add(id);
							// DataOutputStream outToClient = new
							// DataOutputStream(messageHelper.clientSocket.getOutputStream());
							// outToClient.writeBytes(message.message);
							// outToClient.close();
						}
					}

					Connection conn = null;
					PreparedStatement preparedStatement = null;

					try {
						conn = DriverManager.getConnection(
								"jdbc:mysql://localhost/networkProject?user=root&password=tst12368&rewriteBatchedStatements=true");
						preparedStatement = conn.prepareStatement(
								"INSERT INTO MessageTable(messageText, messageTS) VALUES(?,?)",
								Statement.RETURN_GENERATED_KEYS);

						preparedStatement.setString(1, message.message);
						preparedStatement.setTimestamp(2, Timestamp.from(message.timestamp));

						preparedStatement.executeUpdate();

						int messageID = -1;

						ResultSet rs = preparedStatement.getGeneratedKeys();

						rs.next();

						messageID = rs.getInt(1);

						preparedStatement.close();

						preparedStatement = conn.prepareStatement(
								"INSERT INTO MessageToUserTable(userID, messageID, messageTypeID) VALUES(?,?,?)");

						preparedStatement.setInt(1, message.source.id);
						preparedStatement.setInt(2, messageID);
						preparedStatement.setInt(3, ServerHelper.MessageType.Send.ordinal());

						preparedStatement.addBatch();

						int i = 0;

						for (Integer dest : pushToTable) { //message.destination) {
							preparedStatement.setInt(1, dest);
							preparedStatement.setInt(2, messageID);
							preparedStatement.setInt(3, ServerHelper.MessageType.Receive.ordinal());

							preparedStatement.addBatch();

							i++;

							if (i % 1000 == 0 || i == pushToTable.size()) {
								preparedStatement.executeBatch();
							}
						}

						preparedStatement.close();

					} catch (SQLException ex) {
						ex.printStackTrace();
						
						break;
					} finally {
						try {
							preparedStatement.close();
							conn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else if (message.type == Type.Init) {
					// connectionSocket.

					Connection conn = null;
					PreparedStatement preparedStatement = null;

					//String[] userNamePassword = message.message.split(":", 2);

					try {
						conn = DriverManager
								.getConnection("jdbc:mysql://localhost/networkProject?user=root&password=tst12368");
						preparedStatement = conn.prepareStatement(
								"SELECT * FROM UserTable WHERE userName = ? AND userPassword = ?");

						preparedStatement.setString(1, message.source.userName);//userNamePassword[0]);
						preparedStatement.setString(2, message.source.userPassword);//userNamePassword[1]);

						ResultSet resultSet = preparedStatement.executeQuery();

						ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());

						// ObjectInputStream inFromServer = new
						// ObjectInputStream(connectionSocket.getInputStream());

						if (resultSet.next()) {

							Integer userID = resultSet.getInt(1);

							boolean loggedIn = resultSet.getBoolean(6);
							
							User userInfo = new User(userID, resultSet.getString(2), resultSet.getString(4), resultSet.getString(5));

							preparedStatement.close();

							if (loggedIn) {
								outToServer.writeObject(new Message(Type.Disconnect, null, null,
										"You Are Already Logged In, Please Disconnect First!", null));

								break;
							} else {
		
								outToServer.writeObject(new Message(Type.Init, userInfo, null, "Welcome!", null));

								preparedStatement = conn
										.prepareStatement("UPDATE UserTable SET loggedIn = true WHERE userID = ?");
								preparedStatement.setInt(1, userInfo.id);

								preparedStatement.executeUpdate();

								preparedStatement.close();
								
								messageInfo = new AbstractMap.SimpleEntry<Socket, User>(clientSocket, userInfo);
								
								socketList.put(userID, messageInfo);
							}
						} else {
							outToServer.writeObject(new Message(Type.Disconnect, null, null,
									"Couldn't Authenticate, Please Check Your Username and Password!", null));

							break;
						}

					} catch (SQLException ex) {
						ex.printStackTrace();
						
						break;
					} finally {
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

				// DataOutputStream outToClient = new
				// DataOutputStream(clientSocket.getOutputStream());

				// clientSentence = inFromClient.readLine();

				// capitalizedSentence = clientSentence.toUpperCase() + '\n';
				// outToClient.writeBytes("Welcome!");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			if(messageInfo != null)
				socketList.remove(messageInfo.getValue().id);
			
			try {
				// if(!welcomeSocket.isClosed())

				clientSocket.close();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (messageInfo != null) {
				Connection conn = null;
				PreparedStatement preparedStatement = null;

				try {
					conn = DriverManager
							.getConnection("jdbc:mysql://localhost/networkProject?user=root&password=tst12368");

					preparedStatement = conn.prepareStatement("UPDATE UserTable SET loggedIn = false WHERE userID = ?");
					preparedStatement.setInt(1, messageInfo.getValue().id);

					preparedStatement.executeUpdate();

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
				}
			}

			// stopped.OnStopped();
		}
	}

	public void Start(Socket socket) {

		// stopped = stopCallback;
		clientSocket = socket;
		new Thread(this).start();
	}
}
