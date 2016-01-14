package com.tay.rest.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Helloworld {

	String	message;
	
	public Helloworld() {
		
	}
	
	public Helloworld(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
