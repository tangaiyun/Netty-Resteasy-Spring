package com.tay.rest.auth;

public enum Role {
	MARKET("交易市场用户"),
	MAIN_FORCES("主力用户"),
	USER("一般用户");
	
	private String comments;
	private Role(String comments) {
		this.comments = comments;
	}
	public String getComments() {
		return comments;
	}
}
