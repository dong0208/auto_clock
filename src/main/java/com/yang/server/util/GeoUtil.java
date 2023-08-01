package com.yang.server.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class GeoUtil {

    private static String geoCoderUrl = "https://web.hknet-inc.com/store/area/getCity";

    /**
     * 自建逆地址解析
     *
     * @param lat
     * @param lng
     * @return
     */
    public static String getNativeMapGeocoder(String lat, String lng) {
        try {
            String path = geoCoderUrl + "?lat=" + lat + "&lng=" + lng;
            return JSON.parseObject(httpGet(path)).getString("entry");
        } catch (Exception e) {
            return "上海市";
        }
    }

    public static String getQqMapGeocoder(String lat, String lng, String key, String secret) {
        String path = "/ws/geocoder/v1/?key=" + key + "&location=" + lat + "," + lng + secret;
        String sign = DigestUtils.md5Hex(path);
        return httpGet("https://apis.map.qq.com/ws/geocoder/v1/?key=" + key + "&location=" + lat + "," + lng + "&sig=" + sign);
    }

    public static String getQqMapGeocoderWithPoi(String lat, String lng, String key, String secret) {
        String path = "/ws/geocoder/v1/?get_poi=1&key=" + key + "&location=" + lat + "," + lng + secret;
        String sign = DigestUtils.md5Hex(path);
        return httpGet("https://apis.map.qq.com/ws/geocoder/v1/?get_poi=1&key=" + key + "&location=" + lat + "," + lng + "&sig=" + sign);
    }

    public static String getQqMapHitPoi(String lat, String lng, String key, String secret, String searchkey, String city) {
        String path = "/ws/place/v1/suggestion/?key=" + key + "&keyword=" + searchkey + "&location=" + lat + "," + lng + "&region=" + city + secret;
        String sign = DigestUtils.md5Hex(path);
        return httpGet("https://apis.map.qq.com/ws/place/v1/suggestion/?key=" + key + "&keyword=" + searchkey + "&location=" + lat + "," + lng + "&region=" + city + "&sig=" + sign);
    }


    /**
     * 获取步行距离(高德)
     *
     * @param origin
     * @param destination
     * @return
     */
    public static double directionWalking(String origin, String destination) {

        String url = "https://restapi.amap.com/v3/direction/walking?origin=" + origin + "&destination=" + destination + "&key=8aa83d6871d2e0282f5ae02e0b865d38";
        String result = httpGet(url);

        try {
            JSONObject resultObject = JSON.parseObject(result);
            log.info("directionWalking, resultObject={}", JSON.toJSONString(resultObject));
            if ("0".equals(String.valueOf(resultObject.get("status")))) {
                if (resultObject.containsKey("route") && resultObject.getJSONObject("route").containsKey("paths")) {
                    JSONArray array = resultObject.getJSONObject("route").getJSONArray("paths");
                    int meter = Integer.MAX_VALUE;
                    for (Object object : array) {
                        JSONObject des = (JSONObject) object;
                        if (des.containsKey("distance") && des.getInteger("distance") < meter) {
                            meter = des.getInteger("distance");
                        }
                    }
                    return meter;
                }
            }
        } catch (Exception e) {
            log.info("directionWalking exception={}" + e);
        }

        return Integer.MAX_VALUE;
    }


    /**
     * Http Get 请求
     *
     * @param urlString
     * @return responseString
     */
    protected static String httpGet(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2 * 1000);
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (Exception e) {
            log.info("httpGet error url:{} exception:{}", urlString, e.toString());
        }
        return result.toString();
    }
}
