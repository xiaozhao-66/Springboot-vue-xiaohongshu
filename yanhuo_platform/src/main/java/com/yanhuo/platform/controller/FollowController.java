package com.yanhuo.platform.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.platform.service.FollowService;
import com.yanhuo.xo.dto.platform.FollowDTO;
import com.yanhuo.xo.vo.FollowVo;
import com.yanhuo.xo.vo.TrendVo;
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
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/api/platform/follow")
@Api(tags="关注粉丝模块")
public class FollowController {

    @Autowired
    private FollowService followService;

    /**
     * 得到当前用户和关注的所有动态
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @RequestMapping("getAllFollowTrends/{page}/{limit}")
    public Result getAllFollowTrends(@PathVariable long page, @PathVariable long limit, String uid) {
        List<TrendVo> list = followService.getAllFollowTrends(page, limit, uid);
        return Result.ok(list);
    }


    /**
     * 根据类型获取所有关注和粉丝
     *
     * @param uid
     * @param type
     * @return
     */
    @RequestMapping("getAllFriend/{page}/{limit}")
    public Result<?> getAllFriend(@PathVariable long page, @PathVariable long limit, String uid, Integer type) {
        Page<FollowVo> pageInfo = followService.getAllFriend(page, limit, uid, type);
        return Result.ok(pageInfo);
    }

    /**
     * 关注用户
     *
     * @param followDTO
     * @return
     */
    @RequestMapping("followUser")
    public Result<?> followUser(@RequestBody FollowDTO followDTO) {
        ValidatorUtils.validateEntity(followDTO, DefaultGroup.class);
        followService.followUser(followDTO);
        return Result.ok(null);
    }

    /**
     * 查看是否关注用户
     *
     * @param uid
     * @param fid
     * @return
     */
    @RequestMapping("isFollow")
    public Result<?> isFollow(String uid, String fid) {
        boolean flag = followService.isFollow(uid, fid);
        return Result.ok(flag);
    }

    /**
     * 删除关注
     *
     * @param followDTO
     * @return
     */
    @RequestMapping("clearFollow")
    public Result<?> clearFollow(@RequestBody FollowDTO followDTO) {
        ValidatorUtils.validateEntity(followDTO, DefaultGroup.class);
        followService.clearFollow(followDTO);
        return Result.ok(null);
    }

}