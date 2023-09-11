package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.AddGroup;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.platform.service.AgreeCollectService;
import com.yanhuo.xo.dto.platform.AgreeCollectDTO;
import com.yanhuo.xo.vo.AgreeCollectVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 48423
 */
@RestController
@RequestMapping("/api/platform/agreeCollect")
@Api(tags="点赞收藏模块")
public class AgreeCollectController {

    @Autowired
    private AgreeCollectService agreeCollectService;


    /**
     * 点赞图片和评论
     *
     * @param agreeCollectDTO
     * @return
     */
    @RequestMapping("agree")
    public Result<?> agree(@RequestBody AgreeCollectDTO agreeCollectDTO) {
        ValidatorUtils.validateEntity(agreeCollectDTO, AddGroup.class, DefaultGroup.class);
        agreeCollectService.agree(agreeCollectDTO);
        return Result.ok(null);
    }

    /**
     * 查看是否点赞
     *
     * @param agreeCollectDTO
     * @return
     */
    @RequestMapping("isAgree")
    public Result<?> isAgree(@RequestBody AgreeCollectDTO agreeCollectDTO) {
        ValidatorUtils.validateEntity(agreeCollectDTO, DefaultGroup.class);
        boolean flag = agreeCollectService.isAgree(agreeCollectDTO);
        return Result.ok(flag);
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
        Page<AgreeCollectVo> pageInfo = agreeCollectService.getAllAgreeAndCollection(page, limit, uid);
        return Result.ok(pageInfo);
    }

    /**
     * 取消点赞
     *
     * @param agreeCollectDTO
     * @return
     */
    @RequestMapping("cancelAgree")
    public Result<?> cancelAgree(@RequestBody AgreeCollectDTO agreeCollectDTO) {
        ValidatorUtils.validateEntity(agreeCollectDTO, DefaultGroup.class);
        agreeCollectService.cancelAgree(agreeCollectDTO);
        return Result.ok(null);
    }


    /**
     * 得到所有的收藏
     *
     * @param page
     * @param limit
     * @param uid
     * @param type
     * @return
     */
    @RequestMapping("getAllCollection/{page}/{limit}")
    public Result<?> getAllCollection(@PathVariable long page, @PathVariable long limit, String uid, Integer type) {
        Page<AgreeCollectVo> pageInfo = agreeCollectService.getAllCollection(page, limit, uid, type);
        return Result.ok(pageInfo);
    }

    /**
     * 收藏
     *
     * @param agreeCollectDTO
     * @return
     */
    @RequestMapping("collection")
    public Result<?> collection(@RequestBody AgreeCollectDTO agreeCollectDTO) {
        Map<String, String> res = agreeCollectService.collection(agreeCollectDTO);
        return Result.ok(res);
    }

    /**
     * 取消收藏
     *
     * @param agreeCollectDTO
     * @returne
     */
    @RequestMapping("cancelCollection")
    public Result<?> cancelCollection(@RequestBody AgreeCollectDTO agreeCollectDTO) {
        Map<String, String> map = agreeCollectService.cancelCollection(agreeCollectDTO);
        return Result.ok(map);
    }
}
