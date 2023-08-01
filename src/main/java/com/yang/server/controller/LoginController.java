package com.yang.server.controller;

import com.yang.server.entity.AutoAccount;
import com.yang.server.entity.XxlSsoUser;
import com.yang.server.service.LoginService;
import com.yang.server.util.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/web")
@Slf4j
@ApiModel("登录")
public class LoginController {


    @Resource
    private LoginService loginService;

   // @ApiIgnore
    @ApiOperation("手机号+密码 登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
            @ApiImplicitParam(name = "type", value = "登录类型。0-管理员，1-分级管理员", required = false),
    })
    @PostMapping("/login")
    public HttpResult login(String phone, String password, String type, HttpServletRequest request, HttpServletResponse response) {
        log.info("LoginController phone:{}, type:{}", phone, type);
        // valid login
        AutoAccount account = loginService.doLoginUser(phone, password, type);
        SsoWebLoginHelper.removeSessionIdByCookie(request, response);
        // 1、make xxl-sso user
        XxlSsoUser xxlUser = new XxlSsoUser();
        xxlUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
        xxlUser.setUserId(String.valueOf(account.getId()));
        xxlUser.setExpireMinite(SsoLoginStore.getRedisExpireMinite());
        xxlUser.setExpireFreshTime(System.currentTimeMillis());
        xxlUser.setMobilePhone(account.getPhone());
        xxlUser.setCreateId(account.getCreateId());
        xxlUser.setType(account.getType());
        xxlUser.setGongClockDays(account.getGongClockDays());
        xxlUser.setZhiClockDays(account.getZhiClockDays());
        xxlUser.setAccountStatus(account.getAccountStatus());
        // 2、generate sessionId + storeKey
        String sessionId = SsoSessionIdHelper.makeSessionId(xxlUser);
        // 3、login, store storeKey
        SsoTokenLoginHelper.login(response, sessionId, xxlUser);
        xxlUser.setSessionId(sessionId);
        // 4、return sessionId
        return HttpResult.success(xxlUser);
    }



    @ApiOperation("sessionId获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sessionId", value = "sessionId"),
    })
    @PostMapping("/getUserInfo")
    public HttpResult getUserInfo(String sessionId, HttpServletRequest request, HttpServletResponse response) {
        // logout, remove storeKey
        XxlSsoUser user = SsoTokenLoginHelper.getUserFromRedis(sessionId);
        if (user != null) {
            return HttpResult.success(user);
        }
        return HttpResult.failure("找不到用户");
    }

    @ApiOperation("登出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sessionId", value = "sessionId"),
    })
    @PostMapping("/logout")
    public HttpResult logout(String sessionId, HttpServletRequest request, HttpServletResponse response) {
        // logout, remove storeKey
        SsoTokenLoginHelper.logout(sessionId);
        SsoWebLoginHelper.logout(request, response);
        return HttpResult.success();
    }

    @ApiOperation("登陆检查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sessionId", value = "sessionId"),
    })
    @PostMapping("/logincheck")
    public HttpResult logincheck(String sessionId, HttpServletRequest request, HttpServletResponse response) {
        // logout
        XxlSsoUser xxlUser;
        if (StringUtils.isNotBlank(sessionId)) {
            xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        } else {
            xxlUser = SsoWebLoginHelper.loginCheck(request, response);
        }
        if (xxlUser == null) {
            UserFriendlyException exception = new UserFriendlyException(SsoConstants.SsoLoginFailErrorMessage);
            exception.setErrorCode(SsoConstants.SsoLoginFailErrorCode);
            exception.setHttpStatusCode(200);
            throw exception;
        }
        return HttpResult.success(xxlUser);
    }

    @ApiOperation("修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "登陆人id"),
            @ApiImplicitParam(name = "password", value = "密码"),
    })
    @PostMapping("/updatePassword")
    public HttpResult logincheck(String id,String password, HttpServletRequest request, HttpServletResponse response) {
        Assert.isTrue(id != null && password != null,"参数错误");
        return loginService.updatePassWord(id,password);
    }




}
