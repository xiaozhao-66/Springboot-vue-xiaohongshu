package com.xz.recommend.service;

import com.xz.common.service.BaseService;
import com.xz.recommend.entity.ImgDetailsEntity;

import java.util.HashMap;

/**
 * @author 48423
 */
public interface RecommendService extends BaseService<ImgDetailsEntity> {

    HashMap<String, Object> newRecommendToUser(long page, long limit, String uid);
}
