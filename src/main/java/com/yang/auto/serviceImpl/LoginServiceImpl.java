package com.yang.auto.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yang.auto.entity.AutoAccount;
import com.yang.auto.mapper.AutoAccountMapper;
import com.yang.auto.service.LoginService;
import com.yang.auto.util.EncodeUtil;
import com.yang.auto.util.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Resource
    private AutoAccountMapper autoAccountMapper;


    @Override
    public AutoAccount doLoginUser(String phone, String password, String type) {
        List<AutoAccount> accountList = autoAccountMapper.selectList(new QueryWrapper<AutoAccount>().lambda().eq(AutoAccount::getPhone,phone));
        Assert.isTrue(accountList.size() != 0, "该用户不存在！");
        Assert.isTrue(accountList.size() == 1, "用户存在多个，请联系管理员！");
        AutoAccount autoAccount = accountList.get(0);
        Assert.isTrue(autoAccount.getPassword().equalsIgnoreCase(EncodeUtil.base64Encoder(password)), "登陆失败，密码或账号错误！");
        Assert.isTrue(autoAccount.getAccountStatus() != 1,"账号已被禁用");
        return autoAccount;
    }

    @Override
    public HttpResult updatePassWord(String id, String password) {
        AutoAccount autoAccount = autoAccountMapper.selectById(id);
        Assert.isTrue(autoAccount != null, "该用户不存在！");
        //加密
        int update = autoAccountMapper.update(new AutoAccount(),new UpdateWrapper<AutoAccount>().lambda()
                .set(AutoAccount::getPassword,EncodeUtil.base64Encoder(password))
                .eq(AutoAccount::getId,id)
        );
        if (update > 0){
            return HttpResult.success();
        }
        return HttpResult.failure("修改失败");
    }
}
