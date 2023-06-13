package com.xz.platform.daoTest;

import com.alibaba.fastjson.JSON;
import com.xz.common.constant.cacheConstant.ImgDetailCacheNames;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.dao.ImgDetailsDao;
import com.xz.platform.dao.UserDao;
import com.xz.platform.entity.ImgDetailsEntity;
import com.xz.platform.entity.UserEntity;
import com.xz.platform.vo.ImgDetailSearchVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class RedisTest {


    @Autowired
    ImgDetailsDao imgDetailsDao;

    @Autowired
    UserDao userDao;

    @Autowired
    RedisUtils redisUtils;


    //插入所有的图片信息
    @Test
    public void test(){

        List<ImgDetailsEntity> imgDetailList = imgDetailsDao.selectList(null);



        List<Long> uids = imgDetailList.stream().map(ImgDetailsEntity::getUserId).collect(Collectors.toList());

        List<UserEntity> userList = userDao.selectBatchIds(uids);

        HashMap<Long,UserEntity> userMap = new HashMap<>();

        userList.forEach(item->{
            userMap.put(item.getId(),item);
        });

        String key = "imgDetailListKey";

        for (ImgDetailsEntity model: imgDetailList) {


//            ImgDetailSearchVo imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailSearchVo.class);
//            UserEntity userEntity = userMap.get(model.getUserId());
//            imgDetailVo.setUsername(userEntity.getUsername())
//                       .setAvatar(userEntity.getAvatar());
            redisUtils.hPut(key,String.valueOf(model.getId()), JSON.toJSONString(model));

        }

    }
}
