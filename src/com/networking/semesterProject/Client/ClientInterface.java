package com.networking.semesterProject.Client;

import java.net.Socket;

import com.networking.semesterProject.Message;

public interface ClientInterface
{
	public void OnConnected(Socket clientSocket, Message message);
	public void OnFailed();
	public void OnDisconnected(Message message);
}