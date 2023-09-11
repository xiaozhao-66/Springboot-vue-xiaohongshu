package com.yanhuo.search.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.search.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 48423
 */
@RequestMapping("/api/search/searchRecord")
@RestController
public class SearchRecordEsController {

    @Autowired
    EsService esService;

    /**
     * 增加搜索记录
     *
     * @param keyword
     * @throws IOException
     */
    @RequestMapping("addSearchRecordData")
    public void addSearchRecordData(String keyword) throws IOException {
        esService.addSearchRecordData(keyword);
    }

    /**
     * 搜索记录
     *
     * @param keyword
     * @return
     * @throws IOException
     */
    @RequestMapping("esSearchRecord")
    public Result<?> esSearchRecord(String keyword) throws IOException {
        List<Map<String, Object>> res = esService.esSearchRecord(keyword);
        return Result.ok(res);
    }
}
