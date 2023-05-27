package com.xz.auth.dao;

import com.xz.auth.entity.UserRecordEntity;
import com.xz.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Mapper
public interface UserRecordDao extends BaseDao<UserRecordEntity> {
	
}