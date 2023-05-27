package com.xz.recommend.dao;

import com.xz.common.dao.BaseDao;
import com.xz.recommend.entity.TagEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Mapper
public interface TagDao extends BaseDao<TagEntity> {

    List<TagEntity> selectTagsNums(int nums);
}