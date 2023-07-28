package com.yang.auto.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel("账户信息")
public class AutoAccount {


    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("用户类型。0.管理员，1分级管理员")
    private Integer type;

    @ApiModelProperty("创建时间")
    private Date gmtCreate;

    @ApiModelProperty("更新时间")
    private Date gmtModify;

    @ApiModelProperty("0 未删除，1 已删除")
    private Integer isDelete;

    @ApiModelProperty("0系统创建（也就是管理员），其他用户id")
    private Long createId;

    @ApiModelProperty("账号状态，0正常，1已被禁用")
    private Integer accountStatus;

    @ApiModelProperty("打卡剩余天数(工学云) zhiClockDays二选一或者全部")
    private Integer gongClockDays;

    @ApiModelProperty("打卡剩余天数(职校家园) zhiClockDays二选一或者全部")
    private Integer zhiClockDays;




}
