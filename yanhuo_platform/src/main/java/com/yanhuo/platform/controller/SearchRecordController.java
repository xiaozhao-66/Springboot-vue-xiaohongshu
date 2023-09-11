package com.yanhuo.platform.controller;



import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.AddGroup;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.dto.search.SearchRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/platform/searchRecord")
public class SearchRecordController {

    @Autowired
    UserService userService;

    /**
     * 得到当前用户所有的搜索记录
     */
    @RequestMapping("getAllSearchRecord")
    public Result<?> getAllSearchRecord(String uid) {
        List<String> result = userService.getAllSearchRecord(uid);
        return Result.ok(result);
    }


    @RequestMapping("addSearchRecord")
    public Result<?> addSearchRecord(@RequestBody SearchRecordDTO searchRecordDTO) {
        ValidatorUtils.validateEntity(searchRecordDTO,DefaultGroup.class);
        userService.addSearchRecord(searchRecordDTO);
        return Result.ok(null);
    }

    @RequestMapping("deleteSearchRecord")
    public Result<?> deleteSearchRecord(@RequestBody List<String> words, String uid) {
        userService.deleteSearchRecord(words, uid);
        return Result.ok(null);
    }

}
