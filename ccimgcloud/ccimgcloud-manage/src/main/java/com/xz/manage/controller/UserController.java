package com.xz.manage.controller;

import com.xz.common.constant.Constant;
import com.xz.common.page.PageData;
import com.xz.common.utils.Result;
import com.xz.common.validator.AssertUtils;
import com.xz.common.validator.ValidatorUtils;
import com.xz.common.validator.group.AddGroup;
import com.xz.common.validator.group.DefaultGroup;
import com.xz.common.validator.group.UpdateGroup;
import com.xz.manage.entity.UserEntity;
import com.xz.manage.service.UserService;
import com.xz.manage.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@RestController
@RequestMapping("/user")
@Api(tags="")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("getPage/{page}/{limit}")
    public Result<?> getPage(@PathVariable long page,@PathVariable long limit, @RequestBody(required = false) Map<String, Object> params){
       Result<PageData<UserVo>>  result = new Result<>();
        PageData<UserVo> pageData = userService.getPage(page, limit, params);
        return result.ok(pageData);
    }

    @RequestMapping("getOne")
    public Result<?> getOne(long id){
        UserEntity userEntity = userService.selectById(id);
        return new Result<>().ok(userEntity);
    }
}