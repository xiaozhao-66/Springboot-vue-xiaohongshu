package com.xz.recommend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.utils.Result;
import com.xz.recommend.service.ImgDetailsService;
import com.xz.recommend.vo.ImgDetailVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("recommend")
@Api(tags="推送模块")
public class ImgDetailsController {


    @Autowired
    ImgDetailsService imgDetailsService;


    /**
     * 旧的推荐系统（使用协同过滤算法）
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @RequestMapping("recommendToUser/{page}/{limit}")
    public Result<?> recommendToUser(@PathVariable long page, @PathVariable long limit, String uid) {
        Page<ImgDetailVo> pageInfo = imgDetailsService.recommendToUser(page, limit, uid);
        return new Result<Page<ImgDetailVo>>().ok(pageInfo);
    }


    /**
     * 旧的推荐方式:根据文本相似性计算
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @RequestMapping("recommendToUser2/{page}/{limit}")
    public Result<?> recommendToUser2(@PathVariable long page, @PathVariable long limit, String uid) {
        Page<ImgDetailVo> pageInfo = imgDetailsService.recommendToUser2(page, limit, uid);
        return new Result<Page<ImgDetailVo>>().ok(pageInfo);
    }
}
