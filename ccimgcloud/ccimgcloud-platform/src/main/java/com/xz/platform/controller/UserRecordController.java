package com.xz.platform.controller;


import com.xz.common.utils.Result;
import com.xz.platform.service.UserRecordService;
import com.xz.platform.vo.UserRecordVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/userrecord")
@Api(tags="用户记录模块")
public class UserRecordController {

    @Autowired
    private UserRecordService userRecordService;

    /**
     * 获取用户记录
     *
     * @param uid
     * @return
     */
    @RequestMapping("getUserRecord")
    public Result<?> getUserRecord(String uid) {
        UserRecordVo userRecordVo = userRecordService.getUserRecord(uid);
        return new Result<>().ok(userRecordVo);
    }

    /**
     * 清除用户记录
     *
     * @param uid
     * @param type
     * @return
     */
    @RequestMapping("clearUserRecord")
    public Result<?> clearUserRecord(String uid, Integer type) {
        userRecordService.clearUserRecord(uid, type);
        return new Result<>().ok();
    }
}