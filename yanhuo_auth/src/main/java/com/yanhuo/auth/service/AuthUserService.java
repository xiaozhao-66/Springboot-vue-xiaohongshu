package com.yanhuo.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.auth.AuthUserDTO;
import com.yanhuo.xo.dto.auth.UserOtherLoginRelationDTO;
import com.yanhuo.xo.model.User;

import java.util.Map;

public interface AuthUserService extends IService<User> {


    /**
     * 用户登录
     *
     * @param authUserDTO
     * @return
     */
    Map<String, Object> login(AuthUserDTO authUserDTO);

    /**
     * 使用验证码登录
     *
     * @param authUserDTO
     * @return
     */
    Map<String, Object> loginByCode(AuthUserDTO authUserDTO);

    /**
     * 根据用户的token信息得到当前用户
     *
     * @param token token信息
     * @return 用户类
     */
    User getUserInfoByToken(String token);

    /**
     * 用户注册
     *
     * @param authUserDTO 前台传递用户信息
     */
    Map<String, Object> regist(AuthUserDTO authUserDTO);

    /**
     * 用户是否注册
     *
     * @param authUserDTO
     * @return
     */
    boolean isRegist(AuthUserDTO authUserDTO);

    /**
     * 退出登录
     *
     * @param authUserDTO
     * @return
     */
    void loginOut(AuthUserDTO authUserDTO);

    /**
     * 校验
     *
     * @param authUserDTO
     * @return
     */
    Map<String, Object> check(AuthUserDTO authUserDTO);

    /**
     * 修改密码
     *
     * @param authUserDTO
     * @return
     */
    Boolean updatePassword(AuthUserDTO authUserDTO);

    /**
     * 第三方登录
     *
     * @param userOtherLoginRelationDTO
     * @return
     */
    Map<String, Object> otherLogin(UserOtherLoginRelationDTO userOtherLoginRelationDTO);

    /**
     * 刷新token
     *
     * @param authUserDTO
     * @return
     */
    @Deprecated
    Map<String, Object> refreshToken(AuthUserDTO authUserDTO);
}
