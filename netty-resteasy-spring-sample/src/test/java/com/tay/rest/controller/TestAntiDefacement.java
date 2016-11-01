package com.tay.rest.controller;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.RandomStringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tay.rest.antidefacement.Coder;
import com.tay.rest.auth.UserLogin;
import com.tay.rest.pojo.SafeResponse;

import static org.junit.Assert.assertTrue;

public class TestAntiDefacement {

	@Test
	public void testSinglePOST() throws JsonProcessingException {
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(buildUrl("hello/loginecho"));
		String userName = "abc";
		String password = "efg";
		UserLogin userLogin = new UserLogin(userName,password);
		String accessRandomStr = RandomStringUtils.randomAlphanumeric(8);
		Long time = System.currentTimeMillis();
		ObjectMapper mapper = new ObjectMapper();  
        
        // Convert object to JSON string  
        String Json =  mapper.writeValueAsString(userLogin);
		String signature = Coder.genSignature(Json);
		
		Response response = target.request().header("ACCESS_RANDOM_STR", accessRandomStr).header("ACCESS_TIME", time).header("ACCESS_SIGNATURE",signature).post(Entity.entity(userLogin,MediaType.APPLICATION_JSON));
		GenericType<SafeResponse<UserLogin>> SafeResponseType = new GenericType<SafeResponse<UserLogin>>() {};
		SafeResponse<UserLogin> sr = response.readEntity(SafeResponseType);
		System.out.println(sr.getContent());
		System.out.println(sr.getSignature());
		String returnJson = mapper.writeValueAsString(sr.getContent());
		String returnSinature = Coder.genSignature(returnJson);
		assertTrue(sr.getSignature().equals(returnSinature));
		response.close();
	}
	public String buildUrl(String target) {
		return String.format("http://localhost:%d/%s/%s",
				8082, "resteasy", target);
	}
}
