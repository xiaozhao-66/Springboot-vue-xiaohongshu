package com.xz.platform.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.utils.Result;
import com.xz.common.validator.ValidatorUtils;
import com.xz.common.validator.group.AddGroup;
import com.xz.common.validator.group.DefaultGroup;
import com.xz.platform.dto.AgreeDTO;
import com.xz.platform.service.AgreeService;
import com.xz.platform.vo.AgreeVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/agree")
@Api(tags="点赞模块")
public class AgreeController {

    @Autowired
    private AgreeService agreeService;

    /**
     * 点赞图片和评论
     *
     * @param agreeDTO
     * @return
     */
    @RequestMapping("agree")
    public Result<?> agree(@RequestBody AgreeDTO agreeDTO) {
        ValidatorUtils.validateEntity(agreeDTO, AddGroup.class, DefaultGroup.class);
        agreeService.agree(agreeDTO);
        return new Result<>().ok();
    }

    /**
     * 查看是否点赞
     *
     * @param agreeDTO
     * @return
     */
    @RequestMapping("isAgree")
    public Result<?> isAgree(@RequestBody AgreeDTO agreeDTO) {
        ValidatorUtils.validateEntity(agreeDTO, DefaultGroup.class);
        boolean flag = agreeService.isAgree(agreeDTO);
        return new Result<Boolean>().ok(flag);
    }

    /**
     * 得到当前用户所有的赞和收藏
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @RequestMapping("getAllAgreeAndCollection/{page}/{limit}")
    public Result<?> getAllAgreeAndCollection(@PathVariable("page") long page,
                                              @PathVariable("limit") long limit,
                                              String uid) {
        List<AgreeVo> agreeVoList = agreeService.getAllAgreeAndCollection(page, limit, uid);
        return new Result<List<AgreeVo>>().ok(agreeVoList);
    }

    /**
     * 取消点赞
     * @param agreeDTO
     * @return
     */
    @RequestMapping("cancelAgree")
    public Result<?> cancelAgree(@RequestBody AgreeDTO agreeDTO) {
        ValidatorUtils.validateEntity(agreeDTO, DefaultGroup.class);
        agreeService.cancelAgree(agreeDTO);
        return new Result<>().ok();
    }
}