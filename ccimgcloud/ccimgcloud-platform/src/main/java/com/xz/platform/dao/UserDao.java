package com.xz.platform.dao;

import com.xz.common.dao.BaseDao;
import com.xz.platform.entity.UserEntity;
import com.xz.platform.vo.TrendVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Mapper
public interface UserDao extends BaseDao<UserEntity> {

    List<TrendVo> getTrendByUser(long page, long limit, String userId);
}