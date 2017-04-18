package com.networking.semesterProject;

import java.io.*;
import java.util.*;
import java.sql.*;


public class Scheduler implements Serializable{
	
	//Submit button
	//connect to db 
	//send info to db
	//
	
	private String userID;	// user id
	
	private List<String> workWeek;	// Work schedule
	
	private Connection conn = null;
	private Statement stmt = null;
	
	
	public Scheduler(String userID, List<String> workWeek ){
		this.userID = userID;
		this.workWeek = workWeek;
		
	}
	
	public Scheduler(ResultSet rs) {
		
		workWeek = new ArrayList<String>();
		
			for(int i = 1 ; i <= 7; i++ ){
				
					try {
						workWeek.add(rs.getString(i));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	
	public Scheduler() {
		// TODO Auto-generated constructor stub
		
		workWeek = null;
	}

	public List<String> getSchedule() {
		
		return workWeek;
	}
	
	
	public void setSchedule() {	// Takes info from the screen and stores it when submit is pressed
		
		/*
		 * Need to find out if conenction is active 
		 * if not make connection here
		 * 
		 */
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=tst12368");
			
			
			stmt =  conn.createStatement();
			
			stmt.executeUpdate("create database if not exists networkProject");
			
			stmt.close();
			
			stmt = conn.createStatement();
			
			
			String sql; 
			sql = "INSERT INTO Schedule (userid, Mon, Tues, Wed, Thurs, Fri, Sat, Sun)" + 
								"VALUES( ?, ?, ?, ?, ?, ?, ?, ?)";// insert statement
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			conn.setAutoCommit(false);
			
			pstmt.setString(1, userID);
			
			pstmt.setString(2, workWeek.get(0));		//Monday
			pstmt.setString(2, workWeek.get(1));		//Tuesday
			pstmt.setString(2, workWeek.get(2));		//Wednesday
			pstmt.setString(2, workWeek.get(3));		//Thursday
			pstmt.setString(2, workWeek.get(4));		//Friday		
			pstmt.setString(2, workWeek.get(5));		//Saturday
			pstmt.setString(2, workWeek.get(6));		//Sunday

			pstmt.addBatch();
			
			
			stmt.executeBatch();
			
			stmt.close();
			
			conn.close();	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

	
	
}