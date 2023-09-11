package com.yanhuo.platform.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanhuo.xo.model.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Mapper
public interface MessageDao extends BaseMapper<Message> {
	
}