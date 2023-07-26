package com.yang.auto.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {



    public final static String Accept_Language="zh-CN,zh;q=0.8";
    public final static String user_agent_value="Mozilla/5.0 (Linux; Android 7.0; HTC M9e Build/EZG0TF) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.1566.54 Mobile Safari/537.36";
    public final static String Content_Type="application/json; charset=UTF-8";
    public final static String Host="api.moguding.net:9000";
    public final static String Accept_Encoding="";
    public final static String Cache_Control="no-cache";

    public static String PostRequest(URL Url, String token, String sign, String roleKey, String jsonStr){
        StringBuffer s = new StringBuffer();
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            conn = (HttpURLConnection) Url.openConnection();
            conn.setRequestMethod("POST");

            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);

            conn.setRequestProperty("Host", Host);
            conn.setRequestProperty("Accept-Language", Accept_Language);
            conn.setRequestProperty("User-Agent", user_agent_value);
            conn.setRequestProperty("Sign", sign);
            conn.setRequestProperty("Authorization", token);
            conn.setRequestProperty("roleKey", roleKey);
            conn.setRequestProperty("Content-Type", Content_Type);
            conn.setRequestProperty("Accept-Encoding", Accept_Encoding);
            conn.setRequestProperty("Cache-Control", Cache_Control);
            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream());

            out.write(jsonStr);
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null){
                    result.append(line);
                }
            }else{

                s.append("ResponseCode is an error code:" + conn.getResponseCode());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return result.toString();
    }


    public final static String zhi_os="android";
    public final static String zhi_appVersion="51";
    public final static String zhi_Sign="Sign";
    public final static String zhi_cl_ip="192.168.1.6";
    public final static String zhi_UserAgent="okhttp/3.14.9";
    public final static String zhi_content_type = "application/json;charset=utf-8";


    public static String PostZhiRequest(URL Url, String token, String sign, String roleKey, String jsonStr,String phon){
        StringBuffer s = new StringBuffer();
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            conn = (HttpURLConnection) Url.openConnection();
            conn.setRequestMethod("POST");

            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);

            conn.setRequestProperty("os", zhi_os);
            if (phon != null){
                conn.setRequestProperty("phone", phon);
            }
            conn.setRequestProperty("appVersion", zhi_appVersion);
            if (sign != null){
                conn.setRequestProperty("Sign", sign);
            }else {
                conn.setRequestProperty("Sign", zhi_Sign);
            }
            conn.setRequestProperty("cl_ip", zhi_cl_ip);
            conn.setRequestProperty("User-Agent", zhi_UserAgent);
            conn.setRequestProperty("Content-Type", zhi_content_type);

            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream());
            if (jsonStr != null){
                out.write(jsonStr);
            }
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null){
                    result.append(line);
                }
            }else{

                s.append("ResponseCode is an error code:" + conn.getResponseCode());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return result.toString();
    }

}
