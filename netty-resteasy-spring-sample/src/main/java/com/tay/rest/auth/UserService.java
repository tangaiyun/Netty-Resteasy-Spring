package com.tay.rest.auth;

public interface UserService {
	public User getUser(String userToken);
	public String login(UserLogin userLogin);
}
