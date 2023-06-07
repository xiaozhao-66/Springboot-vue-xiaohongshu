package com.xz.platform.common.task;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.common.constant.cacheConstant.ImgDetailCacheNames;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.dao.ImgDetailsDao;
import com.xz.platform.dao.UserDao;
import com.xz.platform.entity.ImgDetailsEntity;
import com.xz.platform.entity.UserEntity;
import com.xz.platform.vo.ImgDetailInfoVo;
import com.xz.platform.vo.ImgDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class ImgDetailTask {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    ImgDetailsDao imgDetailsDao;

    @Autowired
    UserDao userDao;

    static final String KEY = ImgDetailCacheNames.IMG_DETAIL;
    static final String HotKEY = ImgDetailCacheNames.HOT;

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
                    imgDetailsEntity.setAgreeCount(imgDetailInfoVo.getAgreeCount());
                    imgDetailInfoVo.setCollectionCount(imgDetailInfoVo.getCollectionCount());
                    imgDetailsDao.updateById(imgDetailsEntity);
                }
            }
        }
    }

    @Async
    @Scheduled(fixedDelay = 1000*60*10)  //10分钟刷新一次热榜
    public void refreshHot(){

        List<ImgDetailsEntity> imgDetailsEntityList = imgDetailsDao.selectList(new QueryWrapper<ImgDetailsEntity>().orderByDesc("agree_count").ge("agree_count", 1));
        List<ImgDetailVo> list = new ArrayList<>();
        ImgDetailVo imgDetailVo = null;
        List<String> imgList = null;
        UserEntity userEntity = null;
        for (ImgDetailsEntity model : imgDetailsEntityList) {
            imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            userEntity = userDao.selectById(model.getUserId());
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            imgDetailVo.setImgsUrl(imgList)
                    .setUsername(userEntity.getUsername())
                    .setAvatar(userEntity.getAvatar());
            list.add(imgDetailVo);
        }

        redisUtils.set(HotKEY,JSON.toJSONString(list));
    }


}
