package com.tay.rest.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestJedisExpire {
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("root-context.xml");
		JedisPool pool = (JedisPool)ac.getBean("jedisPool");
		Jedis jedis = pool.getResource();
		jedis.set("key", "value");
		jedis.expire("key", 5);
		while(true) {
			System.out.println(jedis.ttl("key"));
			System.out.println(jedis.exists("key"));
			Thread.sleep(1000);
//			jedis.expire("key", 20);
		}
		
	}
}

