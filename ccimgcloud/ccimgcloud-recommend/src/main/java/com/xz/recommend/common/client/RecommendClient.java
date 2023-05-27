package com.xz.recommend.common.client;


import com.xz.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 48423
 */
@FeignClient(value = "ccimgcloud-platform")
@Component
public interface RecommendClient {


    @RequestMapping("/platform/imgdetails/getPage/{page}/{limit}")
    public Result<?> getPage(@PathVariable long page, @PathVariable long limit);
}
