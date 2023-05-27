package com.xz.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.utils.Result;
import com.xz.platform.dto.SearchRecordDTO;
import com.xz.platform.service.SearchRecordService;
import com.xz.platform.vo.ImgDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/searchRecord")
public class SearchRecordController {

    @Autowired
    SearchRecordService searchRecordService;


    /**
     * 得到当前用户所有的搜索记录
     */
    @RequestMapping("getAllSearchRecord")
    public Result<?> getAllSearchRecord(String uid) {

        List<String> result = searchRecordService.getAllSearchRecord(uid);
        return new Result<List<String>>().ok(result);
    }


    @RequestMapping("addSearchRecord")
    public Result<?> addSearchRecord(@RequestBody SearchRecordDTO searchRecordDTO) {
        searchRecordService.addSearchRecord(searchRecordDTO);
        return new Result<>().ok();
    }

    @RequestMapping("deleteSearchRecord")
    public Result<?> deleteSearchRecord(@RequestBody List<String> words, String uid) {
        searchRecordService.deleteSearchRecord(words, uid);
        return new Result<>().ok();
    }
}
