package com.xz.platform.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.utils.Result;
import com.xz.common.validator.ValidatorUtils;
import com.xz.common.validator.group.DefaultGroup;
import com.xz.platform.dto.BrowseRecordDTO;
import com.xz.platform.service.BrowseRecordService;
import com.xz.platform.vo.BrowseRecordVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/browserecord")
@Api(tags="浏览记录")
public class BrowseRecordController {

    @Autowired
    private BrowseRecordService browseRecordService;


    /**
     * 得到当前用户所有的浏览记录
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @RequestMapping("getAllBrowseRecordByUser/{page}/{limit}")
    public Result<?> getAllBrowseRecordByUser(@PathVariable("page") long page, @PathVariable("limit") long limit, String uid) {
        Page<BrowseRecordVo> pageInfo = browseRecordService.getAllBrowseRecordByUser(page, limit, uid);
        return new Result<Page<BrowseRecordVo>>().ok(pageInfo);
    }


    /**
     * 添加浏览记录
     *
     * @param browseRecordDTO
     * @return
     */
    @RequestMapping("addBrowseRecord")
    public Result<?> addBrowseRecord(@RequestBody BrowseRecordDTO browseRecordDTO) {
        ValidatorUtils.validateEntity(browseRecordDTO, DefaultGroup.class);
        browseRecordService.addBrowseRecord(browseRecordDTO);
        return new Result<>().ok();
    }

    /**
     * 删除浏览记录
     *
     * @param ids
     * @return
     */
    @RequestMapping("delRecord")
    public Result<?> delRecord(@RequestBody String[] ids) {
        browseRecordService.delRecord(ids);
        return new Result<>().ok();
    }
}