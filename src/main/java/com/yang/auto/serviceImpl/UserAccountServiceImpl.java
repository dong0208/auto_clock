package com.yang.auto.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.auto.entity.AutoAccount;
import com.yang.auto.entity.AutoUser;
import com.yang.auto.entity.UserClock;
import com.yang.auto.entity.UserClockDay;
import com.yang.auto.mapper.AutoAccountMapper;
import com.yang.auto.mapper.AutoUserMapper;
import com.yang.auto.mapper.UserClockDayMapper;
import com.yang.auto.service.ClockService;
import com.yang.auto.service.UserAccountService;
import com.yang.auto.util.EncodeUtil;
import com.yang.auto.util.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    @Resource
    private AutoAccountMapper autoAccountMapper;
    @Resource
    private ClockService clockService;
    @Resource
    private AutoUserMapper autoUserMapper;
    @Resource
    private UserClockDayMapper userClockDayMapper;


    @Override
    public HttpResult insertAccount(AutoAccount account) {
        //查询创建人信息
        AutoAccount autoAccount = autoAccountMapper.selectOne(new QueryWrapper<AutoAccount>().lambda().eq(AutoAccount::getId, account.getCreateId()).eq(AutoAccount::getType, 0));
        if (autoAccount == null) {
            return HttpResult.failure("创建人不存在");
        }
        //查询手机号是否重复添加
        List<AutoAccount> accountList = autoAccountMapper.selectList(new QueryWrapper<AutoAccount>().lambda().eq(AutoAccount::getPhone, account.getPhone()));
        if (accountList.size() > 0) {
            return HttpResult.failure("手机号已存在，不能重复添加");
        }
        String password = EncodeUtil.base64Encoder(account.getPassword());
        account.setPassword(password);
        account.setType(1);
        int a = autoAccountMapper.insert(account);
        if (a != 1) {
            return HttpResult.failure("添加账户失败");
        }

        if (account.getGongClockDays() != null) {
            //添加修改记录
            try {
                UserClockDay userClockDay = new UserClockDay();
                userClockDay.setUserId(account.getId());
                userClockDay.setCreateId(account.getCreateId());
                userClockDay.setClockDays(account.getGongClockDays());
                userClockDay.setChangeDays(account.getGongClockDays());
                userClockDay.setType(1);
                userClockDay.setAppType(1);
                userClockDayMapper.insert(userClockDay);
            } catch (Exception e) {
                log.error("添加修改记录异常{}", e);
            }
        }

        if (account.getZhiClockDays() != null && account.getZhiClockDays() > 0) {
            //添加修改记录
            try {
                UserClockDay userClockDay = new UserClockDay();
                userClockDay.setUserId(account.getId());
                userClockDay.setCreateId(account.getCreateId());
                userClockDay.setClockDays(account.getZhiClockDays());
                userClockDay.setChangeDays(account.getZhiClockDays());
                userClockDay.setType(1);
                userClockDay.setAppType(2);
                userClockDayMapper.insert(userClockDay);
            } catch (Exception e) {
                log.error("添加修改记录异常{}", e);
            }
        }

        return HttpResult.success("添加账户成功");
    }

    @Override
    public HttpResult updateAccount(AutoAccount account) {
        //查询创建人信息
        AutoAccount autoAccount = autoAccountMapper.selectOne(new QueryWrapper<AutoAccount>().lambda().eq(AutoAccount::getId, account.getCreateId()).eq(AutoAccount::getType, 0));
        AutoAccount autoAccount1 = autoAccountMapper.selectById(account.getId());
        if (autoAccount == null) {
            return HttpResult.failure("创建人不存在");
        }
        String password = EncodeUtil.base64Encoder(account.getPassword());
        if (!autoAccount1.getPassword().equals(password)) {
            int a = autoAccountMapper.update(new AutoAccount(), new UpdateWrapper<AutoAccount>().lambda().set(AutoAccount::getPassword, password).eq(AutoAccount::getId, account.getId()));
            if (a != 1) {
                return HttpResult.failure("修改密码失败");
            }
        }

        if (autoAccount1.getGongClockDays() != account.getGongClockDays()) {
            int a = autoAccountMapper.update(new AutoAccount(), new UpdateWrapper<AutoAccount>().lambda().set(AutoAccount::getGongClockDays, account.getGongClockDays()).eq(AutoAccount::getId, account.getId()));
            if (a != 1) {
                return HttpResult.failure("修改打卡天数失败");
            }
            //添加修改记录
            try {
                UserClockDay userClockDay = new UserClockDay();
                userClockDay.setUserId(account.getId());
                userClockDay.setCreateId(account.getCreateId());
                userClockDay.setClockDays(account.getGongClockDays());
                userClockDay.setChangeDays(account.getGongClockDays() - autoAccount1.getGongClockDays());
                userClockDay.setType(1);
                userClockDay.setAppType(1);
                userClockDayMapper.insert(userClockDay);
            } catch (Exception e) {
                log.error("添加修改记录异常{}", e);
            }
        }

        if (autoAccount1.getZhiClockDays() != account.getZhiClockDays()) {
            int a = autoAccountMapper.update(new AutoAccount(), new UpdateWrapper<AutoAccount>().lambda().set(AutoAccount::getZhiClockDays, account.getZhiClockDays()).eq(AutoAccount::getId, account.getId()));
            if (a != 1) {
                return HttpResult.failure("修改打卡天数失败");
            }
            //添加修改记录
            try {
                UserClockDay userClockDay = new UserClockDay();
                userClockDay.setUserId(account.getId());
                userClockDay.setCreateId(account.getCreateId());
                userClockDay.setClockDays(account.getZhiClockDays());
                userClockDay.setChangeDays(account.getZhiClockDays() - autoAccount1.getZhiClockDays());
                userClockDay.setType(1);
                userClockDay.setAppType(2);
                userClockDayMapper.insert(userClockDay);
            } catch (Exception e) {
                log.error("添加修改记录异常{}", e);
            }
        }

        if (account.getAccountStatus() != account.getAccountStatus()) {
            int a = autoAccountMapper.update(new AutoAccount(), new UpdateWrapper<AutoAccount>().lambda().set(AutoAccount::getAccountStatus, account.getAccountStatus()).eq(AutoAccount::getId, account.getId()));
            if (a != 1) {
                return HttpResult.failure("修改账号状态失败");
            }
        }

        return HttpResult.success("修改成功");
    }

    @Override
    public HttpResult<IPage<AutoAccount>> selctAccountAll(Long id, Integer pageNo, Integer pageSize, String phone) {
        LambdaQueryWrapper<AutoAccount> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotEmpty(phone)) {
            lambdaQueryWrapper.eq(AutoAccount::getPhone, phone);
        }
        lambdaQueryWrapper.eq(AutoAccount::getCreateId, id);
        Page<AutoAccount> accountPage = new Page<>(pageNo, pageSize);
        IPage<AutoAccount> autoAccountIPage = autoAccountMapper.selectPage(accountPage, lambdaQueryWrapper);
        List<AutoAccount> records = accountPage.getRecords();
        for (AutoAccount record : records) {
            record.setPassword(EncodeUtil.base64Decoder(record.getPassword()));
        }
        return HttpResult.success(autoAccountIPage);
    }

    @Override
    public HttpResult selectInfo(Long id) {
        AutoAccount autoAccount = autoAccountMapper.selectById(id);
        if (autoAccount != null) {
            return HttpResult.success(autoAccount);
        }
        return HttpResult.failure("查询不到该账户信息");
    }

    @Override
    public HttpResult insertUser(AutoUser autoUser) {
        if (StringUtils.isNotEmpty(autoUser.getWeeks()) && autoUser.getClockPm() == 0 &&  autoUser.getClockAm() == 0 ){
            return HttpResult.failure("选择周几,上午打卡或下午打卡二选一或都选");
        }

        //校验账号密码
        if (autoUser.getAppType() == 1) { //工学云
            HttpResult httpResult = clockService.gongxueyunVerify(autoUser.getPhone(), autoUser.getPassword());
            if (!httpResult.isStatus()) {
                return httpResult;
            }
        } else {
            HttpResult httpResult = clockService.zhiClockVerify(autoUser.getPhone(), autoUser.getPassword(), "");
            if (!httpResult.isStatus()) {
                return httpResult;
            }
        }
        //保存信息
        String password = EncodeUtil.base64Encoder(autoUser.getPassword());
        autoUser.setPassword(password);
        int a = autoUserMapper.insert(autoUser);
        if (a != 1) {
            return HttpResult.failure("添加用户失败");
        }
        if (autoUser.getAppType() == 1) { //工学云
            //添加修改记录
            try {
                UserClockDay userClockDay = new UserClockDay();
                userClockDay.setUserId(autoUser.getId());
                userClockDay.setCreateId(autoUser.getCreateId());
                userClockDay.setClockDays(autoUser.getClockDays());
                userClockDay.setChangeDays(autoUser.getClockDays());
                userClockDay.setType(2);
                userClockDay.setAppType(autoUser.getAppType());
                userClockDayMapper.insert(userClockDay);

            } catch (Exception e) {
                log.error("添加修改记录异常{}", e);
            }

        } else {
            try {
                UserClockDay userClockDay = new UserClockDay();
                userClockDay.setUserId(autoUser.getId());
                userClockDay.setCreateId(autoUser.getCreateId());
                userClockDay.setClockDays(autoUser.getClockDays());
                userClockDay.setChangeDays(autoUser.getClockDays());
                userClockDay.setType(2);
                userClockDay.setAppType(autoUser.getAppType());
                userClockDayMapper.insert(userClockDay);
            } catch (Exception e) {
                log.error("添加修改记录异常{}", e);
            }
        }
        return HttpResult.success("添加成功");
    }


    @Override
    public HttpResult updateUser(AutoUser autoUser) {
        if (StringUtils.isNotEmpty(autoUser.getWeeks()) && autoUser.getClockPm() == 0 &&  autoUser.getClockAm() == 0 ){
            return HttpResult.failure("选择周几,上午打卡或下午打卡二选一或都选");
        }
        AutoUser autoUser1 = autoUserMapper.selectById(autoUser.getId());
        if (autoUser1 == null) {
            return HttpResult.failure("找不到用户信息");
        }
        //校验账号密码
        if (autoUser.getPassword().equals(EncodeUtil.base64Encoder(autoUser1.getPassword()))) {
            if (autoUser.getAppType() == 1) { //工学云
                HttpResult httpResult = clockService.gongxueyunVerify(autoUser.getPhone(), autoUser.getPassword());
                if (!httpResult.isStatus()) {
                    return httpResult;
                }
            } else {
                HttpResult httpResult = clockService.zhiClockVerify(autoUser.getPhone(), autoUser.getPassword(), "");
                if (!httpResult.isStatus()) {
                    return httpResult;
                }
            }
        }

        String password = EncodeUtil.base64Encoder(autoUser.getPassword());
        autoUser.setPassword(password);

        int update = autoUserMapper.update(new AutoUser(), new UpdateWrapper<AutoUser>().lambda()
                .set(AutoUser::getAppType, autoUser.getAppType())
                .set(AutoUser::getArea, autoUser.getArea())
                .set(AutoUser::getCity, autoUser.getCity())
                .set(AutoUser::getAddress, autoUser.getAddress())
                .set(AutoUser::getClockAm, autoUser.getClockAm())
                .set(AutoUser::getClockPm, autoUser.getClockPm())
                .set(AutoUser::getCountry, autoUser.getCountry())
                .set(AutoUser::getLatitude, autoUser.getLatitude())
                .set(AutoUser::getLongitude, autoUser.getLongitude())
                .set(AutoUser::getPassword, autoUser.getPassword())
                .set(AutoUser::getPhone, autoUser.getPhone())
                .set(AutoUser::getWeeks, autoUser.getWeeks())
                .set(AutoUser::getProvince, autoUser.getProvince())
                .set(AutoUser::getClockDays, autoUser.getClockDays())
                .eq(AutoUser::getId, autoUser.getId())
        );
        if (update != 1) {
            return HttpResult.failure("修改失败");
        }
        if (autoUser.getAppType() == 1) { //工学云
            //添加修改记录
            try {
                if (autoUser.getClockDays() - autoUser1.getClockDays() != 0) {
                    UserClockDay userClockDay = new UserClockDay();
                    userClockDay.setUserId(autoUser.getId());
                    userClockDay.setCreateId(autoUser1.getCreateId());
                    userClockDay.setClockDays(autoUser.getClockDays());
                    userClockDay.setChangeDays(autoUser.getClockDays() - autoUser1.getClockDays());
                    userClockDay.setType(2);
                    userClockDay.setAppType(autoUser.getAppType());
                    userClockDayMapper.insert(userClockDay);
                }
            } catch (Exception e) {
                log.error("添加修改记录异常{}", e);
            }

        } else {
            try {
                if (autoUser.getClockDays() - autoUser1.getClockDays() != 0) {
                    UserClockDay userClockDay = new UserClockDay();
                    userClockDay.setUserId(autoUser.getId());
                    userClockDay.setCreateId(autoUser1.getCreateId());
                    userClockDay.setClockDays(autoUser.getClockDays());
                    userClockDay.setChangeDays(autoUser.getClockDays() - autoUser1.getClockDays());
                    userClockDay.setType(2);
                    userClockDay.setAppType(autoUser.getAppType());
                    userClockDayMapper.insert(userClockDay);
                }
            } catch (Exception e) {
                log.error("添加修改记录异常{}", e);
            }

        }

        return HttpResult.success("修改成功");
    }

    @Override
    public HttpResult<IPage<AutoUser>> selctUserAll(Long id, Integer pageNo, Integer pageSize, String phone) {

        LambdaQueryWrapper<AutoUser> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (phone != null) {
            lambdaQueryWrapper.eq(AutoUser::getPhone, phone);
        }
        lambdaQueryWrapper.eq(AutoUser::getCreateId, id);
        Page<AutoUser> accountPage = new Page<>(pageNo, pageSize);
        IPage<AutoUser> autoAccountIPage = autoUserMapper.selectPage(accountPage, lambdaQueryWrapper);
        List<AutoUser> records = accountPage.getRecords();
        for (AutoUser record : records) {
            record.setPassword(EncodeUtil.base64Decoder(record.getPassword()));
        }
        return HttpResult.success(autoAccountIPage);
    }

    @Override
    public HttpResult selectUserInfo(Long id) {
        AutoUser autoUser = autoUserMapper.selectById(id);
        if (autoUser != null) {
            return HttpResult.success(autoUser);
        }
        return HttpResult.failure("查询不到该用户信息");
    }

    @Override
    public HttpResult handleClock(Long id, Long createId) {
        AutoUser autoUser = autoUserMapper.selectById(id);
        if (autoUser == null) {
            return HttpResult.failure("查询不到该用户信息");
        }
        //判断用户是否符合条件打卡
        if (!autoUser.isEnable() || autoUser.getClockDays() <= 0) {
            return HttpResult.failure("该用户打卡功能已失效");
        }
        AutoAccount autoAccount = autoAccountMapper.selectById(createId);
        if (autoAccount == null) {
            return HttpResult.failure("查询不到操作人信息");
        }
        if (autoUser.getAppType() == 1 && autoAccount.getGongClockDays() <= 1) {
            return HttpResult.failure("管理员打卡功能已失效");
        }
        if (autoUser.getAppType() == 2 && autoAccount.getZhiClockDays() <= 0) {
            return HttpResult.failure("管理员打卡功能已失效");
        }

        UserClock userClock = new UserClock();
        BeanUtils.copyProperties(autoUser, userClock);
        if (autoUser.getAppType() == 1) {
            HttpResult httpResult = clockService.userOperation(userClock);
            if (!httpResult.isStatus()) {
                return httpResult;
            }
            //扣减打卡剩余时间用户、管理员
            updateClockDays(autoUser, autoAccount, 2);
            return httpResult;
        } else {
            HttpResult httpResult = clockService.userZhiClock(userClock);
            if (!httpResult.isStatus()) {
                return httpResult;
            }
            //扣减打卡剩余时间用户、管理员
            updateClockDays(autoUser, autoAccount, 2);
            return httpResult;
        }

    }


    public void updateClockDays(AutoUser autoUser, AutoAccount autoAccount, Integer type) {

        Integer day = 0;
        if (autoUser.getAppType() == 1) {
            day = 2;

        } else if (autoUser.getAppType() == 2) {
            day = 1;
        }
        try {
            autoUserMapper.update(new AutoUser(), new UpdateWrapper<AutoUser>().lambda().set(AutoUser::getClockDays, autoUser.getClockDays() - day).eq(AutoUser::getId, autoUser.getId()));
            autoAccountMapper.update(new AutoAccount(), new UpdateWrapper<AutoAccount>().lambda().set(AutoAccount::getGongClockDays, autoAccount.getGongClockDays() - day).eq(AutoAccount::getId, autoAccount.getId()));
            UserClockDay userClockDay = new UserClockDay();
            userClockDay.setUserId(autoUser.getId());
            userClockDay.setCreateId(autoAccount.getId());
            userClockDay.setClockDays(autoUser.getClockDays() - day);
            userClockDay.setChangeDays(day);
            userClockDay.setType(type);
            userClockDay.setAppType(autoUser.getAppType());
            userClockDayMapper.insert(userClockDay);
        } catch (Exception e) {
            log.error("updateClockDays,扣减打卡剩余时间用户、管理员异常，e{}", e);
        }

    }

    @Override
    public HttpResult activateUserClock(Long id, Boolean enable) {
        int a = autoUserMapper.update(new AutoUser(), new UpdateWrapper<AutoUser>().lambda()
                .set(AutoUser::isEnable, enable)
                .eq(AutoUser::getId, id)
        );
        if (a == 1) {
            return HttpResult.success();
        }
        return HttpResult.failure("启动打卡失败");
    }

}
