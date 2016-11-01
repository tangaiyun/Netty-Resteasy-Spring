package com.tay.rest.pojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tay.rest.antidefacement.Coder;

public class SafeResponse<T> {
	T content;
	private String signature;
	public void setContent(T t) {
		this.content = t;
	}
	public T getContent() {
		return content;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getSignature() {
		return signature;
	}
	
	public static <T> SafeResponse<T> wrap(T t) {
		SafeResponse<T> instance = new SafeResponse<T>();
		instance.setContent(t);
		ObjectMapper mapper = new ObjectMapper();  
        
        // Convert object to JSON string  
        String Json = null;
		try {
			Json = mapper.writeValueAsString(t);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String signature = Coder.genSignature(Json);
		instance.setSignature(signature);
		return instance;
		
	}
}
