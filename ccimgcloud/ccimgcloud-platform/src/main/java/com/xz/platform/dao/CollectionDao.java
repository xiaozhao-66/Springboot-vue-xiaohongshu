package com.xz.platform.dao;

import com.xz.common.dao.BaseDao;
import com.xz.platform.entity.CollectionEntity;
import com.xz.platform.vo.CollectionVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Mapper
public interface CollectionDao extends BaseDao<CollectionEntity> {

    void deleteBatchIdList(List<String> idList);

    List<CollectionVo> getAllCollection(long page, long limit, String uid, Integer type);
}