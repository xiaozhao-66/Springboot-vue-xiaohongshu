package com.xz.platform;

import com.alibaba.fastjson.JSON;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.utils.JsonUtils;
import com.yanhuo.common.utils.SendMessageMq;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.platform.PlatformApplication;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.model.ImgDetail;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.vo.UserRecordVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = PlatformApplication.class)
public class PlatfromApplicationTests {

    @Autowired
    ImgDetailService imgDetailService;

    @Autowired
    UserService userService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    SendMessageMq sendMessageMq;


    //插入所有的图片信息
    @Test
    public void test(){



    }


    @Test
    public void test2(){

        List<User> list = userService.list();

        for (User user : list) {
            String userRecordKey = PlatformConstant.USER_RECORD + user.getId();
            UserRecordVo userRecordVo = new UserRecordVo();
            userRecordVo.setAgreeCollectionCount(0L);
            userRecordVo.setAddFollowCount(0L);
            userRecordVo.setNoreplyCount(0L);
            redisUtils.set(userRecordKey, JsonUtils.toJsonString(userRecordVo));
        }

    }

    @Test
    public void test3(){
        sendMessageMq.sendMessage("yanhuo.test.direct","testKey","test");
    }
}
