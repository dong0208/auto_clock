package com.yang.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yang.server.entity.AutoAccount;
import com.yang.server.entity.UserAccountRecords;
import com.yang.server.util.HttpResult;
import com.yang.server.entity.AutoUser;

public interface UserAccountService {
    HttpResult insertAccount(AutoAccount account);

    HttpResult updateAccount(AutoAccount account);

    HttpResult<IPage<AutoAccount>> selctAccountAll(Long id, Integer pageNo, Integer pageSize, String phone);

    HttpResult selectInfo(Long id);

    HttpResult insertUser(AutoUser autoUser);

    HttpResult updateUser(AutoUser autoUser);

    HttpResult<IPage<AutoUser>> selctUserAll(Long id, Integer pageNo, Integer pageSize, String phone,String createPhone);

    HttpResult selectUserInfo(Long id);

    HttpResult handleClock(Long id, Long createId);

    void updateClockDays(AutoUser autoUser,AutoAccount autoAccount,Integer type,Integer way);

    HttpResult activateUserClock(Long id, Boolean enable);

    HttpResult<IPage<UserAccountRecords>> getUserClockRecords(Long id, Integer pageNo, Integer pageSize, String phone,String createPhone);
}
