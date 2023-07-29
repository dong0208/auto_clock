package com.yang.auto.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yang.auto.entity.AutoAccount;
import com.yang.auto.entity.AutoUser;
import com.yang.auto.util.HttpResult;

public interface UserAccountService {
    HttpResult insertAccount(AutoAccount account);

    HttpResult updateAccount(AutoAccount account);

    HttpResult<IPage<AutoAccount>> selctAccountAll(Long id, Integer pageNo, Integer pageSize, String phone);

    HttpResult selectInfo(Long id);

    HttpResult insertUser(AutoUser autoUser);

    HttpResult updateUser(AutoUser autoUser);

    HttpResult<IPage<AutoUser>> selctUserAll(Long id, Integer pageNo, Integer pageSize, String phone);

    HttpResult selectUserInfo(Long id);

    HttpResult handleClock(Long id, Long createId);

    void updateClockDays(AutoUser autoUser,AutoAccount autoAccount,Integer type);

    HttpResult activateUserClock(Long id, Boolean enable);
}
