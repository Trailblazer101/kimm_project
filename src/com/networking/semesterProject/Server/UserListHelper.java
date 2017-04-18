package com.networking.semesterProject.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import com.networking.semesterProject.Message;
import com.networking.semesterProject.Message.Type;
import com.networking.semesterProject.User;

public class UserListHelper implements Runnable {

	private Map<Integer, User> allUsers = new HashMap<Integer, User>();
	private Map<Integer, AbstractMap.SimpleEntry<Socket, User>> socketList = new HashMap<Integer, AbstractMap.SimpleEntry<Socket, User>>();
	
	public UserListHelper( Map<Integer, User> allUsers, Map<Integer, AbstractMap.SimpleEntry<Socket, User>> list) {
		// stopped = stop;
		// id = i;
		this.socketList = list;
		this.allUsers = allUsers;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement statement = null;

		allUsers.clear();

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/networkProject?user=root&password=tst12368");
			statement = conn.createStatement();

			ResultSet result = statement.executeQuery("SELECT * FROM UserTable");

			while (result.next()) {
				Integer userID = result.getInt(1);

				User user = new User(userID, result.getString(2), result.getString(4), result.getString(5));
				user.loggedIn = result.getBoolean(6);

				allUsers.put(userID, user);
			}

			statement.close();

			conn.close();
			
			for (SimpleEntry<Socket, User> messageHelper : socketList.values()) {

				Integer id = messageHelper.getValue().id;

				ObjectOutputStream outToServer = new ObjectOutputStream(
						messageHelper.getKey().getOutputStream());
				
				outToServer.writeObject(new Message(Type.Update, messageHelper.getValue(), allUsers, null, null));
			}
			

		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void Start() {

		new Thread(this).start();
	}
}
