package com.networking.semesterProject.Server;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
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
					for(MessageHelper messageHelper : socketList)
					{
						if(!message.source.equals(messageHelper.id) && (message.destination == null || message.destination.contains(messageHelper.id)))
						{
							ObjectOutputStream outToServer = new
									 ObjectOutputStream(messageHelper.clientSocket.getOutputStream());
							
							//maybe clean message up before send?
							outToServer.writeObject(message);
							//DataOutputStream outToClient = new DataOutputStream(messageHelper.clientSocket.getOutputStream());
							//outToClient.writeBytes(message.message);
							//outToClient.close();
						}
					}
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
