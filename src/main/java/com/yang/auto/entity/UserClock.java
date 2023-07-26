package com.yang.auto.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("用户打卡信息")
public class UserClock {

    @ApiModelProperty("是否启用该用户的打卡（true或false)")
    private Boolean enable;

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

    @ApiModelProperty("android 或 ios")
    private String type;

    @ApiModelProperty("详细地址，如果你打卡的时候中间带的有·这个符号你也就手动加上，这里填什么，打卡后工学云就会显示你填的内容（工学云默认·这个符号左右都会有一个空格）")
    private String address;

    @ApiModelProperty("打卡位置精度,通过坐标拾取来完成(仅需精确到小数点后6位)")
    private String longitude;

    @ApiModelProperty("打卡位置纬度,通过坐标拾取来完成(仅需精确到小数点后6位)")
    private String latitude;

    @ApiModelProperty("打卡结果微信推送，微信推送使用的是pushPlus，请到官网绑定微信(传送门)，然后在发送消息里面把你的token复制出来粘贴到pushKey这项")
    private String pushKey;

    @ApiModelProperty("打卡类型，上班-START，下班-END")
    private String signType;

    @ApiModelProperty("(职家校园)设备类型,格式:手机品牌英文名称|手机代号|安卓系统版本,例如:Xiaomi|Mi 13|13")
    private String deviceType;

}
