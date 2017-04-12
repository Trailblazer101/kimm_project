package com.networking.semesterProject;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class Message implements Serializable
{
	public enum Type {
	    Init, Disconnect, Send
	}
	
	public Type type;
	
	public List<Integer> destination;
	public Integer source;
	
	public String message;
	
	public Instant timestamp;
	
	public Message(Type type, Integer source, List<Integer> destination, String message, Instant timestamp)
	{
		this.type = type;
		this.source = source;
		this.destination = destination;
		this.message = message;
		this.timestamp = timestamp;
	}
}