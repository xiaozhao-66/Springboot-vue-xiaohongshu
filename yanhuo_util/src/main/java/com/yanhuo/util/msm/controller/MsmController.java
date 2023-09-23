package com.yanhuo.util.msm.controller;

import com.yanhuo.common.constant.auth.AuthConstant;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.util.msm.service.MsmService;
import com.yanhuo.xo.dto.auth.AuthUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
     * @param
     * @throws Exception
     */
    @PostMapping("sendMsm")
    public void sendMsm(@RequestBody AuthUserDTO authUserDTO) throws Exception {
        String code = msmService.sendMsm(authUserDTO.getPhone());
        redisUtils.set(AuthConstant.CODE+authUserDTO.getPhone(), code, 60 * 5L);
    }
}
