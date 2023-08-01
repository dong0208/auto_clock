package com.yang.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yang.server.util.HttpResult;
import com.yang.server.util.GeoUtil;
import com.yang.server.util.UserIdHelper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


@RestController
@RequestMapping("/area")
@Slf4j
@ApiModel("地图")
public class AreaInfoController {

    @Resource
    private UserIdHelper userIdHelper;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "lat", value = "经度"),
            @ApiImplicitParam(name = "lng", value = "纬度")
    })
    @ApiOperation("逆地址转换(腾讯地图)")
    @GetMapping("/geocoder")
    public HttpResult geocoder(
            @RequestParam("lat") String lat,
            @RequestParam("lng") String lng,
            @RequestParam(value = "companyId",required = false) Long companyId,
            HttpServletRequest request, HttpServletResponse response
    ) {
        // 文档地址 https://lbs.qq.com/service/webService/webServiceGuide/webServiceGcoder
        // 请求案例 https://apis.map.qq.com/ws/geocoder/v1/?location=39.984154,116.307490&key=QRKBZ-QRYHJ-FMUFR-KHOOM-O7E66-FAF72
        // 加签方案 https://lbs.qq.com/FAQ/server_faq.html#3
        // 签名key fWM4l8hOxacCWEDiah7VP0krwsOCxrRF
        // String userId = userIdHelper.getCurrentLoginUserIdRaw(request, response);
        String userId = "";
        try {
            userId = userIdHelper.getCurrentLoginUserIdRaw(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getGeoCoder(lat, lng, userId);
    }


    private HttpResult getGeoCoder(@RequestParam("lat") String lat, @RequestParam("lng") String lng, String userId) {
        String result = GeoUtil.getQqMapGeocoder(lat, lng, "QRKBZ-QRYHJ-FMUFR-KHOOM-O7E66-FAF72", "fWM4l8hOxacCWEDiah7VP0krwsOCxrRF");// 闪电接龙的Key，10W额外配额
        JSONObject resultObject = JSON.parseObject(result);
        if ("0".equals(String.valueOf(resultObject.get("status")))) {
            return HttpResult.success(resultObject.get("result"));
        } else {
            result = GeoUtil.getQqMapGeocoder(lat, lng, "TYEBZ-J44C4-R4UUM-XMNXZ-53R57-TXBC3", "LGTWrx7YgMnpqjYg4WvVd29frypY1q");// 断帅个人Key,配额较小
            resultObject = JSON.parseObject(result);
            if ("0".equals(String.valueOf(resultObject.get("status")))) {
                return HttpResult.success(resultObject.get("result"));
            }
        }
        // 未获取到，返回默认
        HashMap<String,Object> defaultReturn = new HashMap<>();
        HashMap<String,Object> ad_info = new HashMap<>();
        ad_info.put("city",GeoUtil.getNativeMapGeocoder(lat,lng));
        defaultReturn.put("ad_info",ad_info);
        return HttpResult.success(defaultReturn);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "地址关键词"),
    })
    @ApiOperation("输入关键字提示地理位置(高德)")
    @GetMapping("/assistant/inputtips")
    public HttpResult assistantInputtips(
            @RequestParam("keyword") String keyword, HttpServletRequest request, HttpServletResponse response) {
        // 文档地址 https://lbs.amap.com/api/webservice/guide/api/inputtips
        try {

            String result = httpGet("https://restapi.amap.com/v3/assistant/inputtips?&keywords="+keyword+"&key=8aa83d6871d2e0282f5ae02e0b865d38" );
            JSONObject resultObject = JSON.parseObject(result);
            log.info("searchAddressAp, resultObject={}", JSON.toJSONString(resultObject));
            if ("1".equals(String.valueOf(resultObject.get("status")))) {
                return HttpResult.success(resultObject);
            }
            String info = resultObject.getString("info");
            return HttpResult.failure(info);

        } catch (Exception e) {
            log.info("assistantInputtips , keyword= {},e={}",keyword,e);
        }

        return HttpResult.failure("系统错误");
    }


    /**
     * Http Get 请求
     * @param urlString
     * @return responseString
     */
    protected static String httpGet(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2*1000);
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (Exception e) {
            log.info("httpGet error url:{} exception:{}",urlString,e.toString());
        }
        return result.toString();
    }


}
