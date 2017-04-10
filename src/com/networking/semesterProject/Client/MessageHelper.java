package com.networking.semesterProject.Client;

import java.io.*;
import java.net.*;
import java.util.*;

import com.networking.semesterProject.Message;
import com.networking.semesterProject.Message.Type;


public class MessageHelper implements Runnable {

	Message message;
	// String capitalizedSentence;
	// ServerSocket welcomeSocket;

	//private StoppedServer stopped = null;
	
	//public MessageHelper() {}

	public Socket clientSocket;

	@Override
	public void run() {

		try {
			ObjectOutputStream outToServer = new
			ObjectOutputStream(clientSocket.getOutputStream());
			
			outToServer.writeObject(message);
			
			//outToServer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Start(Socket socket, Message mess) {
		
		//stopped = stopCallback;
		clientSocket = socket;
		message = mess;
		
		new Thread(this).start();
	}
}
