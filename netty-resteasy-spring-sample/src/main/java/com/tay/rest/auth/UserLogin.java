package com.tay.rest.auth;

public class UserLogin {
	private String account;
	private String password;

	public UserLogin(String account, String password) {
		this.account = account;
		this.password = password;
	}
	
	public UserLogin() {
		
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
