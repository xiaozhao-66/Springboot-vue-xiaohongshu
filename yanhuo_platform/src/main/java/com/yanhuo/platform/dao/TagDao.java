package com.yanhuo.platform.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanhuo.xo.model.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Mapper
public interface TagDao extends BaseMapper<Tag> {

    /**
     * 选择热门标签
     * @param nums
     * @return
     */
    List<Tag> selectTagsNums(int nums);
}