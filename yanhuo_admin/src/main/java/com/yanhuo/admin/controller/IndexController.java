package com.yanhuo.admin.controller;

import com.yanhuo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 48423
 */
@Api(tags = "登录接口")
@RestController
@RequestMapping("/api/admin/index")
public class IndexController {

    /**
     * 管理员登录
     *
     * @return
     */
    @PostMapping("login")
    public Result<?> login() {

        //返回token值
        Map<String, String> map = new HashMap<>();
        map.put("token", "token-admin");
        return Result.ok(map);
    }


    /**
     * 得到管理员登录信息
     *
     * @return
     */
    @GetMapping("info")
    public Result<?> info() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "admin");
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }


    /**
     * 退出登录
     *
     * @return
     */
    @ApiOperation("退出")
    @PostMapping("logout")
    public Result<?> logout() {
        return Result.ok(null);
    }
}
