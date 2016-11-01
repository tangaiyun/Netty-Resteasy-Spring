package com.tay.rest.controller;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;

import com.tay.rest.pojo.Helloworld;

public class TestHttpAccess {
	@Test
	public void testHelloworld() throws InterruptedException {
		while (true) {
			ResteasyClient client = new ResteasyClientBuilder().build();

			ResteasyWebTarget target = client.target(buildUrl("hello/world/2345"));
			Response response = target.request().header("UserToken", "").get();
//			Helloworld hw = response.readEntity(Helloworld.class);
			System.out.println(response.getStatus());
			Thread.sleep(1000);
		}
	}

	public String buildUrl(String target) {
		return String.format("http://localhost:%d/%s/%s", 8082, "/resteasy", target);
	}
}
