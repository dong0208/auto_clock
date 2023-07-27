package com.yang.auto.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie.Util
 *
 * @author xuxueli 2015-12-12 18:01:06
 */
public class CookieUtil {

	// 默认缓存时间,单位/秒, 2H
	private static final int COOKIE_MAX_AGE = Integer.MAX_VALUE;
	// 保存路径,根路径
	private static final String COOKIE_PATH = "/";

	public static String[] domains = null;
	
	/**
	 * 保存
	 *
	 * @param response
	 * @param key
	 * @param value
	 * @param ifRemember 
	 */
	public static void set(HttpServletResponse response, String key, String value, boolean ifRemember) {
		int age = ifRemember?COOKIE_MAX_AGE:-1;
		set(response, key, value, COOKIE_PATH, age, false);
	}

	/**
	 * 保存
	 *
	 * @param response
	 * @param key
	 * @param value
	 * @param maxAge
	 */
	private static void set(HttpServletResponse response, String key, String value, String path, int maxAge, boolean isHttpOnly) {

//		Assert.isTrue(domains != null && domains.length > 0,"系统错误，服务域名未设置");
		if (domains != null) {
			for (String domain : domains) {
				Cookie cookie = new Cookie(key, value);
				cookie.setDomain(domain);
				cookie.setPath(path);
				cookie.setMaxAge(maxAge);
				cookie.setHttpOnly(isHttpOnly);
				response.addCookie(cookie);
//				String s = key+"="+value+";";
//				response.setHeader("Set-Cookie",s + "Domain="+com.xxl.sso.domain+"; Path=/; Secure;SameSite=None;httponly");
			}
		} else {
			Cookie cookie = new Cookie(key, value);
			cookie.setDomain("hknet-inc.com");
			cookie.setPath(path);
			cookie.setMaxAge(maxAge);
			cookie.setHttpOnly(isHttpOnly);
			response.addCookie(cookie);
		}

//		Cookie hkCookie = new Cookie(key, value);
//		hkCookie.setDomain("hknet-inc.com");
//		hkCookie.setPath(path);
//		hkCookie.setMaxAge(maxAge);
//		hkCookie.setHttpOnly(isHttpOnly);
//		response.addCookie(hkCookie);

//		String s = key+"="+value+";";
//		response.setHeader("Set-Cookie",s + "Domain=hknet-inc.com; Path=/; Secure;SameSite=None;httponly");

//		Cookie paipaiCookie = new Cookie(key, value);
//		paipaiCookie.setDomain("aipaipai-inc.com");
//		paipaiCookie.setPath(path);
//		paipaiCookie.setMaxAge(maxAge);
//		paipaiCookie.setHttpOnly(isHttpOnly);
//		response.addCookie(paipaiCookie);
	}
	
	/**
	 * 查询value
	 *
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getValue(HttpServletRequest request, String key) {
		Cookie cookie = get(request, key);
		if (cookie != null) {
			return cookie.getValue();
		}
		return null;
	}

	/**
	 * 查询Cookie
	 *
	 * @param request
	 * @param key
	 */
	private static Cookie get(HttpServletRequest request, String key) {
		Cookie[] arr_cookie = request.getCookies();
		if (arr_cookie != null && arr_cookie.length > 0) {
			for (Cookie cookie : arr_cookie) {
				if (cookie.getName().equals(key) && cookie.getValue() != null && StringUtils.isNotBlank(cookie.getValue())) {
					return cookie;
				}
			}
		}
		return null;
	}
	
	/**
	 * 删除Cookie
	 *
	 * @param request
	 * @param response
	 * @param key
	 */
	public static void remove(HttpServletRequest request, HttpServletResponse response, String key) {
		Cookie cookie = get(request, key);
		if (cookie != null) {
			set(response, key, "", COOKIE_PATH, 0, false);
		}
	}

}