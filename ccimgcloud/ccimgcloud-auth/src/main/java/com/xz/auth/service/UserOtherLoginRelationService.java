package com.xz.auth.service;

import com.xz.auth.dto.AuthUserDTO;
import com.xz.auth.dto.UserOtherLoginRelationDTO;
import com.xz.auth.entity.AuthUser;
import com.xz.auth.entity.UserOtherLoginRelationEntity;
import com.xz.common.service.CrudService;

public interface UserOtherLoginRelationService extends CrudService<UserOtherLoginRelationEntity, UserOtherLoginRelationDTO> {
}
