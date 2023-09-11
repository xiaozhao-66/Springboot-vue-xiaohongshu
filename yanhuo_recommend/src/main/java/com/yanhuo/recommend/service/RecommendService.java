package com.yanhuo.recommend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.model.ImgDetail;

import java.util.HashMap;

/**
 * @author xiaozhao 484235492@qq.com
 */
public interface RecommendService extends IService<ImgDetail> {

    /**
     * 暂时随机推荐
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    HashMap<String, Object> recommendToUserByCF(long page, long limit, String uid);

    /**
     * 使用机器学习模型做推荐系统
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    HashMap<String, Object> recommendToUser(long page, long limit, String uid);


}
