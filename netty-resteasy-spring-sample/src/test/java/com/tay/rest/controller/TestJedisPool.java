package com.tay.rest.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

public class TestJedisPool {
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("root-context.xml");
		JedisPool pool = (JedisPool) ac.getBean("jedisPool");
		int count = 0;
		while (true) {
			Jedis jedis = pool.getResource();
			jedis.get("keyName");
			Transaction tx = jedis.multi();
			tx.incr("keyName");
			tx.expire("keyName", 10);
			tx.exec();	
			jedis.close();
			System.out.println("jedisPool active count:" + pool.getNumActive());
			System.out.println("jedisPool idle count:" + pool.getNumIdle());
			Thread.sleep(10);
		}

	}

}
