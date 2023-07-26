package com.yang.auto.controller;

import com.yang.auto.entity.UserClock;
import com.yang.auto.service.ClockService;
import com.yang.auto.util.HttpResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/clock")
@Slf4j
@ApiModel("打卡")
public class ClockController {


    @Resource
    private ClockService clockService;


    @ApiOperation(value = "打卡接口")
    @PostMapping("/user//gong/operation")
    public HttpResult userClock(@RequestBody UserClock userClock){

        return clockService.userOperation(userClock);
    }

    @ApiOperation(value = "打卡记录")
    @PostMapping("/user/Records")
    public HttpResult userRecords(@RequestBody UserClock userClock){

        return clockService.userRecords(userClock);
    }


    @ApiOperation(value = "打卡接口(职家)")
    @PostMapping("/user/zhi/operation")
    public HttpResult userZhiClock(@RequestBody UserClock userClock){

        return clockService.userZhiClock(userClock);
    }

}
