package com.yang.server.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserClockDay {


    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("剩余打卡天数")
    private Integer clockDays;

    @ApiModelProperty("变更天数值")
    private Integer changeDays;

    @ApiModelProperty("app类型。1工学云，2职校家园")
    private Integer appType;

    @ApiModelProperty("创建时间")
    private Date gmtCreate;

    @ApiModelProperty("更新时间")
    private Date gmtModify;

    @ApiModelProperty("0 未删除，1 已删除")
    private Integer isDelete;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("0系统创建，帐户id")
    private Long createId;

    @ApiModelProperty("用户类型，1-账户，2-用户")
    private Integer type;


}
