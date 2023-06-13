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
import com.xz.platform.vo.ImgDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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


    static final String HotKEY = ImgDetailCacheNames.HOT;
    static final String viewRecordKey = ImgDetailCacheNames.IMG_VIEW_RECORD;
    @Async
    @Scheduled(fixedDelay = 1000*60)  //间隔1分
    public void updateViewRecord() {

        Set<String> listKey = redisUtils.getListKey(viewRecordKey);
        if (!listKey.isEmpty()) {
            for (String k : listKey) {
                    String mid = k.split(":")[1];
                    ImgDetailsEntity imgDetailsEntity = imgDetailsDao.selectById(mid);
                    imgDetailsEntity.setViewCount(Long.valueOf(redisUtils.get(k)));
                    imgDetailsDao.updateById(imgDetailsEntity);
            }
        }
    }


    @Async
    @Scheduled(fixedDelay = 1000*60*10)  //10分钟刷新一次热榜
    public void refreshHot(){

        List<ImgDetailsEntity> imgDetailList = imgDetailsDao.selectList(new QueryWrapper<ImgDetailsEntity>().orderByDesc("agree_count").ge("agree_count", 1));
        List<ImgDetailVo> list = new ArrayList<>();

        List<Long> uids = imgDetailList.stream().map(ImgDetailsEntity::getUserId).collect(Collectors.toList());

        List<UserEntity> userList = userDao.selectBatchIds(uids);

        HashMap<Long,UserEntity> userMap = new HashMap<>();
        userList.forEach(item->{
            userMap.put(item.getId(),item);
        });

        ImgDetailVo imgDetailVo;
        List<String> imgList;
        UserEntity userEntity;
        for (ImgDetailsEntity model : imgDetailList) {
            imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            userEntity = userMap.get(model.getUserId());
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            imgDetailVo.setImgsUrl(imgList)
                    .setUsername(userEntity.getUsername())
                    .setAvatar(userEntity.getAvatar());
            list.add(imgDetailVo);
        }

        redisUtils.set(HotKEY,JSON.toJSONString(list));
    }


}
