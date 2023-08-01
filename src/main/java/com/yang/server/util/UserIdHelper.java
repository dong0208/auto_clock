package com.yang.server.util;

import com.yang.server.entity.XxlSsoUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class UserIdHelper {

    private void printSSONoLogInfo(HttpServletRequest request, HttpServletResponse response,String reason) {
        try {
            log.info("printSSONoLogInfo:{} cookieUtils xxl_sso_sessionid:{}", reason,CookieUtil.getValue(request, "xxl_sso_sessionid"));
            log.info("printSSONoLogInfo:{} requestHeader sessionId:{}",reason,request.getHeader("sessionId"));
            log.info("printSSONoLogInfo:{} requestHeader sessionId:{}",reason,request.getHeader("sessionid"));
            log.info("printSSONoLogInfo:{} requestHeader sessionId:{}",reason,request.getHeader("cookie"));
            log.info("printSSONoLogInfo:{} requestHeader sessionId:{}",reason,request.getHeader("Cookie"));
            log.info("printSSONoLogInfo:{} requestParameter xxl_sso_sessionid:{}",reason,request.getParameter("xxl_sso_sessionid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前登录用户ID，做登录校验，返回特殊错误码。
     *
     * @throws UserFriendlyException
     */
    public Long getCurrentLoginUserId(HttpServletRequest request, HttpServletResponse response) throws RuntimeException {
        XxlSsoUser xxlUser = SsoWebLoginHelper.loginCheck(request, response);
        log.info("xxlUser null{}",xxlUser);
        if (xxlUser == null) {
            printSSONoLogInfo(request,response,"no xxlUser");
            log.info("xxlUser null");
            throw new UserFriendlyException(SsoConstants.SsoLoginFailErrorMessage).setErrorCode(SsoConstants.SsoLoginFailErrorCode).setHttpStatusCode(200);
        }
        String userId = xxlUser.getUserId();
        if (StringUtils.isBlank(userId)) {
            printSSONoLogInfo(request,response,"no userId");
            throw new UserFriendlyException(SsoConstants.SsoLoginFailErrorMessage).setErrorCode(SsoConstants.SsoLoginFailErrorCode).setHttpStatusCode(200);
        }
        return Long.parseLong(userId);
    }

    /**
     * 获取当前登录用户ID，不做登录校验。
     */
    public String getCurrentLoginUserIdRaw(HttpServletRequest request, HttpServletResponse response) {
        XxlSsoUser xxlUser = SsoWebLoginHelper.loginCheck(request, response);
        if (xxlUser == null) {
            return null;
        }
        return xxlUser.getUserId();
    }

}
