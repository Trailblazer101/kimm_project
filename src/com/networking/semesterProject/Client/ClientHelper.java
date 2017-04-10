package com.networking.semesterProject.Client;

import java.io.*;
import java.net.*;

import com.networking.semesterProject.LoginWindow;
import com.networking.semesterProject.Message;

public class ClientHelper implements Runnable {

	String sentence = null;
	String modifiedSentence = null;
	// BufferedReader inFromUser = new BufferedReader(new
	// InputStreamReader(System.in));
	
	ClientInterface client = null;
	
	public ClientHelper(ClientInterface clien)
	{
		client = clien;
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
			
			
			ObjectInputStream inFromServer = new
			ObjectInputStream(clientSocket.getInputStream());
			
			Message message = (Message)inFromServer.readObject();
			
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
