package com.yanhuo.util.msm.controller;

import cn.hutool.core.util.RandomUtil;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.util.msm.service.DmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *发送邮件的功能
 */
@RestController
@RequestMapping("/api/util/dm")
public class DmController {

    @Autowired
    DmService dmService;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 发生邮件
     *
     * @param email
     * @return
     */
    @GetMapping("sendDm/{email}")
    public Result<?> sendDm(@PathVariable String email) {
        try {
            String content = RandomUtil.randomNumbers(4);
            dmService.sendDm(email, content);
            redisUtils.set("code", content, 60 * 5L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok(null);
    }
}
