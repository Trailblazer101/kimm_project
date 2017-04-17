package com.networking.semesterProject;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum Type {
	    Init, Disconnect, Send
	}
	
	public Type type;
	
	public Map<Integer, User> destination;
	public User source;
	
	public String message;
	
	public Instant timestamp;
	
	public Message(Type type, User source, Map<Integer, User> destination, String message, Instant timestamp)
	{
		this.type = type;
		this.source = source;
		this.destination = destination;
		this.message = message;
		this.timestamp = timestamp;
	}                                                                                                                                            
}