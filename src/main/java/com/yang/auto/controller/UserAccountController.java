package com.yang.auto.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yang.auto.entity.AutoAccount;
import com.yang.auto.entity.AutoUser;
import com.yang.auto.service.UserAccountService;
import com.yang.auto.util.HttpResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
@Slf4j
@ApiModel("用户账户控制类")
public class UserAccountController {


    @Resource
    private UserAccountService userAccountService;


    @ApiOperation("账户添加")
    @PostMapping("/insertAccount")
    public HttpResult insertAccount(@RequestBody AutoAccount account, HttpServletRequest request, HttpServletResponse response) {

        Assert.isTrue((account.getZhiClockDays() != null && account.getZhiClockDays() > 0) || (account.getGongClockDays() != null && account.getGongClockDays() > 0),"打卡剩余天数必填");
        Assert.isTrue(account.getPhone() != null ,"手机号必填");
        Assert.isTrue(account.getPassword() != null ,"密码必填");
        Assert.isTrue(account.getCreateId() != null ,"创建人必填");
        return userAccountService.insertAccount(account);
    }



    @ApiOperation("（管理员操作）账户修改，包括密码修改/账户状态/手机号，打卡剩余天数")
    @PostMapping("/updateAccount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "账户id",required = true),
            @ApiImplicitParam(name = "createId", value = "修改人",required = true),
            @ApiImplicitParam(name = "password", value = "密码",required = false),
            @ApiImplicitParam(name = "accountStatus", value = "账号状态，0正常，1已被禁用",required = false),
            @ApiImplicitParam(name = "zhiClockDays", value = "打卡剩余天数",required = false),
            @ApiImplicitParam(name = "gongClockDays", value = "打卡剩余天数",required = false)
    })
    public HttpResult updateAccount(Long id,Long createId,String password,Integer accountStatus,Integer zhiClockDays,Integer gongClockDays, HttpServletRequest request, HttpServletResponse response) {
        Assert.isTrue(createId != null ,"修改人必填");
        Assert.isTrue(id != null ,"账户id必填");
        AutoAccount account = new AutoAccount();
        account.setAccountStatus(accountStatus);
        account.setPassword(password);
        account.setCreateId(createId);
        account.setZhiClockDays(zhiClockDays);
        account.setGongClockDays(gongClockDays);
        account.setId(id);
        return userAccountService.updateAccount(account);
    }

    @ApiOperation("分页查询账户信息")
    @PostMapping("/selctAccountAll")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "管理员id",required = true),
            @ApiImplicitParam(name = "pageNo", value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize", value = "条数",required = true),
            @ApiImplicitParam(name = "phone", value = "手机号",required = false),
    })
    public HttpResult<IPage<AutoAccount>> selctAccountAll(Long id, Integer pageNo, Integer pageSize, String phone, HttpServletRequest request, HttpServletResponse response) {
        log.info("selctAccountAll:id:{},pageNo:{},pageSize:{},phone:{}",id,pageNo,pageSize,phone);
        Assert.isTrue(id != null ,"管理员id必填");
        Assert.isTrue(pageNo != null ,"页码必填");
        Assert.isTrue(pageNo != null ,"条数必填");
        return userAccountService.selctAccountAll(id,pageNo,pageSize,phone);
    }

    @ApiOperation("（查询账号信息")
    @PostMapping("/selectInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "账户id",required = true),
    })
    public HttpResult selectInfo(Long id, HttpServletRequest request, HttpServletResponse response) {
        Assert.isTrue(id != null ,"账户id必填");
        return userAccountService.selectInfo(id);
    }

    @ApiOperation("添加用户信息")
    @PostMapping("/insertUser")
    public HttpResult insertUser(@RequestBody AutoUser autoUser, HttpServletRequest request, HttpServletResponse response) {
        Assert.isTrue(autoUser.getPhone() != null ,"phone必填");
        Assert.isTrue(autoUser.getPassword() != null ,"密码必填");
        Assert.isTrue(autoUser.getLongitude() != null ,"经度必填");
        Assert.isTrue(autoUser.getLatitude() != null ,"维度必填");
        Assert.isTrue(autoUser.getAddress() != null && autoUser.getCity() != null && autoUser.getCountry() != null && autoUser.getProvince() != null
                && autoUser.getArea() != null && autoUser.getCreateId() != null && autoUser.getAppType() != null
                ,"参数");
        return userAccountService.insertUser(autoUser);
    }

    @ApiOperation("编辑用户信息")
    @PostMapping("/updateUser")
    public HttpResult updateUser(@RequestBody AutoUser autoUser, HttpServletRequest request, HttpServletResponse response) {
        Assert.isTrue(autoUser.getPhone() != null ,"phone必填");
        Assert.isTrue(autoUser.getPassword() != null ,"密码必填");
        Assert.isTrue(autoUser.getLongitude() != null ,"经度必填");
        Assert.isTrue(autoUser.getLatitude() != null ,"维度必填");
        Assert.isTrue(autoUser.getAddress() != null && autoUser.getCity() != null && autoUser.getCountry() != null && autoUser.getProvince() != null
                        && autoUser.getArea() != null && autoUser.getCreateId() != null && autoUser.getAppType() != null && autoUser.getId() != null
                ,"参数");
        return userAccountService.updateUser(autoUser);
    }


    @ApiOperation("分页查询用户信息")
    @PostMapping("/selctUserAll")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "管理员id",required = true),
            @ApiImplicitParam(name = "pageNo", value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize", value = "条数",required = true),
            @ApiImplicitParam(name = "phone", value = "手机号",required = false),
    })
    public HttpResult<IPage<AutoUser>> selctUserAll(Long id, Integer pageNo, Integer pageSize, String phone, HttpServletRequest request, HttpServletResponse response) {
        log.info("selctUserAll:id:{},pageNo:{},pageSize:{},phone:{}",id,pageNo,pageSize,phone);
        Assert.isTrue(id != null ,"管理员id必填");
        Assert.isTrue(pageNo != null ,"页码必填");
        Assert.isTrue(pageNo != null ,"条数必填");
        return userAccountService.selctUserAll(id,pageNo,pageSize,phone);
    }

    @ApiOperation("（查询用户信息")
    @PostMapping("/selectUserInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id",required = true),
    })
    public HttpResult selectUserInfo(Long id, HttpServletRequest request, HttpServletResponse response) {
        Assert.isTrue(id != null ,"账户id必填");
        return userAccountService.selectUserInfo(id);
    }

    @ApiOperation("启动用户打卡功能")
    @PostMapping("/activateUserClock")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id",required = true),
            @ApiImplicitParam(name = "enable", value = "开启或关闭",required = true,type = "boolean"),
    })
    public HttpResult activateUserClock(Long id, Boolean enable,HttpServletRequest request, HttpServletResponse response) {
        Assert.isTrue(id != null ,"账户id必填");
        return userAccountService.activateUserClock(id,enable);
    }



    @ApiOperation("用户打卡")
    @PostMapping("/handleClock")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id",required = true),
            @ApiImplicitParam(name = "createId", value = "创建人id",required = true),
    })
    public HttpResult handleClock(Long id,Long createId, HttpServletRequest request, HttpServletResponse response) {
        Assert.isTrue(id != null && createId != null ,"账户id必填");
        return userAccountService.handleClock(id,createId);
    }

}
