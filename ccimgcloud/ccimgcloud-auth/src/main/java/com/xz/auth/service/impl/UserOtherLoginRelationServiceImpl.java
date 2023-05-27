package com.xz.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.auth.dao.AuthUserDao;
import com.xz.auth.dao.UserOtherLoginRelationDao;
import com.xz.auth.dto.AuthUserDTO;
import com.xz.auth.dto.UserOtherLoginRelationDTO;
import com.xz.auth.entity.AuthUser;
import com.xz.auth.entity.UserOtherLoginRelationEntity;
import com.xz.auth.service.AuthUserService;
import com.xz.auth.service.UserOtherLoginRelationService;
import com.xz.common.service.impl.CrudServiceImpl;

import java.util.Map;

public class UserOtherLoginRelationServiceImpl extends CrudServiceImpl<UserOtherLoginRelationDao, UserOtherLoginRelationEntity, UserOtherLoginRelationDTO> implements UserOtherLoginRelationService {
    @Override
    public QueryWrapper<UserOtherLoginRelationEntity> getWrapper(Map<String, Object> params) {
        return null;
    }


}
