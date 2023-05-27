package com.xz.platform.common.client;

import com.xz.platform.vo.ImgDetailSearchVo;
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

    @RequestMapping("/search/search/addBulkData")
    void addBulkData(@RequestBody List<ImgDetailSearchVo> dataList) throws IOException;

//    @RequestMapping("/search/search/esSearch/{page}/{limit}")
//    List<ImgDetailSearchVo> esSearch(@PathVariable long page, @PathVariable long limit, String keyword) throws IOException;

    @RequestMapping("/search/search/addData")
    void addData(@RequestBody ImgDetailSearchVo imgDetailSearchVo) throws IOException;

    @RequestMapping("/search/search/delData/{id}")
    void delData(@PathVariable String id) throws IOException;

    @RequestMapping("/search/search/update")
    void update(@RequestBody ImgDetailSearchVo imgDetailSearchVo) throws IOException;
}
