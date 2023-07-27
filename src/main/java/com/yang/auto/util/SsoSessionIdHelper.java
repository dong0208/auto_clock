package com.yang.auto.util;


import com.yang.auto.entity.XxlSsoUser;

/**
 * make client sessionId
 *
 *      client: cookie = [userid#version]
 *      server: redis
 *                  key = [userid]
 *                  value = user (user.version, valid this)
 *
 * //   group         The same group shares the login status, Different groups will not interact
 *
 * @author xuxueli 2018-11-15 15:45:08
 */

public class SsoSessionIdHelper {


    /**
     * make client sessionId
     *
     * @param xxlSsoUser
     * @return
     */
    public static String makeSessionId(XxlSsoUser xxlSsoUser){
        String storeKey = xxlSsoUser.getUserId();
        //处理多小程序登录拆分，避免篡改
        storeKey = storeKey+"#"+xxlSsoUser.getMobilePhone().replace("_","%");
        return storeKey.concat("_").concat(xxlSsoUser.getVersion());
    }

    /**
     * parse storeKey from sessionId
     *
     * @param sessionId
     * @return
     */
    public static String parseStoreKey(String sessionId) {
        if (sessionId!=null && sessionId.contains("_")) {
            String[] sessionIdArr = sessionId.split("_");
            if (sessionIdArr.length==2
                    && sessionIdArr[0]!=null
                    && sessionIdArr[0].trim().length()>0) {
                return sessionIdArr[0].trim();
            }
        }
        return null;
    }

    /**
     * parse version from sessionId
     *
     * @param sessionId
     * @return
     */
    public static String parseVersion(String sessionId) {
        if (sessionId!=null && sessionId.indexOf("_")>-1) {
            String[] sessionIdArr = sessionId.split("_");
            if (sessionIdArr.length==2
                    && sessionIdArr[1]!=null
                    && sessionIdArr[1].trim().length()>0) {
                String version = sessionIdArr[1].trim();
                return version;
            }
        }
        return null;
    }

}
