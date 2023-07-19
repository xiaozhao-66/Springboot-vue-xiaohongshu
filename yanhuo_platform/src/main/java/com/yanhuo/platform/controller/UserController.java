package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.vo.FollowVo;
import com.yanhuo.xo.vo.TrendVo;
import com.yanhuo.xo.vo.UserRecordVo;
import com.yanhuo.xo.vo.UserVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@RestController
@RequestMapping("/api/platform/user")
@Api(tags="用户模块")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 得到当前用户的所有动态
     *
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    @RequestMapping("/getTrendByUser/{page}/{limit}")
    public Result<?> getTrendByUser(@PathVariable long page, @PathVariable long limit, String userId, Integer type) {
        Page<TrendVo> pageInfo = userService.getTrendByUser(page, limit, userId, type);
        return Result.ok(pageInfo);
    }

    /**
     * 获取用户信息
     *
     * @param uid
     * @return
     */
    @RequestMapping("getUserInfo")
    public Result<?> getUserInfo(String uid) {
        User user = userService.getById(uid);
        return Result.ok(user);
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @RequestMapping("updateUser")
    public Result<?> updateUser(@RequestBody User user) {
        User user1 = userService.updateUser(user);
        return Result.ok(user1);
    }


    /**
     * 查找用户信息
     *
     * @param keyword
     * @return
     */
    @RequestMapping("searchUser/{page}/{limit}")
    public Result<?> searchUser(@PathVariable long page, @PathVariable long limit, String keyword, String uid) {
        Page<FollowVo> pageInfo = userService.searchUser(page, limit, keyword, uid);
        return Result.ok(pageInfo);
    }

    /**
     * 用户名精确查找
     *
     * @param keyword
     * @return
     */
    @RequestMapping("searchUserByUsername")
    public Result<?> searchUserByUsername(String keyword) {
        List<UserVo> userVoList = userService.searchUserByUsername(keyword);
        return Result.ok(userVoList);
    }

    @RequestMapping("getUserRecord")
    public Result<?> getUserRecord(String uid) {
        UserRecordVo userRecordVo = userService.getUserRecord(uid);
        return Result.ok(userRecordVo);
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
        userService.clearUserRecord(uid, type);
        return Result.ok(null);
    }

}