package com.xz.recommend.dao;

import com.xz.common.dao.BaseDao;
import com.xz.recommend.entity.UserEntity;
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

    List<String> selectRand(int nums);
}