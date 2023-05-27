package com.xz.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.page.PageData;
import com.xz.common.service.CrudService;
import com.xz.platform.dto.FollowDTO;
import com.xz.platform.entity.FollowEntity;
import com.xz.platform.vo.FollowTrendVo;
import com.xz.platform.vo.FollowVo;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface FollowService extends CrudService<FollowEntity, FollowDTO> {

    List<FollowTrendVo> getAllFollowTrends(long page, long limit, String userId);

    boolean isFollow(String uid, String fid);

    Page<FollowVo> getAllFanUser(long page, long limit, String uid);

    void followUser(FollowDTO followDTO);

    List<FollowVo> getAllFriend(String uid, Integer type);

    void clearFollow(FollowDTO followDTO);
}