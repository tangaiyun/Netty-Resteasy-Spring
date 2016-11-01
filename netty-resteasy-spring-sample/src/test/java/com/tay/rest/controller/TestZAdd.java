package com.tay.rest.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestZAdd {
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("root-context.xml");
		JedisPool pool = (JedisPool)ac.getBean("jedisPool");
		Jedis jedis = pool.getResource();
		jedis.zadd("testkey111", 1, "1");
		jedis.zadd("testkey111", 1, "2");
		System.out.println(jedis.zcount("testkey111", "-inf", "+inf"));
		while(true) {
			System.out.println(jedis.ttl("testkey111"));
			System.out.println(jedis.exists("testkey111"));
			Thread.sleep(1000);
//			jedis.expire("key", 20);
		}
	}
}
