package com.yanhuo.platform.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.xo.dto.platform.BrowseRecordDTO;
import com.yanhuo.xo.vo.ImgDetailVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/api/platform/browseRecord")
@Api(tags="浏览记录")
public class BrowseRecordController {

    @Autowired
    private ImgDetailService imgDetailService;


    @RequestMapping("getAllBrowseRecordByUser/{page}/{limit}")
    public Result<?> getAllBrowseRecordByUser(@PathVariable("page") long page, @PathVariable("limit") long limit, String uid) {
        List<ImgDetailVo> browseRecordVoList = imgDetailService.getAllBrowseRecordByUser(page, limit, uid);
        return Result.ok(browseRecordVoList);
    }


    @RequestMapping("addBrowseRecord")
    public Result<?> addBrowseRecord(@RequestBody BrowseRecordDTO browseRecordDTO) {
        imgDetailService.addBrowseRecord(browseRecordDTO);
        return Result.ok(null);
    }


    /**
     * 删除浏览记录
     *
     * @param idList
     * @return
     */
    @RequestMapping("delRecord/{uid}")
    public Result<?> delRecord(@RequestBody List<String> idList, @PathVariable String uid) {
        imgDetailService.delRecord(uid, idList);
        return Result.ok(null);
    }
}