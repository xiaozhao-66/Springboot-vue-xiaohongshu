package com.yanhuo.recommend.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.recommend.service.RecommendService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/recommend/newRecommend")
@Api(tags="使用机器学习模型重做推荐系统")
public class RecommendController {

    @Autowired
    RecommendService recommendService;

    /**
     * 暂时随机推荐
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @RequestMapping("RecommendToUserByCF/{page}/{limit}")
    public Result<?> RecommendToUserByCF(@PathVariable long page, @PathVariable long limit, String uid){
        HashMap<String, Object> map = recommendService.recommendToUserByCF(page, limit, uid);
        return Result.ok(map);
    }

    /**
     * 推荐功能
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @RequestMapping("newRecommendToUser/{page}/{limit}")
    public Result<?> newRecommendToUser(@PathVariable long page, @PathVariable long limit, String uid){
        HashMap<String, Object> map = recommendService.recommendToUser(page, limit, uid);
        return Result.ok(map);
    }
}
