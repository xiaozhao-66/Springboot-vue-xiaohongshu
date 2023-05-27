package com.xz.auth.dao;

import com.xz.auth.entity.AuthUser;
import com.xz.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthUserDao extends BaseDao<AuthUser> {
}
