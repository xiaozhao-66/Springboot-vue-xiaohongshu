package com.xz.platform.dao;

import com.xz.common.dao.BaseDao;
import com.xz.platform.entity.CommentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Mapper
public interface CommentDao extends BaseDao<CommentEntity> {

    List<CommentEntity> subList(Map<String, Object> map);
}