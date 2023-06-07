package com.xz.recommend.common.client;


import com.xz.recommend.vo.ImgDetailSearchVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@FeignClient(value = "ccimgcloud-search")
@Component
public interface EsClient {

    @RequestMapping("/search/search/esSearchList")
    List<ImgDetailSearchVo> esSearchList() throws IOException;

    @RequestMapping("/search/search/getPage/{page}/{limit}/{type}")
    List<ImgDetailSearchVo> getPage(@PathVariable long page, @PathVariable long limit, @PathVariable Integer type) throws IOException;
}
