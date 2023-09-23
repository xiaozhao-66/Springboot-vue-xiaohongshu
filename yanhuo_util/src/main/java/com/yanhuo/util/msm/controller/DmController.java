package com.yanhuo.util.msm.controller;

import cn.hutool.core.util.RandomUtil;
import com.yanhuo.common.constant.auth.AuthConstant;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.util.msm.service.DmService;
import com.yanhuo.xo.dto.auth.AuthUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
     * @param
     * @return
     */
    @PostMapping("sendDm")
    public Result<?> sendDm(@RequestBody AuthUserDTO authUserDTO) {
        try {
            String content = RandomUtil.randomNumbers(4);
            dmService.sendDm(authUserDTO.getEmail(), content);
            redisUtils.set(AuthConstant.CODE+authUserDTO.getEmail(), content, 60 * 5L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok(null);
    }
}
