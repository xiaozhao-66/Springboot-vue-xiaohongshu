package com.yanhuo.util.msm.controller;

import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.util.msm.service.MsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *发送信息的功能
 */
@RestController
@RequestMapping("/api/util/msm")
public class MsmController {


    @Autowired
    MsmService msmService;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 发送短信
     *
     * @param phone
     * @throws Exception
     */
    @RequestMapping("sendMsm/{phone}")
    public void sendMsm(@PathVariable String phone) throws Exception {
        String code = msmService.sendMsm(phone);
        redisUtils.set("code", code, 60 * 5L);
    }
}
