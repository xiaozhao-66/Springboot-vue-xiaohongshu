package com.xz.platform.dao;

import com.xz.common.dao.BaseDao;
import com.xz.platform.entity.ImgDetailsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Mapper
public interface ImgDetailsDao extends BaseDao<ImgDetailsEntity> {

    List<ImgDetailsEntity> selectBatch(Map<String, Object> map);
}