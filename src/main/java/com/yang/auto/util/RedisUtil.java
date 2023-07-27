package com.yang.auto.util;

import com.alibaba.fastjson.JSONObject;
import com.yang.auto.entity.XxlSsoUser;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

public class RedisUtil {

	public static void setValue(String key, String value, Integer expireTime) {
		StringRedisTemplate stringRedisTemplate = SpringBeanFactoryUtils.getApplicationContext().getBean(StringRedisTemplate.class);
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		ops.set(key, value, expireTime, TimeUnit.SECONDS);
	}

	public static String getValue(String key) {
		StringRedisTemplate stringRedisTemplate = SpringBeanFactoryUtils.getApplicationContext().getBean(StringRedisTemplate.class);
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		return ops.get(key);
	}

	public static void remove(String key) {
		StringRedisTemplate stringRedisTemplate = SpringBeanFactoryUtils.getApplicationContext().getBean(StringRedisTemplate.class);
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		ops.set(key, "", 1, TimeUnit.MILLISECONDS);
		//stringRedisTemplate.delete(key);
	}

	public static void setObjectValue(String redisKey, XxlSsoUser xxlUser, int i) {
		StringRedisTemplate stringRedisTemplate = SpringBeanFactoryUtils.getApplicationContext().getBean(StringRedisTemplate.class);
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		ops.set(redisKey, JSONObject.toJSONString(xxlUser), i, TimeUnit.SECONDS);
	}

	public static String getObjectValue(String redisKey) {
		StringRedisTemplate stringRedisTemplate = SpringBeanFactoryUtils.getApplicationContext().getBean(StringRedisTemplate.class);
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		return ops.get(redisKey);
	}
}
