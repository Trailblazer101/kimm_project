package com.networking.semesterProject.Client;

import java.util.List;
import java.util.Map;

import com.networking.semesterProject.Scheduler;
import com.networking.semesterProject.User;

public interface ExtendedClientInterface extends ClientInterface {
	
	public void OnSchedule(Scheduler schedule);
	public void OnUserList(Map<Integer, User> userList);
}
