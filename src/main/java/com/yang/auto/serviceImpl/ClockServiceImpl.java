package com.yang.auto.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yang.auto.config.Aes;
import com.yang.auto.entity.UserClock;
import com.yang.auto.service.ClockService;
import com.yang.auto.util.EncodeUtil;
import com.yang.auto.util.HttpResult;
import com.yang.auto.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

@Service
@Slf4j
public class ClockServiceImpl implements ClockService {



    final  static String loginURL = "https://api.moguding.net:9000/session/user/v3/login";
    final  static String signInURL = "https://api.moguding.net:9000/attendence/clock/v2/save";
    final  static String planIdURL = "https://api.moguding.net:9000/practice/plan/v3/getPlanByStu";
    final  static String recordsUrl = "https://api.moguding.net:9000/attendence/clock/v1/listSynchro";

    final  static String zhiSignInURL = "https://sxbaapp.zcj.jyt.henan.gov.cn/interface/clockindaily20220827.ashx";
    final  static String zhiloginURL = "https://sxbaapp.zcj.jyt.henan.gov.cn/interface/relog.ashx";
    final  static String zhiTokenURL = "https://sxbaapp.zcj.jyt.henan.gov.cn/interface/token.ashx";






    @Override
    public HttpResult userOperation(UserClock userClock) {

        try {
            String password = EncodeUtil.base64Decoder(userClock.getPassword());
            String mi = String.valueOf(System.currentTimeMillis());
            String Json = HttpUtil.PostRequest(new URL(loginURL), "", "", "", "{\"password\":\""+ Aes.encrypt(password,"23DbtQHR2UMbH6mJ")+"\",\"t\":\""+Aes.encrypt(mi,"23DbtQHR2UMbH6mJ")+"\",\"phone\":\""+Aes.encrypt(userClock.getPhone(),"23DbtQHR2UMbH6mJ")+"\",\"loginType\":\"android\",\"uuid\":\"\"}");
            log.info("工学云登录接口返回信息{},{}",Json,userClock.getPhone());

            String userId = JSONObject.parseObject(Json).getJSONObject("data").getString("userId");

            String token = JSONObject.parseObject(Json).getJSONObject("data").getString("token");

            String str = userId + "student" + "3478cbbc33f84bd00d75d7dfa69e0daa";

            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            // 一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            String sign = new BigInteger(1, md.digest()).toString(16);

            Json = HttpUtil.PostRequest(new URL(planIdURL),token,sign,"student","{\"state\":\"\"}");
            log.info("工学云获取planId返回信息{},{}",Json,userClock.getPhone());
            String planId = JSONObject.parseObject(Json).getJSONArray("data").getJSONObject(0).getString("planId");


            String str1="Android"+userClock.getSignType()+planId+userId+userClock.getAddress()+"3478cbbc33f84bd00d75d7dfa69e0daa";

            // 生成一个MD5加密计算摘要
            MessageDigest md2 = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md2.update(str1.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            // 一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            sign = new BigInteger(1, md2.digest()).toString(16);

            /**这条JSon是用于发送签到请求的*/
            String SignInJson = "{\"country\":\""+userClock.getCountry()+"\"," +
                    "\"address\":\""+userClock.getAddress()+"\"," +
                    "\"province\":\""+userClock.getProvince()+"\"," +
                    "\"city\":\""+userClock.getCity()+"\"," +
                    "\"latitude\":\""+userClock.getLatitude()+"\"," +
                    "\"description\":\"\"," +
                    "\"planId\":\""+planId+"\"," +
                    "\"type\":\""+userClock.getSignType()+"\"," +
                    "\"device\":\"Android\"," +
                    "\"longitude\":\""+userClock.getLongitude()+"\"" +
                    "}";

            String loginReturn = HttpUtil.PostRequest(new URL(signInURL), token, sign, "student", SignInJson);
            log.info("工学云签到结果返回信息{},{}",loginReturn,userClock.getPhone());
            JSONObject jsonObject = JSON.parseObject(loginReturn);

            if (("200").equals(jsonObject.getString("code"))){
                return HttpResult.success("打卡成功");
            }else{
                return HttpResult.failure(jsonObject.getString("msg"));
            }
        }catch (Exception e){
            log.error("工学云签到异常",e);
            return HttpResult.failure("网络不稳定引发打卡失败");
        }

    }

    @Override
    public HttpResult userRecords(UserClock userClock) {
        try {
            String mi = String.valueOf(System.currentTimeMillis());
            String Json = HttpUtil.PostRequest(new URL(loginURL), "", "", "", "{\"password\":\"" + Aes.encrypt(userClock.getPassword(), "23DbtQHR2UMbH6mJ") + "\",\"t\":\"" + Aes.encrypt(mi, "23DbtQHR2UMbH6mJ") + "\",\"phone\":\"" + Aes.encrypt(userClock.getPhone(), "23DbtQHR2UMbH6mJ") + "\",\"loginType\":\"android\",\"uuid\":\"\"}");
            log.info("查询打卡记录：工学云登录接口返回信息{},手机号{}", Json, userClock.getPhone());
            String token = JSONObject.parseObject(Json).getJSONObject("data").getString("token");
            JSONObject jsonObject = new JSONObject();
            jsonObject.fluentPut("t", Aes.encrypt(mi, "23DbtQHR2UMbH6mJ"));
            String Json2 = HttpUtil.PostRequest(new URL(recordsUrl), token, "", "", jsonObject.toString());
            log.info("工学云签到记录结果返回信息{},{}", Json2, userClock.getPhone());
            JSONObject jsonObject2 = JSON.parseObject(Json2);
            if ("200".equals(jsonObject2.getString("code"))){
                return HttpResult.success("获取打卡记录成功");
            }
            return HttpResult.failure(jsonObject2.getString("msg"));

        } catch (Exception e) {
            log.error("工学云签到记录异常", e);
            return HttpResult.failure("网络不稳定引发打卡失败");
        }
    }

    @Override
    public HttpResult userZhiClock(UserClock userClock) {
        log.info("userZhiClock:userClock{}",JSONObject.toJSONString(userClock));
        try {
            //获取token
            String json = HttpUtil.PostZhiRequest(new URL(zhiTokenURL),null,null,null,userClock.getDeviceType());
            log.info("职家获取用户token:json:{},phon:{}",json,userClock.getPhone());
            JSONObject jsonObject = JSON.parseObject(json);
            if (!"1001".equals(jsonObject.getString("code"))){
                return HttpResult.failure(jsonObject.getString("msg"));
            }
            String token = jsonObject.getJSONObject("data").getString("token");
            log.info("职家获取用户token:token:{}",token);
            //登录
            String password2 = EncodeUtil.base64Decoder(userClock.getPassword());
            String password = getMd5(password2);
            if (password == null){
                return HttpResult.failure("密码加密失败");
            }

            JSONObject jsonData = new JSONObject();
            jsonData.putIfAbsent("phone",userClock.getPhone());
            jsonData.putIfAbsent("password",password);
            jsonData.putIfAbsent("dtype",6);
            jsonData.putIfAbsent("dToken",getRandomNumber());
            String sign = getMd5(jsonData.toJSONString()+token);
            String jsonLogin = HttpUtil.PostZhiRequest(new URL(zhiloginURL),null,sign,jsonData.toJSONString(),null);
            log.info("职家获取用户登录信息:json:{},phon:{}",jsonLogin,userClock.getPhone());
            if (jsonLogin == null){
                return HttpResult.failure("职校家园请求失败，可能是网络不稳定导致，请联系管理员");
            }
            JSONObject jsonObjectLogin = JSONObject.parseObject(jsonLogin);
            if (!"1001".equals(jsonObjectLogin.getString("code"))){
                return HttpResult.failure("职校家园登录失败"+jsonObjectLogin.getString("msg"));
            }
            //打卡
            String uid = jsonObjectLogin.getJSONObject("data").getString("uid");
            JSONObject data = new JSONObject();
            data.putIfAbsent("dtype",1);
            data.putIfAbsent("uid",uid);
            data.putIfAbsent("address",userClock.getAddress());
            data.putIfAbsent("phonetype",userClock.getDeviceType());
            data.putIfAbsent("probability",-1);
            data.putIfAbsent("longitude",userClock.getLongitude());
            data.putIfAbsent("latitude",userClock.getLatitude());
            String sing = getMd5(data.toJSONString()+token);
            String jsonSave = HttpUtil.PostZhiRequest(new URL(zhiSignInURL),null,sing,data.toJSONString(),null);
            log.info("职家获取用户打卡信息:jsonSave:{},phon:{}",jsonSave,userClock.getPhone());
            if (jsonSave == null){
                return HttpResult.failure("职校家园打卡请求失败，可能是网络不稳定导致，请联系管理员");
            }
            JSONObject jsonObject1 = JSONObject.parseObject(jsonSave);
            if (!"1001".equals(jsonObject1.getString("code"))){
                return HttpResult.failure("职校家园登录失败"+jsonObject1.getString("msg"));
            }
            return HttpResult.success("打卡成功");
        }catch (Exception e){
            log.error("职校家园签到异常",e);
            return HttpResult.failure("网络不稳定引发打卡失败");
        }

    }

    public String getMd5(String sSrc){
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(sSrc.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            // 一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            String md5Str = new BigInteger(1, md.digest()).toString(16);
            return md5Str;
        }catch (Exception e){
            log.info("Md5加密失败，e",e);
            return null;
        }

    }

    private static final String SYMBOLS = "0123456789abcdef"; // 数字和26个字母组成
    private static final Random RANDOM = new SecureRandom();

    public static String getRandomNumber() {
        char[] nonceChars = new char[32];  //指定长度为32位/自己可以要求设置

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }


    @Override
    public HttpResult gongxueyunVerify(String phon,String password) {

        try {
            String mi = String.valueOf(System.currentTimeMillis());
            String Json = HttpUtil.PostRequest(new URL(loginURL), "", "", "", "{\"password\":\""+ Aes.encrypt(password,"23DbtQHR2UMbH6mJ")+"\",\"t\":\""+Aes.encrypt(mi,"23DbtQHR2UMbH6mJ")+"\",\"phone\":\""+Aes.encrypt(phon,"23DbtQHR2UMbH6mJ")+"\",\"loginType\":\"android\",\"uuid\":\"\"}");
            log.info("gongxueyunVerify:,{},{}",Json,phon);
            JSONObject jsonObject = JSON.parseObject(Json);

            if (("200").equals(jsonObject.getString("code"))){
                return HttpResult.success("校验成功");
            }else{
                return HttpResult.failure(jsonObject.getString("msg"));
            }

        }catch (Exception e){
            log.error("gongxueyunVerify,{}",e);
            return HttpResult.failure("校验失败");
        }

    }

    @Override
    public HttpResult zhiClockVerify(String phone,String password1,String deviceType) {
        log.info("userZhiClock:phone{},password1{},deviceType{}", phone,password1,deviceType);
        try {
            //获取token
            String json = HttpUtil.PostZhiRequest(new URL(zhiTokenURL), null, null, null, deviceType);
            log.info("zhiClockVerify职家获取用户token:json:{},phon:{}", json, phone);
            JSONObject jsonObject = JSON.parseObject(json);
            if (!"1001".equals(jsonObject.getString("code"))) {
                return HttpResult.failure(jsonObject.getString("msg"));
            }
            String token = jsonObject.getJSONObject("data").getString("token");
            log.info("zhiClockVerify职家获取用户token:token:{}", token);
            //登录
            String password = getMd5(password1);
            if (password == null) {
                return HttpResult.failure("密码加密失败");
            }
            JSONObject jsonData = new JSONObject();
            jsonData.putIfAbsent("phone", phone);
            jsonData.putIfAbsent("password", password);
            jsonData.putIfAbsent("dtype", 6);
            jsonData.putIfAbsent("dToken", getRandomNumber());
            String sign = getMd5(jsonData.toJSONString() + token);
            String jsonLogin = HttpUtil.PostZhiRequest(new URL(zhiloginURL), null, sign, jsonData.toJSONString(), null);
            log.info("zhiClockVerify职家获取用户登录信息:json:{},phon:{}", jsonLogin, phone);
            if (jsonLogin == null) {
                return HttpResult.failure("职校家园请求失败，可能是网络不稳定导致，请联系管理员");
            }
            JSONObject jsonObjectLogin = JSONObject.parseObject(jsonLogin);
            if (!"1001".equals(jsonObjectLogin.getString("code"))) {
                return HttpResult.failure("职校家园登录失败" + jsonObjectLogin.getString("msg"));
            }
            return HttpResult.success();
        } catch (Exception e) {
            log.error("zhiClockVerify职校家园签到异常", e);
            return HttpResult.failure("网络不稳定引发打卡失败");
        }
    }

}
