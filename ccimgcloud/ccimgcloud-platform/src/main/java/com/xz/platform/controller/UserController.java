package com.xz.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.utils.Result;
import com.xz.platform.entity.UserEntity;
import com.xz.platform.service.UserService;
import com.xz.platform.vo.FollowVo;
import com.xz.platform.vo.TrendVo;
import com.xz.platform.vo.UserVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@RestController
@RequestMapping("/user")
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
    public Result<?> getTrendByUser(@PathVariable long page, @PathVariable long limit, String userId) {
        List<TrendVo> trendVoList = userService.getTrendByUser(page, limit, userId);
        return new Result<List<TrendVo>>().ok(trendVoList);
    }

    /**
     * 获取用户信息
     *
     * @param uid
     * @return
     */
    @RequestMapping("getUserInfo")
    public Result<?> getUserInfo(String uid) {
        UserVo userVo = userService.getUserInfo(uid);
        return new Result<UserVo>().ok(userVo);
    }

    /**
     * 更新用户信息
     *
     * @param userEntity
     * @return
     */
    @RequestMapping("updateUser")
    public Result<?> updateUser(@RequestBody UserEntity userEntity) {
        UserEntity user = userService.updateUser(userEntity);
        return new Result<>().ok(user);
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
        return new Result<>().ok(pageInfo);
    }


    /**
     * 用户名精确查找
     *
     * @param keyword
     * @return
     */
    @RequestMapping("searchUserByUsername")
    public Result<?> searchUserByUsername(String keyword) {
        UserVo user = userService.searchUserByUsername(keyword);
        return new Result<>().ok(user);
    }
}