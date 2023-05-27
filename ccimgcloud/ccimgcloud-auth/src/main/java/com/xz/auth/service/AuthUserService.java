package com.xz.auth.service;

import com.xz.auth.dto.AuthUserDTO;
import com.xz.auth.dto.UserOtherLoginRelationDTO;
import com.xz.auth.entity.AuthUser;
import com.xz.common.service.CrudService;

import java.util.Map;

public interface AuthUserService extends CrudService<AuthUser, AuthUserDTO> {
    Map<String, Object> login(AuthUserDTO authUserDTO);

    AuthUser getUserInfoByToken(String token);

    void loginOut(AuthUser authUser);

    Map<String, Object> regist(AuthUserDTO authUserDTO);

    Map<String, Object> check(AuthUserDTO authUserDTO);

    Boolean updatePassword(AuthUserDTO authUserDTO);

    Map<String, Object> loginByCode(AuthUserDTO authUserDTO);

    Map<String, Object> otherLogin(UserOtherLoginRelationDTO userOtherLoginRelationDTO);

    Map<String, Object> refreshToken(AuthUser authUser);

    boolean isRegist(AuthUserDTO authUserDTO);
}
