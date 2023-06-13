package com.xz.platform.dao;

import com.xz.common.dao.BaseDao;
import com.xz.platform.entity.UserRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Mapper
public interface UserRecordDao extends BaseDao<UserRecordEntity> {

    List<UserRecordEntity> selectBatchByUid(List<Long> uids);
}