package com.xz.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author xiaozhao
 */
@SpringBootApplication(scanBasePackages = {"com.xz.platform","com.xz.common"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
public class PlatFormApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatFormApplication.class, args);
    }

}
