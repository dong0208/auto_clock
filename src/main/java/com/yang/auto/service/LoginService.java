package com.yang.auto.service;

import com.yang.auto.entity.AutoAccount;
import com.yang.auto.util.HttpResult;

public interface LoginService {
    AutoAccount doLoginUser(String phone, String password, String type);

    HttpResult updatePassWord(String id, String password);
}
