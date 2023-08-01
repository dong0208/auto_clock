package com.yang.server.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushUtil {

    public static void send(String token,String text) {
        String title = "打卡消息通知";  //消息的标题
        String content = text+"<br/><img src='http://www.pushplus.plus/doc/img/push.png' />";  //消息的内容,包含文字、换行和图片
        String url = "https://www.pushplus.plus/send?title=" + title + "&content=" + content + "&token=" + token;

        //服务器发送Get请求，接收响应内容
        String response = HttpUtil.sendGet(url);
        //把返回的字符串结果变成对象
        JSONObject jsonObject = JSONObject.parseObject(response);
        log.info("send:{}",jsonObject.toString());
    }

    public static void main(String[] args) {
        send("232","232");
    }
}
