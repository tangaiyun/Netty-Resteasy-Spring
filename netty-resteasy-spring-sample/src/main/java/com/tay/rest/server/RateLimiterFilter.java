package com.tay.rest.server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.tay.rest.ratelimiter.RateLimiter;
import com.tay.rest.ratelimiter.RedisRateLimiter;

import redis.clients.jedis.JedisPool;

@Component
@Provider
public class RateLimiterFilter implements ContainerRequestFilter {
	@Autowired
	private JedisPool jedisPool;
	private final WeakHashMap<String, RedisRateLimiter> limiterMap = new WeakHashMap<String, RedisRateLimiter>();
	private static final String[] IP_HEADER_KYES = { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP",
			"WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };
	private static final String USER_TOKEN_KEY = "UserToken";
	private static final ServerResponse ACCESS_DENIED = new ServerResponse(
			"Access denied because of exceeding access rate", Status.FORBIDDEN.getStatusCode(), new Headers<Object>());
	protected final Logger logger = LoggerFactory.getLogger(RateLimiterFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext
				.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
		
		Method method = methodInvoker.getMethod();

		if (method.isAnnotationPresent(RateLimiter.class)) {
			RateLimiter rateLimiterAnnotation = method.getAnnotation(RateLimiter.class);
			int permits = rateLimiterAnnotation.permits();
			TimeUnit timeUnit = rateLimiterAnnotation.timeUnit();
			String requestPath = requestContext.getUriInfo().getPath();
			if (rateLimiterAnnotation.base() == RateLimiter.Base.General) {
				String rateLimiterKey = requestPath;
				checkExists(requestPath, timeUnit, permits);
				RedisRateLimiter redisRatelimiter = limiterMap.get(requestPath);
				rateCheck(redisRatelimiter, rateLimiterKey, requestContext);
			} else if (rateLimiterAnnotation.base() == RateLimiter.Base.IP) {
				String ip = getIP(requestContext);
				if (ip != null) {
					String rateLimiterKey = requestPath + ":" + ip;
					checkExists(requestPath, timeUnit, permits);
					RedisRateLimiter redisRatelimiter = limiterMap.get(requestPath);
					rateCheck(redisRatelimiter, rateLimiterKey, requestContext);
				}
			} else if (rateLimiterAnnotation.base() == RateLimiter.Base.User) {
				String userToken = getUserToken(requestContext);
				if (userToken != null) {
					String rateLimiterKey = requestPath + ":" + userToken;
					checkExists(requestPath, timeUnit, permits);
					RedisRateLimiter redisRatelimiter = limiterMap.get(requestPath);
					rateCheck(redisRatelimiter, rateLimiterKey, requestContext);
				}
			}
		}

	}

	private void rateCheck(RedisRateLimiter redisRatelimiter, String keyPrefix,
			ContainerRequestContext requestContext) {
		if (redisRatelimiter != null) {
			if (!redisRatelimiter.acquire(keyPrefix)) {
				logger.info("RateExceedException: keyPrefix:" + keyPrefix);
				requestContext.abortWith(ACCESS_DENIED);
			}
		}
	}

	private void checkExists(String rateLimiterKey, TimeUnit timeUnit, int permits) {
		synchronized (limiterMap) {
			if (limiterMap.get(rateLimiterKey) == null) {
				RedisRateLimiter redisRatelimiter = new RedisRateLimiter(jedisPool, timeUnit, permits);
				limiterMap.put(rateLimiterKey, redisRatelimiter);
			}
		}
	}

	private String getIP(ContainerRequestContext requestContext) {
		for (String ipHeaderKey : IP_HEADER_KYES) {
			String ip = requestContext.getHeaderString(ipHeaderKey);
			if (ip != null && ip.length() != 0 && (!"unknown".equalsIgnoreCase(ip))) {
				return ip;
			}
		}
		return null;
	}

	private String getUserToken(ContainerRequestContext requestContext) {
		String userToken = requestContext.getHeaderString(USER_TOKEN_KEY);
		return userToken;
	}
}
