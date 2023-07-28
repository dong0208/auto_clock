package com.yang.auto.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.auto.entity.AutoAccount;
import com.yang.auto.entity.AutoUser;
import com.yang.auto.entity.UserClock;
import com.yang.auto.mapper.AutoAccountMapper;
import com.yang.auto.mapper.AutoUserMapper;
import com.yang.auto.service.ClockService;
import com.yang.auto.service.UserAccountService;
import com.yang.auto.util.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class UserClockGongTask {


    @Resource
    private AutoAccountMapper autoAccountMapper;
    @Resource
    private ClockService clockService;
    @Resource
    private AutoUserMapper autoUserMapper;
    @Resource
    private UserAccountService userAccountService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void clockGong() {
        try {

            log.info("----------------------工学云上午8点打卡任务开始------------------------:::" + Thread.currentThread().getName());
            long startTime = System.currentTimeMillis();
            List<AutoAccount> accountList = autoAccountMapper.selectList(new QueryWrapper<AutoAccount>().lambda().select(AutoAccount::getId)
                    .eq(AutoAccount::getAccountStatus, 0)
                    .eq(AutoAccount::getIsDelete, 0)
            );

            for (AutoAccount account : accountList) {
                Integer day = 0;
                if (account.getGongClockDays() <= 1) {
                    continue;
                }
                List<AutoUser> autoUsers = autoUserMapper.selectList(new QueryWrapper<AutoUser>().lambda()
                        .eq(AutoUser::getCreateId, account.getId())
                        .eq(AutoUser::getAppType,1)
                        .eq(AutoUser::getIsDelete, 0)
                );
                for (AutoUser autoUser : autoUsers) {
                    if (autoUser.getClockDays() <= 1 || !autoUser.isEnable() || StringUtils.isEmpty(autoUser.getWeeks()) || autoUser.getClockAm() == 0) {
                        continue;
                    }
                    //判断今天周几
                    LocalDateTime localDateTime = LocalDateTime.now();
                    int value = localDateTime.getDayOfWeek().getValue();
                    // 周六、周日不打卡
                    if (value != 6 || value != 7) {
                        if (autoUser.getWeeks().contains(String.valueOf(value))) {
                            UserClock userClock = new UserClock();
                            BeanUtils.copyProperties(autoUser, userClock);
                            userClock.setSignType("START");
                            HttpResult httpResult = clockService.userOperation(userClock);
                            if (httpResult.isStatus()) {
                                day = day + 2;
                                userAccountService.updateClockDays(autoUser, account, 2);
                            }
                        }
                    }
                    if (account.getGongClockDays() - day <= 1 && account.getType() == 1) {
                        log.info("下级管理员工学云打卡剩余天数小于2天打卡结束::account = {}", JSONObject.toJSONString(account));
                        break;
                    }

                }

            }
            Runtime.getRuntime().gc();
            long endTime = System.currentTimeMillis();
            float min = (endTime - startTime) / 1000F;
            String str = "一共执行了:" + min + "秒";
            log.info("clockGong（秒）::str = {}", str);
        } catch (Exception e) {
            log.error("clockGong异常", e);
        }

    }


    @Scheduled(cron = "0 0 18 * * ?")
    public void clockGongPm() {
        try {

            log.info("----------------------工学云下午18点打卡任务开始------------------------:::" + Thread.currentThread().getName());
            long startTime = System.currentTimeMillis();
            List<AutoAccount> accountList = autoAccountMapper.selectList(new QueryWrapper<AutoAccount>().lambda().select(AutoAccount::getId)
                    .eq(AutoAccount::getAccountStatus, 0)
                    .eq(AutoAccount::getIsDelete, 0)
            );

            for (AutoAccount account : accountList) {
                Integer day = 0;
                if (account.getGongClockDays() <= 1) {
                    continue;
                }
                List<AutoUser> autoUsers = autoUserMapper.selectList(new QueryWrapper<AutoUser>().lambda()
                        .eq(AutoUser::getCreateId, account.getId())
                        .eq(AutoUser::getAppType,1)
                        .eq(AutoUser::getIsDelete, 0)
                );
                for (AutoUser autoUser : autoUsers) {
                    if (autoUser.getClockDays() <= 1 || !autoUser.isEnable() || StringUtils.isEmpty(autoUser.getWeeks()) || autoUser.getClockPm() == 0) {
                        continue;
                    }
                    //判断今天周几
                    LocalDateTime localDateTime = LocalDateTime.now();
                    int value = localDateTime.getDayOfWeek().getValue();
                    // 周六、周日不打卡
                    if (value != 6 || value != 7) {
                        if (autoUser.getWeeks().contains(String.valueOf(value))) {
                            UserClock userClock = new UserClock();
                            BeanUtils.copyProperties(autoUser, userClock);
                            userClock.setSignType("START");
                            HttpResult httpResult = clockService.userOperation(userClock);
                            if (httpResult.isStatus()) {
                                day = day + 2;
                                userAccountService.updateClockDays(autoUser, account, 2);
                            }
                        }
                    }
                    if (account.getGongClockDays() - day <= 1 && account.getType() == 1) {
                        log.info("clockGongPm下级管理员工学云打卡剩余天数小于2天打卡结束::account = {}", JSONObject.toJSONString(account));
                        break;
                    }

                }

            }
            Runtime.getRuntime().gc();
            long endTime = System.currentTimeMillis();
            float min = (endTime - startTime) / 1000F;
            String str = "一共执行了:" + min + "秒";
            log.info("clockGongPm（秒）::str = {}", str);
        } catch (Exception e) {
            log.error("clockGongPm异常", e);
        }

    }

}
