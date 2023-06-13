package com.xz.platform.common.client;

import com.xz.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(value = "ccimgcloud-utils")
@Component
public interface OSSClient {

    @RequestMapping("/utils/qiniufileoss/deleteFile")
    Result<?> deleteFile(@RequestParam String fileName);

}
