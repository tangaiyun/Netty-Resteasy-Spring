package com.tay.rest.antidefacement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class DuplicateRequestChecker {
	public static final int TTL = 10;
	public static final String DUMMY_VALUE = "";
	@Autowired
	private JedisPool jedisPool;
	public boolean checkDuplicate(String requestUniqueKey) {
		boolean rtv = false;
		if (jedisPool != null) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				if(jedis.exists(requestUniqueKey)) {
					rtv = true;
				}
				else {
					jedis.setex(requestUniqueKey, TTL, DUMMY_VALUE);
					rtv = false;
				}
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
		return rtv;
	}
}
