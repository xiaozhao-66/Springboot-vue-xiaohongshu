package com.xz.search.controller;


import com.xz.common.utils.Result;
import com.xz.search.service.EsService;
import com.xz.search.vo.ImgDetailSearchVo;
import com.xz.search.vo.SearchRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 48423
 */
@RequestMapping("searchRecord")
@RestController
public class SearchRecordEsController {

    @Autowired
    EsService esService;

    @RequestMapping("addSearchRecordData")
    public void addSearchRecordData(String keyword) throws IOException {
        esService.addSearchRecordData(keyword);
    }

    @RequestMapping("esSearchRecord")
    public Result<?> esSearchRecord(String keyword) throws IOException {
        List<Map<String, Object>> res = esService.esSearchRecord(keyword);
        return new Result<List<Map<String, Object>>>().ok(res);
    }
}
