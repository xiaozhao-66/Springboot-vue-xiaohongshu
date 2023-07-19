package com.yanhuo.platform.client;

import com.yanhuo.xo.vo.ImgDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@FeignClient(value = "yanhuo-search")
@Component
public interface EsClient {

    @RequestMapping("/api/search/imgDetailSearch/addBulkData")
    void addBulkData(@RequestBody List<ImgDetailVo> dataList) throws IOException;

    @RequestMapping("/api/search/imgDetailSearch/addData")
    void addData(@RequestBody ImgDetailVo imgDetailSearchVo) throws IOException;

    @RequestMapping("/api/search/imgDetailSearch/delData/{id}")
    void delData(@PathVariable String id) throws IOException;

    @RequestMapping("/api/search/imgDetailSearch/update")
    void update(@RequestBody ImgDetailVo imgDetailSearchVo) throws IOException;
}
