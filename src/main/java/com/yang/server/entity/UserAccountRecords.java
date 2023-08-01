package com.yang.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserAccountRecords {


    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("邮箱是否已发送，0未发送，1已发送")
    private Integer isMailbox;

    @ApiModelProperty("打卡方式。0.自动打卡，1手动")
    private Integer type;

    @ApiModelProperty("创建时间")
    private Date gmtCreate;

    @ApiModelProperty("更新时间")
    private Date gmtModify;

    @ApiModelProperty("0 未删除，1 已删除")
    private Integer isDelete;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("app类型。1工学云，2职校家园")
    private Integer appType;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("创建人id")
    private Long createId;

    @TableField(exist = false)
    @ApiModelProperty("创建人手机号")
    private String createPhone;


}
