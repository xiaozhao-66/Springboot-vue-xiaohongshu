package com.xz.platform.dao;

import com.xz.common.dao.BaseDao;
import com.xz.platform.entity.AgreeEntity;
import com.xz.platform.vo.AgreeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Mapper
public interface AgreeDao extends BaseDao<AgreeEntity> {

    List<AgreeVo> getAllAgreeAndCollection(@Param("page") long page, @Param("limit") long limit, @Param("uid") String uid);
}