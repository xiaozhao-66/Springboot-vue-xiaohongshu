package com.yanhuo.auth.controller;


import com.yanhuo.auth.service.AuthUserService;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.AddGroup;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.xo.dto.auth.AuthUserDTO;
import com.yanhuo.xo.dto.auth.UserOtherLoginRelationDTO;
import com.yanhuo.xo.model.User;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 48423
 */
@Api(tags = "权限模块")
@RestController
@RequestMapping("/api/auth")
public class AuthUserController {

    @Autowired
    AuthUserService authUserService;

    /**
     * 用户登录
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("login")
    public Result<?> login(@RequestBody AuthUserDTO authUserDTO) {
        Map<String, Object> map = authUserService.login(authUserDTO);
        return Result.ok(map);
    }

    /**
     * 使用验证码登录
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("loginByCode")
    public Result<?> loginByCode(@RequestBody AuthUserDTO authUserDTO) {
        Map<String, Object> map = authUserService.loginByCode(authUserDTO);
        return Result.ok(map);
    }


    /**
     * 根据用户的token信息得到当前用户
     *
     * @param token token信息
     * @return 用户类
     */
    @RequestMapping("getUserInfoByToken")
    public Result<?> getUserInfoByToken(String token) {
        User user = authUserService.getUserInfoByToken(token);
        return Result.ok(user);
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
        return Result.ok(data);

    }

    /**
     * 用户是否注册
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("isRegist")
    public Result<?> isRegist(@RequestBody AuthUserDTO authUserDTO) {
        boolean isRegist = authUserService.isRegist(authUserDTO);
        return Result.ok(isRegist);

    }


    /**
     * 退出登录
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("loginOut")
    public Result<?> loginOut(@RequestBody AuthUserDTO authUserDTO) {
        authUserService.loginOut(authUserDTO);
        return Result.ok(null);
    }


    /**
     * 校验
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("check")
    public Result<?> check(@RequestBody AuthUserDTO authUserDTO) {
        Map<String, Object> map = authUserService.check(authUserDTO);
        return Result.ok(map);
    }

    /**
     * 修改密码
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("updatePassword")
    public Result<?> updatePassword(@RequestBody AuthUserDTO authUserDTO) {
        ValidatorUtils.validateEntity(authUserDTO, DefaultGroup.class);
        Boolean flag = authUserService.updatePassword(authUserDTO);
        return Result.ok(flag);
    }


    /**
     * 第三方登录
     *
     * @param userOtherLoginRelationDTO
     * @return
     */
    @RequestMapping("/otherLogin")
    public Result<?> otherLogin(@RequestBody UserOtherLoginRelationDTO userOtherLoginRelationDTO) {
        Map<String, Object> map = authUserService.otherLogin(userOtherLoginRelationDTO);
        return Result.ok(map);
    }

    /**
     * (已废弃)刷新token
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("refreshToken")
    public Result<?> refreshToken(@RequestBody AuthUserDTO authUserDTO) {
        Map<String, Object> map = authUserService.refreshToken(authUserDTO);
        return Result.ok(map);
    }
}
