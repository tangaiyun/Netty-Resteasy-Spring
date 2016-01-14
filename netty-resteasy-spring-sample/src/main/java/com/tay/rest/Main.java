package com.tay.rest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import com.tay.rest.controller.HomeController;
import com.tay.rest.server.NettyServer;

public class Main {

	public static void main(String[] args)
			throws Exception {

		ApplicationContext ac = new ClassPathXmlApplicationContext("root-context.xml");
		Assert.notNull(ac);
		Assert.notNull(ac.getBean(HomeController.class));

		NettyServer netty = ac.getBean(NettyServer.class);

		netty.start();

	}
}
