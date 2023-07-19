package com.yanhuo.platform.service;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.platform.FollowDTO;
import com.yanhuo.xo.model.Follow;
import com.yanhuo.xo.vo.FollowVo;
import com.yanhuo.xo.vo.TrendVo;

import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface FollowService extends IService<Follow> {

    /**
     * 得到当前用户和关注的所有动态
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    List<TrendVo> getAllFollowTrends(long page, long limit, String uid);

    /**
     * 根据类型获取所有关注和粉丝
     *
     * @param uid
     * @param type 0代表获取所有粉丝，1代表获取所有关注用户
     * @return
     */
    Page<FollowVo> getAllFriend(long page, long limit, String uid, Integer type);

    /**
     * 关注用户
     *
     * @param followDTO
     * @return
     */
    void followUser(FollowDTO followDTO);

    /**
     * 查看是否关注用户
     *
     * @param uid
     * @param fid
     * @return
     */
    boolean isFollow(String uid, String fid);

    /**
     * 删除关注
     *
     * @param followDTO
     * @return
     */
    void clearFollow(FollowDTO followDTO);
}