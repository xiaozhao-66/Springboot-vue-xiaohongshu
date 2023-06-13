package com.xz.platform.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.utils.Result;
import com.xz.common.validator.ValidatorUtils;
import com.xz.common.validator.group.DefaultGroup;
import com.xz.platform.dto.FollowDTO;
import com.xz.platform.service.FollowService;
import com.xz.platform.vo.FollowTrendVo;
import com.xz.platform.vo.FollowVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/follow")
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
    public Result<?> getAllFollowTrends(@PathVariable long page, @PathVariable long limit, String uid) {
        List<FollowTrendVo> list = followService.getAllFollowTrends(page, limit, uid);
        return new Result<List<FollowTrendVo>>().ok(list);
    }


    /**
     * 得到当前用户的所有粉丝
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @RequestMapping("getAllFanUser/{page}/{limit}")
    public Result<?> getAllFanUser(@PathVariable long page, @PathVariable long limit, String uid) {
        Page<FollowVo> pageInfo = followService.getAllFanUser(page, limit, uid);
        return new Result<Page<FollowVo>>().ok(pageInfo);
    }

    /**
     * 根据类型获取所有关注和粉丝
     *
     * @param uid
     * @param type
     * @return
     */
    @RequestMapping("getAllFriend")
    public Result<?> getAllFriend(String uid, Integer type) {
        List<FollowVo> list = followService.getAllFriend(uid, type);
        return new Result<List<FollowVo>>().ok(list);
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
        return new Result<>().ok();
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
        return new Result<Boolean>().ok(flag);
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
        return new Result<>().ok();
    }
}