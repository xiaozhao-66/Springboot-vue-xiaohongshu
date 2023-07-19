package com.yanhuo.auth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanhuo.xo.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 48423
 */
@Mapper
public interface AuthUserDao extends BaseMapper<User> {
}
