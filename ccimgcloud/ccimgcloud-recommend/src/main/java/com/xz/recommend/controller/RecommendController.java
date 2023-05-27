package com.xz.recommend.controller;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import com.xz.common.utils.RedisUtils;
import com.xz.common.utils.Result;
import com.xz.recommend.common.client.RecommendClient;
import com.xz.recommend.service.ImgDetailsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author 48423
 */
@RestController
@RequestMapping("newRecommend")
@Api(tags="使用机器学习模型重做推荐系统")
public class RecommendController {

    @Autowired
    ImgDetailsService imgDetailsService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RecommendClient recommendClient;

    /**
     * 只看这一个方法就行了
     * @param page
     * @param limit
     * @param uid
     * @return
     * @throws ModelException
     * @throws IOException
     * @throws TranslateException
     */
    @RequestMapping("newRecommendToUser/{page}/{limit}")
    public Result<?> newRecommendToUser(@PathVariable long page, @PathVariable long limit, String uid) throws ModelException, IOException, TranslateException {

        String ukey = "brimg:" + uid;

        if (Boolean.FALSE.equals(redisUtils.hasKey(ukey))) {
            return recommendClient.getPage(page, limit);
        }

        HashMap<String, Object> map = imgDetailsService.newRecommendToUser(page, limit, uid);
        return new Result<HashMap<String, Object>>().ok(map);
    }
}
