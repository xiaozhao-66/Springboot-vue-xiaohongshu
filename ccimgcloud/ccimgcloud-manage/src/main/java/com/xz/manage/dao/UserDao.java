package com.xz.manage.dao;

import com.xz.common.dao.BaseDao;
import com.xz.manage.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Mapper
public interface UserDao extends BaseDao<UserEntity> {
	
}