package com.yang.server.service;

import com.yang.server.entity.UserClock;
import com.yang.server.util.HttpResult;

public interface ClockService {
    HttpResult userOperation(UserClock userClock);

    HttpResult userRecords(UserClock userClock);

    HttpResult userZhiClock(UserClock userClock);

    HttpResult gongxueyunVerify(String phon,String password);

    HttpResult zhiClockVerify(String phone,String password1,String deviceType);
}
