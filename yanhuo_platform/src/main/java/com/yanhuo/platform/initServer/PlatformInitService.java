package com.yanhuo.platform.initServer;

import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.platform.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component  //被 spring 容器管理
@Order(1)   //如果多个自定义的 ApplicationRunner  ，用来标明执行的顺序
public class PlatformInitService implements ApplicationRunner {

    private static Logger logger =  LoggerFactory.getLogger(PlatformInitService.class);

    @Autowired
    ImgDetailService imgDetailService;

    @Autowired
    UserService userService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("===先启动search服务再启动platform服务======");
        imgDetailService.addBulkData();
        imgDetailService.addBulkRedisData();
        userService.addBulkUserRecord();
    }

}