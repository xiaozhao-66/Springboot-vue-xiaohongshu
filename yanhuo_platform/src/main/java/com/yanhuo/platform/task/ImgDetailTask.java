package com.yanhuo.platform.task;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.model.ImgDetail;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.vo.ImgDetailVo;
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
    ImgDetailService imgDetailService;

    @Autowired
    UserService userService;

    @Async
    @Scheduled(fixedDelay = 1000*60)  //间隔1分
    public void updateViewRecord() {

        Set<String> listKey = redisUtils.getListKey(PlatformConstant.IMG_VIEW_RECORD);
        if (!listKey.isEmpty()) {
            for (String k : listKey) {
                    String mid = k.split(":")[1];
                    ImgDetail imgDetail = imgDetailService.getById(mid);
                    imgDetail.setViewCount(Long.valueOf(redisUtils.get(k)));
                     imgDetailService.updateById(imgDetail);
            }
        }
    }


    @Async
    @Scheduled(fixedDelay = 1000*60*10)  //10分钟刷新一次热榜
    public void refreshHot(){

        List<ImgDetail> imgDetailList = imgDetailService.list(new QueryWrapper<ImgDetail>().orderByDesc("agree_count").ge("agree_count", 1));
        List<ImgDetailVo> list = new ArrayList<>();

        List<Long> uids = imgDetailList.stream().map(ImgDetail::getUserId).collect(Collectors.toList());

        List<User> userList = userService.listByIds(uids);

        HashMap<Long,User> userMap = new HashMap<>();
        userList.forEach(item->{
            userMap.put(item.getId(),item);
        });

        ImgDetailVo imgDetailVo;
        List<String> imgList;
        User userEntity;
        for (ImgDetail model : imgDetailList) {
            imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            userEntity = userMap.get(model.getUserId());
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            imgDetailVo.setImgsUrl(imgList)
                    .setUsername(userEntity.getUsername())
                    .setAvatar(userEntity.getAvatar());
            list.add(imgDetailVo);
        }

        redisUtils.set(PlatformConstant.HOT,JSON.toJSONString(list));
    }


}
