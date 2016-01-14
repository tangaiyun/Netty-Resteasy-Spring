package com.tay.rest.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DefaultUserService implements UserService {
	private Map<String, User> userCache = new HashMap<String, User>();
	private static Random random = new Random();
	private static DefaultUserService singleinstance = new DefaultUserService();

	public User getUser(String userToken) {
		return userCache.get(userToken);
	}

	private DefaultUserService() {
	}

	private static String generateUserToken(String account) {
		return account + getRandomStr(8);
	}

	// 0-9 a-z A-Z
	private static String getRandomStr(int len) {
		byte[] bs = new byte[len];
		for (int i = 0; i < len; i++) {
			int num = random.nextInt(75) + 48;// 48-122
			if (num > 57 && num < 65) {
				num = num + 7;
			} else if (num > 90 && num < 97) {
				num = num + 6;
			}
			bs[i] = (byte) num;
		}
		return new String(bs);
	}

	public static DefaultUserService getInstance() {
		return singleinstance;
	}

	// only mock authentication, should compare to data which load from database
	private boolean isAllowLogin(UserLogin userLogin) {
		boolean rtv = false;
		if("001".equals(userLogin.getAccount()) && "001".equals(userLogin.getPassword())) {
			rtv = true;
		}
		if("002".equals(userLogin.getAccount()) && "abc".equals(userLogin.getPassword())) {
			rtv = true;
		}
		if("003".equals(userLogin.getAccount()) && "xyz".equals(userLogin.getPassword())) {
			rtv = true;
		}
		return rtv;
	}

	@Override
	public String login(UserLogin userLogin) {
		if (isAllowLogin(userLogin)) {
			String userToken = generateUserToken(userLogin.getAccount());
			User user = load(userLogin.getAccount());
			userCache.put(userToken, user);
			return userToken;
		} else {
			return null;
		}
	}

	// only mock data, should load from database
	private User load(String account) {
		if ("001".equals(account)) {
			return new User("001", "袁顾明", "001", Role.MARKET);
		} else if ("002".equals(account)) {
			return new User("002", "杨旭伟", "abc", Role.MAIN_FORCES);
		} else if ("003".equals(account)) {
			return new User("003", "周曦新", "xyz", Role.USER);
		}
		else {
			return null;
		}
	}
}
