package com.xz.utils.msm.controller;

import cn.hutool.core.util.RandomUtil;
import com.xz.common.utils.RedisUtils;
import com.xz.common.utils.Result;
import com.xz.utils.msm.service.DmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 *发送邮件的功能
 */
@RestController
@RequestMapping("/dm")
public class DmController {

    @Autowired
    DmService dmService;

    @Autowired
    RedisUtils redisUtils;

    @GetMapping("sendDm/{email}")
    public Result<?> sendDm(@PathVariable String email) {
        try {
            String content = RandomUtil.randomNumbers(4);
            dmService.sendDm(email, content);
            redisUtils.set("code", content, 60 * 5L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<>().ok();
    }
}
