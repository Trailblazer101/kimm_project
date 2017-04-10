package com.networking.semesterProject.Server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.networking.semesterProject.ErrorDialog;
import com.networking.semesterProject.LoginWindow;
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

	@Override
	public void run() {

		List<MessageHelper> socketList = new ArrayList<MessageHelper>();

		try {
			welcomeSocket = new ServerSocket(9000);

			while (!welcomeSocket.isClosed()) {
				Socket connectionSocket = welcomeSocket.accept();

				// connectionSocket.
				

				ObjectOutputStream outToServer = new
				ObjectOutputStream(connectionSocket.getOutputStream());
				
				// ObjectInputStream inFromServer = new
				// ObjectInputStream(connectionSocket.getInputStream());

				// BufferedReader inFromClient = new BufferedReader(
				// new InputStreamReader(connectionSocket.getInputStream()));

				//DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

				// clientSentence = inFromClient.readLine();

				// capitalizedSentence = clientSentence.toUpperCase() + '\n';
				//outToClient.writeBytes("Welcome!");
				outToServer.writeObject(new Message(Type.Init, socketList.size(), null, "Welcome!"));
				
				//outToServer.close();
				
				MessageHelper messageHelper = new MessageHelper(stopped, socketList.size(), socketList);
				messageHelper.Start(connectionSocket);
				
				socketList.add(messageHelper);
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				for (MessageHelper messageHelper : socketList) {
					
					ObjectOutputStream outToServer = new
							ObjectOutputStream(messageHelper.clientSocket.getOutputStream());
					
					outToServer.writeObject(new Message(Type.Disconnect, null, null, "Server Asked You To Leave!"));
					//messageHelper.clientSocket.close();
				} //REPLACE WITH CLOSE FROM CLIENT SIDE, TO BE NICE!

				//if(!welcomeSocket.isClosed())
				welcomeSocket.close();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			stopped.OnStopped();
		}
	}

	public void Start(ServerInterface stopCallback) {
		
		stopped = stopCallback;
		new Thread(this).start();
	}
}
