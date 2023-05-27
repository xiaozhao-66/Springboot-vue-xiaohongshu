package com.xz.platform.common.task;


import com.alibaba.fastjson.JSON;
import com.xz.common.constant.cacheConstant.ImgDetailCacheNames;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.dao.ImgDetailsDao;
import com.xz.platform.entity.ImgDetailsEntity;
import com.xz.platform.vo.ImgDetailInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class ImgDetailTask {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    ImgDetailsDao imgDetailsDao;

    static final String KEY = ImgDetailCacheNames.IMG_DETAIL;

    @Async
    @Scheduled(fixedDelay = 1000)  //间隔1秒
    public void updateViewRecord() {

        Set<String> listKey = redisUtils.getListKey(KEY);
        if (!listKey.isEmpty()) {
            for (String k : listKey) {
                if (redisUtils.getExpire(k) <= 4) {
                    String strObject = redisUtils.get(k);
                    ImgDetailInfoVo imgDetailInfoVo = JSON.parseObject(strObject, ImgDetailInfoVo.class);
                    ImgDetailsEntity imgDetailsEntity = imgDetailsDao.selectById(imgDetailInfoVo.getId());
                    // imgDetailsEntity.setViewCount(imgDetailInfoVo.getViewCount());
                    imgDetailsEntity.setAgreeCount(imgDetailInfoVo.getAgreeCount());
                    imgDetailInfoVo.setCollectionCount(imgDetailInfoVo.getCollectionCount());
                    imgDetailsDao.updateById(imgDetailsEntity);
                }
            }
        }
    }
}
