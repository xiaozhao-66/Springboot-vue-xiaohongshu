package com.yanhuo.platform.api;

import com.yanhuo.common.result.Result;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.platform.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目启动要运行的模块
 * @author 48423
 */
@RestController
@RequestMapping("/api/platform/init")
@Api(tags="初始化模块")
public class ApiController {

    @Autowired
    ImgDetailService imgDetailService;

    @Autowired
    UserService userService;

    /**
     * 批量增加图片信息
     *
     * @return
     */
    @RequestMapping("addBulkData")
    public Result<?> addBulkData() {
        imgDetailService.addBulkData();
        return Result.ok(null);
    }

    /**
     * 批量增加图片信息
     *
     * @return
     */
    @RequestMapping("addBulkRedisData")
    public Result<?> addBulkRedisData() {
        imgDetailService.addBulkRedisData();
        return Result.ok(null);
    }

    /**
     * 批量增加图片信息
     *
     * @return
     */
    @RequestMapping("addBulkUserRecord")
    public Result<?> addBulkUserRecord() {
        userService.addBulkUserRecord();
        return Result.ok(null);
    }
}
