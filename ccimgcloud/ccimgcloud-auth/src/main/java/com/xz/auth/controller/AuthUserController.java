package com.xz.auth.controller;

import com.xz.auth.dto.AuthUserDTO;
import com.xz.auth.dto.UserOtherLoginRelationDTO;
import com.xz.auth.entity.AuthUser;
import com.xz.auth.service.AuthUserService;
import com.xz.common.utils.JwtUtils;
import com.xz.common.utils.Result;
import com.xz.common.validator.ValidatorUtils;
import com.xz.common.validator.group.AddGroup;
import com.xz.common.validator.group.DefaultGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * @author 48423
 */
@RestController
@RequestMapping("auth")
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

    /**
     * 登录功能
     *
     * @param authUserDTO 用户类
     */
    @RequestMapping("login")
    public Result<?> login(@RequestBody AuthUserDTO authUserDTO) {
        Map<String, Object> map = authUserService.login(authUserDTO);
        return new Result<Map<String, Object>>().ok(map);
    }


    @RequestMapping("loginByCode")
    public Result<?> loginByCode(@RequestBody AuthUserDTO authUserDTO) {
        Map<String, Object> map = authUserService.loginByCode(authUserDTO);
        return new Result<Map<String, Object>>().ok(map);
    }


    /**
     * 根据用户的token信息得到当前用户
     *
     * @param token token信息
     * @return 用户类
     */
    @RequestMapping("getUserInfoByToken")
    public Result<?> getUserInfoByToken(String token) {
        AuthUser authUser = authUserService.getUserInfoByToken(token);
        return new Result<AuthUser>().ok(authUser);
    }


    /**
     * 用户注册
     *
     * @param authUserDTO 前台传递用户信息
     */
    @RequestMapping("register")
    public Result<?> register(@RequestBody AuthUserDTO authUserDTO) {
        ValidatorUtils.validateEntity(authUserDTO, AddGroup.class, DefaultGroup.class);
        Map<String, Object> data = authUserService.regist(authUserDTO);
        return new Result<Map<String, Object>>().ok(data);

    }

    @RequestMapping("isRegist")
    public Result<?> isRegist(@RequestBody AuthUserDTO authUserDTO) {
        boolean isRegist = authUserService.isRegist(authUserDTO);
        return new Result<>().ok(isRegist);

    }


    @RequestMapping("loginOut")
    public Result<?> loginOut(@RequestBody AuthUser authUser) {
        authUserService.loginOut(authUser);
        return new Result<>().ok();
    }


    @RequestMapping("check")
    public Result<?> check(@RequestBody AuthUserDTO authUserDTO) {
        Map<String, Object> map = authUserService.check(authUserDTO);
        return new Result<>().ok(map);
    }

    @RequestMapping("updatePassword")
    public Result<?> updatePassword(@RequestBody AuthUserDTO authUserDTO) {
        ValidatorUtils.validateEntity(authUserDTO, DefaultGroup.class);
        Boolean flag = authUserService.updatePassword(authUserDTO);
        return new Result<>().ok(flag);
    }


    @RequestMapping("/otherLogin")
    public Result<?> otherLogin(@RequestBody UserOtherLoginRelationDTO userOtherLoginRelationDTO) {
        Map<String, Object> map = authUserService.otherLogin(userOtherLoginRelationDTO);
        return new Result<>().ok(map);
    }

    @RequestMapping("refreshToken")
    public Result<?> refreshToken(@RequestBody AuthUser authUser) {
        Map<String, Object> map = authUserService.refreshToken(authUser);
        return new Result<>().ok(map);
    }

}
