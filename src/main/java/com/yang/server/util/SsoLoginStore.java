package com.yang.server.util;

import com.alibaba.fastjson.JSONObject;
import com.yang.server.entity.XxlSsoUser;

/**
 * local login store
 *
 * @author xuxueli 2018-04-02 20:03:11
 */
public class SsoLoginStore {

	/**
	 * 1440 minite, 24 hour 30天
	 */
	private static int redisExpireMinite = 30 * 1440;

	public static int getRedisExpireMinite() {
		return redisExpireMinite;
	}

	/**
	 * get
	 */
	public static XxlSsoUser get(String storeKey) {

		String redisKey = redisKey(storeKey);
		String objectValue = RedisUtil.getObjectValue(redisKey);
		if (objectValue != null) {
			XxlSsoUser xxlUser = JSONObject.parseObject(objectValue, XxlSsoUser.class);
			return xxlUser;
		}
		return null;
	}

	/**
	 * remove
	 */
	public static void remove(String storeKey) {
		String redisKey = redisKey(storeKey);
		RedisUtil.remove(redisKey);
	}

	/**
	 * put
	 */
	public static void put(String storeKey, XxlSsoUser xxlUser) {
		String redisKey = redisKey(storeKey);
		// minite to second
		RedisUtil.setObjectValue(redisKey, xxlUser, redisExpireMinite * 60);
	}

	private static String redisKey(String sessionId) {
		return Conf.SSO_SESSIONID.concat("#").concat(sessionId);
	}

}
