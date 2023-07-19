package com.yanhuo.platform.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanhuo.xo.model.Follow;
import com.yanhuo.xo.vo.TrendVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Mapper
public interface FollowDao extends BaseMapper<Follow> {

    /**
     * 得到关注的所有动态
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    List<TrendVo> getAllFollowTrends(long page, long limit, String uid);
}