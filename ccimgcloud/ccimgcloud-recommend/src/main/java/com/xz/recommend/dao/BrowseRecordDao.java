package com.xz.recommend.dao;

import com.xz.common.dao.BaseDao;
import com.xz.recommend.entity.BrowseRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Mapper
public interface BrowseRecordDao extends BaseDao<BrowseRecordEntity> {

    List<String> selectNums(@Param("uid") String uid, @Param("nums")int nums);
}