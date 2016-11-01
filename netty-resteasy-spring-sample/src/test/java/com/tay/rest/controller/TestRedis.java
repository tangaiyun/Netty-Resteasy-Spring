package com.tay.rest.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestRedis {
	public static void main(String[] args) throws InterruptedException {
		
		Jedis jedis = new Jedis("192.168.150.7");
		while(true) {
			
		String keyName1 = "TestKey" + ":" + jedis.time().get(0)+jedis.time().get(1);
		String keyName2 = "Testkey" + ":" + Long.parseLong(jedis.time().get(0))/60;
		String keyName3 = "Testkey" + ":" + Long.parseLong(jedis.time().get(0))/3600;
		String keyName4 = "Testkey" + ":" + Long.parseLong(jedis.time().get(0))/(3600*24);
		
		System.out.println(keyName1);
		System.out.println(keyName2);
		System.out.println(keyName3);
		System.out.println(keyName4);
		jedis.zadd("Test1", 134, "a");
		jedis.zadd("Test1", 145, "b");
		System.out.println(jedis.zcount("Test1", "123", "135"));
		System.out.println("----------------------------------");
			Thread.sleep(1000);
		}
		
	}
}
