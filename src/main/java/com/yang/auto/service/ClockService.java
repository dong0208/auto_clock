package com.yang.auto.service;

import com.yang.auto.entity.UserClock;
import com.yang.auto.util.HttpResult;

public interface ClockService {
    HttpResult userOperation(UserClock userClock);

    HttpResult userRecords(UserClock userClock);

    HttpResult userZhiClock(UserClock userClock);

    HttpResult gongxueyunVerify(String phon,String password);

    HttpResult zhiClockVerify(String phone,String password1,String deviceType);
}
