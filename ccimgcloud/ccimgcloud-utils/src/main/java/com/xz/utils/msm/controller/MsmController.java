package com.xz.utils.msm.controller;


import com.xz.common.utils.RedisUtils;
import com.xz.utils.msm.service.MsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *发送信息的功能
 */
@RestController
@RequestMapping("/msm")
public class MsmController {


    @Autowired
    MsmService msmService;

    @Autowired
    RedisUtils redisUtils;

    @RequestMapping("sendMsm/{phone}")
    public void sendMsm(@PathVariable String phone) throws Exception {
        String code = msmService.sendMsm(phone);
        redisUtils.set("code", code, 60 * 5L);
    }
}
