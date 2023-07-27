package com.yang.auto.util;


import com.yang.auto.entity.XxlSsoUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xuxueli 2018-11-15 15:54:40
 */
public class SsoTokenLoginHelper {

	/**
	 * client login
	 *
	 * @param sessionId
	 * @param xxlUser
	 */
	public static void login(HttpServletResponse response, String sessionId, XxlSsoUser xxlUser) {

		String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
		if (storeKey == null) {
			throw new RuntimeException("parseStoreKey Fail, sessionId:" + sessionId);
		}
		SsoLoginStore.put(storeKey, xxlUser);
		CookieUtil.set(response, Conf.SSO_SESSIONID, sessionId, false);
	}

	/**
	 * client logout
	 *
	 * @param sessionId
	 */
	public static void logout(String sessionId) {

		String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
		if (storeKey == null) {
			return;
		}

		SsoLoginStore.remove(storeKey);
	}

	/**
	 * client logout
	 *
	 * @param request
	 */
	public static void logout(HttpServletRequest request) {
		String headerSessionId = request.getHeader(Conf.SSO_SESSIONID);
		logout(headerSessionId);
	}


	/**
	 * login check
	 *
	 * @param sessionId
	 * @return
	 */
	public static XxlSsoUser loginCheck(String sessionId) {
		if (sessionId == null) {
			return null;
		}
		// 兼容部分ios，sessionid值不对的情况
		if (sessionId.contains("xxl_sso_sessionid=")) {
			sessionId = sessionId.replace("xxl_sso_sessionid=","");
		}
		String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
		if (storeKey == null) {
			return null;
		}

		XxlSsoUser xxlUser = SsoLoginStore.get(storeKey);
		if (xxlUser != null) {
			String version = SsoSessionIdHelper.parseVersion(sessionId);
			//单点登陆控制
			//if (xxlUser.getVersion().equals(version)) {

			// After the expiration time has passed half, Auto refresh
			if ((System.currentTimeMillis() - xxlUser.getExpireFreshTime()) > xxlUser.getExpireMinite() / 2) {
				xxlUser.setExpireFreshTime(System.currentTimeMillis());
				SsoLoginStore.put(storeKey, xxlUser);
			}

			return xxlUser;
		}
		//}
		return null;
	}

	/**
	 * login check
	 *
	 * @param sessionId
	 * @return
	 */
	public static XxlSsoUser getUserFromRedis(String sessionId) {
		if (sessionId == null) {
			return null;
		}
		// 兼容部分ios，sessionid值不对的情况
		if (sessionId.contains("xxl_sso_sessionid=")) {
			sessionId = sessionId.replace("xxl_sso_sessionid=","");
		}
		String storeKey = SsoSessionIdHelper.parseStoreKey(sessionId);
		if (storeKey == null) {
			return null;
		}

		XxlSsoUser xxlUser = SsoLoginStore.get(storeKey);
		if (xxlUser != null) {
			return xxlUser;
		}
		return null;
	}


	/**
	 * login check
	 *
	 * @param request
	 * @return
	 */
	public static XxlSsoUser loginCheck(HttpServletRequest request) {
		String value = request.getHeader(Conf.SSO_SESSIONID);
		if (!StringUtils.hasText(value)) {
			value = CookieUtil.getValue(request, Conf.SSO_SESSIONID);
		}
		return loginCheck(value);
	}


}
