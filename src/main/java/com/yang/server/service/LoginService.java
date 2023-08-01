package com.yang.server.service;

import com.yang.server.entity.AutoAccount;
import com.yang.server.util.HttpResult;

public interface LoginService {
    AutoAccount doLoginUser(String phone, String password, String type);

    HttpResult updatePassWord(String id, String password);
}
