package com.yang.auto.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel("用户打卡信息")
public class AutoUser {

    private Long id;

    @ApiModelProperty("是否启用该用户的打卡（true或false)")
    private boolean enable;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区/县")
    private String area;

    @ApiModelProperty("详细地址，如果你打卡的时候中间带的有·这个符号你也就手动加上，这里填什么，打卡后工学云就会显示你填的内容（工学云默认·这个符号左右都会有一个空格）")
    private String address;

    @ApiModelProperty("打卡位置精度,通过坐标拾取来完成(仅需精确到小数点后6位)")
    private String longitude;

    @ApiModelProperty("打卡位置纬度,通过坐标拾取来完成(仅需精确到小数点后6位)")
    private String latitude;

    @ApiModelProperty("定时打卡数组格式[1,2,3,4]")
    private String weeks;

    @ApiModelProperty("创建时间")
    private Date gmtCreate;

    @ApiModelProperty("更新时间")
    private Date gmtModify;

    @ApiModelProperty("0 未删除，1 已删除")
    private Integer isDelete;

    @ApiModelProperty("账户id")
    private Long createId;

    @ApiModelProperty("定时打卡上午，0-不开启，1-开启（和clockPm二选一）")
    private Integer clockAm;

    @ApiModelProperty("定时打卡下午，0-不开启，1-开启（和clockAm二选一）")
    private Integer clockPm;

    @ApiModelProperty("用户类型1，工学云，2，职校家园")
    private Integer appType;

    @ApiModelProperty("剩余打卡天数")
    private Integer clockDays;

    @ApiModelProperty("是否修改密码，编辑使用")
    private boolean updatePassword;


}
