package com.xz.search.controller;

import com.xz.common.utils.Result;
import com.xz.search.dto.ImgDetailSearchDTO;
import com.xz.search.service.EsService;
import com.xz.search.vo.ImgDetailSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RequestMapping("search")
@RestController
public class EsController {

    @Autowired
    EsService esService;

    @RequestMapping("addBulkData")
    public void addBulkData(@RequestBody List<ImgDetailSearchVo> dataList) throws IOException {
        esService.addbulkData(dataList);
    }


    @RequestMapping("esSearch/{page}/{limit}")
    public Result<?> esSearch(@PathVariable long page, @PathVariable long limit, @RequestBody ImgDetailSearchDTO imgDetailSearchDTO) throws IOException {
        HashMap<String, Object> map = esService.esSearch(page, limit, imgDetailSearchDTO);
        return new Result<HashMap<String, Object>>().ok(map);
    }

    @RequestMapping("getPage/{page}/{limit}/{type}")
    public List<ImgDetailSearchVo> getPage(@PathVariable long page, @PathVariable long limit, @PathVariable Integer type) throws IOException {
        return esService.getPage(page, limit, type);
    }

    @RequestMapping("esSearchList")
    public List<ImgDetailSearchVo> esSearchList() throws IOException {
        return esService.esSearchList();
    }

    @RequestMapping("addData")
    public void addData(@RequestBody ImgDetailSearchVo imgDetailSearchVo) throws IOException {
        esService.addData(imgDetailSearchVo);
    }

    @RequestMapping("delData/{id}")
    public void delData(@PathVariable String id) throws IOException {
        esService.delData(id);
    }

    @RequestMapping("update")
    public void update(@RequestBody ImgDetailSearchVo imgDetailSearchVo) throws IOException {
        esService.update(imgDetailSearchVo);
    }
}
