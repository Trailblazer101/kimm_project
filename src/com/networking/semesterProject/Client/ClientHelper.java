package com.networking.semesterProject.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.networking.semesterProject.Message;
import com.networking.semesterProject.Message.Type;

public class ClientHelper implements Runnable {

	private Socket clientSocket;
	private ClientInterface inter;

	public ClientHelper(ClientInterface inter, Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.inter = inter;
	}

	@Override
	public void run() {

		try {
			// BufferedReader inFromServer = new BufferedReader(new
			// InputStreamReader(clientSocket.getInputStream()));

			while (!clientSocket.isClosed()) {
				ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

				// BufferedReader inFromServer = new BufferedReader(new
				// InputStreamReader(clientSocket.getInputStream()));

				Object msgObject = inFromServer.readObject();

				if (msgObject instanceof Message) {
					Message message = (Message) msgObject;
					if (message.type == Type.Send) {
						/*ZoneId zoneId = ZoneId.of("America/New_York");
						ZonedDateTime zdt = ZonedDateTime.ofInstant(message.timestamp, zoneId);

						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm");

						message.message = message.source.userName + " (" + zdt.format(formatter) + "): "
								+ message.message + "\n";*/

						inter.OnConnected(clientSocket, message);

						// textArea.append();
					} else if (message.type == Type.Disconnect) {
						// textArea.setText("Disconnected With Message:\n\t" +
						// message.message + "\n");
						clientSocket.close();

						inter.OnDisconnected(message);
					}
				} else if(msgObject instanceof List<?>)
				{
					List<Message> messageList = (List<Message>)msgObject;
					
					ZoneId zoneId = ZoneId.of("America/New_York");
					
					for(Message message : messageList)
					{	
						ZonedDateTime zdt = ZonedDateTime.ofInstant(message.timestamp, zoneId);

						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm");

						message.message = message.source.userName + " (" + zdt.format(formatter) + "): "
								+ message.message + "\n";

						inter.OnConnected(clientSocket, message);	
					}
				}

				// inFromServer.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Start() {

		// stopped = stopCallback;
		new Thread(this).start();
	}
}
