package com.networking.semesterProject.Client;

import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.AbstractMap;

import com.networking.semesterProject.LoginWindow;
import com.networking.semesterProject.Message;
import com.networking.semesterProject.Message.Type;

public class ClientHelper implements Runnable {

	//String sentence = null;
	//String modifiedSentence = null;
	// BufferedReader inFromUser = new BufferedReader(new
	// InputStreamReader(System.in));
	
	private ClientInterface client = null;
	private AbstractMap.SimpleEntry<String, String> usernamePassword = null;
	
	public ClientHelper(ClientInterface clien, AbstractMap.SimpleEntry<String, String> usernamePassword)
	{
		client = clien;
		this.usernamePassword = usernamePassword;
	}

	@Override
	public void run() {
		Socket clientSocket = null;
		try {
			clientSocket = new Socket("localhost", 9000);

			/*DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			sentence = "butttttttts"; // inFromUser.readLine();

			outToServer.writeBytes(sentence + '\n');

			modifiedSentence = inFromServer.readLine();

			System.out.println(modifiedSentence);

			clientSocket.close();*/
			
			ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			outToServer.writeObject(new Message(Type.Init, null, null, usernamePassword.getKey() + ":" + usernamePassword.getValue(), Instant.now()));
			
			ObjectInputStream inFromServer = new
			ObjectInputStream(clientSocket.getInputStream());
			
			Message message = (Message)inFromServer.readObject();
			if(message.type == Type.Disconnect)
				client.OnDisconnected(message.message);
			else
				client.OnConnected(clientSocket, message);
			
		} catch (IOException e1) {			
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			client.OnFailed();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			client.OnFailed();
		} finally 
		{
			/*try {
				if(clientSocket != null)
					clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			client.OnDisconnected();*/
		}
	}
	
public void Start() {
		
		//stopped = stopCallback;
		new Thread(this).start();
	}
}
