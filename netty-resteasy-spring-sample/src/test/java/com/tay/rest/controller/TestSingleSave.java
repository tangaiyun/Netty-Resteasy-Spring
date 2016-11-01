package com.tay.rest.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.RandomStringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;

import com.tay.rest.auth.UserLogin;
import com.tay.rest.pojo.Article;

public class TestSingleSave {
	
	@Test
	public void testSinglePOST() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(buildUrl("hello/login"));
		String userName = RandomStringUtils.randomAlphanumeric(3000);
		String password = RandomStringUtils.randomAlphanumeric(3000);
		UserLogin userLogin = new UserLogin(userName,password);
		Response response = target.request().post(Entity.entity(userLogin,MediaType.APPLICATION_JSON));
		String userToken = response.readEntity(String.class);
		System.out.println(userToken);
		response.close();
		
		Article article = new Article(2,"NAME");		
		ResteasyClient client2 = new ResteasyClientBuilder().build();
		ResteasyWebTarget target2 = client2.target(buildUrl("hello/singlesave"));
		Entity<Article> entity = Entity.entity(article,MediaType.APPLICATION_JSON);
		System.out.println("json encoding: "+entity.getEncoding());
		Response response2 = target2.request().header("UserToken",userToken).post(entity);
		Article rtv = response2.readEntity(Article.class);
		response2.close();
		assertNotNull(rtv);
		assertTrue(rtv.getId().equals(2));
		assertTrue(rtv.getName().equals("NAME"));
	}
	

	public String buildUrl(String target) {
		return String.format("http://localhost:%d/%s/%s",
				8082, "resteasy", target);
	}

}
