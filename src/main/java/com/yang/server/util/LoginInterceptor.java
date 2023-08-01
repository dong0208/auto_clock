package com.yang.server.util;

import com.yang.server.entity.XxlSsoUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lww
 * @date 2019-04-18 4:05 PM
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 带星号的当做正则编译处理，需要登陆才能访问
		List<String> urlPartten = new ArrayList<>(16);
		urlPartten.add("/white/delete");
		urlPartten.add("/white/add");
		urlPartten.add("/org/.*");
		urlPartten.add("/account/add/back");

		String servletPath = request.getServletPath();
		Boolean match;
		log.info("LoginInterceptor in {} {}",request.getMethod(),servletPath);
		if (request.getMethod().contains("OPTIONS")) {
			//不拦截options请求
			return true;
		}
		for (String pattern : urlPartten) {
			match = servletPath.matches(pattern);
			if (match) {
				XxlSsoUser xxlUser = SsoWebLoginHelper.loginCheck(request, response);
				if (xxlUser == null) {
					log.info("LoginInterceptor 被拦截 {} {}",request.getMethod(),servletPath);
					UserFriendlyException exception = new UserFriendlyException(SsoConstants.SsoLoginFailErrorMessage);
					exception.setErrorCode(SsoConstants.SsoLoginFailErrorCode);
					exception.setHttpStatusCode(200);
					throw exception;
				}
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}
}
