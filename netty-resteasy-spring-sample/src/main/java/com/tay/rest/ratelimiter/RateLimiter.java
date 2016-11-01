package com.tay.rest.ratelimiter;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface RateLimiter {

	public enum Base {
		General, IP, User
	};

	Base base();
	
	TimeUnit timeUnit() default TimeUnit.SECONDS;

	int permits();
}
