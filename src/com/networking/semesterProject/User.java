package com.networking.semesterProject;

import java.io.Serializable;

public class User implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum Type {
	    User, Admin
	}
	
	public Type type;
	public Integer id;
	
	public String userName;
	public String userPassword;
	
	public String firstName;
	public String lastName;
	
	public Boolean loggedIn;
	
	public User(Integer id)
	{
		this.type = Type.User;
		this.id = id;
		
		this.userName = null;
		this.userPassword = null;
		
		this.firstName = null;
		this.lastName = null;
		
		this.loggedIn = false;
	}
	
	public User(String userID, String userPassword)
	{
		this.type = Type.User;
		this.id = null;
		
		this.userName = userID;
		this.userPassword = userPassword;
		
		this.firstName = null;
		this.lastName = null;
		
		this.loggedIn = false;
	}
	
	public User(Integer id, String userName)
	{
		this.type = Type.User;
		this.id = id;
		
		this.userName = userName;
		this.userPassword = null;
		
		this.firstName = null;
		this.lastName = null;
		
		this.loggedIn = false;
	}   
	
	public User( Integer id, String userName, String firstName, String lastName)
	{
		this.type = Type.User;
		this.id = id;
		
		this.userName = userName;
		this.userPassword = null;
		
		this.firstName = firstName;
		this.lastName = lastName;
		
		this.loggedIn = false;
	}                                                                                                                                            
}