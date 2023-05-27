package com.xz.platform.controller;

import com.xz.platform.service.UserOtherLoginRelationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/userotherloginrelation")
@Api(tags="第三方登录模块")
public class UserOtherLoginRelationController {

    @Autowired
    private UserOtherLoginRelationService userOtherLoginRelationService;

}