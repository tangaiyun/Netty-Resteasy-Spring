package com.tay.rest.server;

public class BizException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String msg;
	public BizException(){
		
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public BizException(String code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}
}
