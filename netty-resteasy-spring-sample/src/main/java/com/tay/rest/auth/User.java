package com.tay.rest.auth;

public class User {
	private String account;
	private String userName;
	private String password;
	private Role role;
	public User(String account, String userName, String password, Role role) {
		this.setAccount(account);
		this.userName = userName;
		this.password = password;
		this.role = role;
	}
	
	public User() {
		
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
}
