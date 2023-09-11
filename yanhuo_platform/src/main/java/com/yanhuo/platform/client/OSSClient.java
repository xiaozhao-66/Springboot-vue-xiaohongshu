package com.yanhuo.platform.client;

import com.yanhuo.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "yanhuo-util")
@Component
public interface OSSClient {

    @RequestMapping("/api/util/fileoss/deleteFile/{type}")
    Result<?> deleteFile(@RequestParam String fileName, @PathVariable Integer type);
}
