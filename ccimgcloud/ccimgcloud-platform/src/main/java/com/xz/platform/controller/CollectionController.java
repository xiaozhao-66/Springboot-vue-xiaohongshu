package com.xz.platform.controller;

import com.xz.common.utils.Result;
import com.xz.platform.dto.CollectionDTO;
import com.xz.platform.service.CollectionService;
import com.xz.platform.vo.CollectionVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/collection")
@Api(tags="收藏模块")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;


    /**
     * 得到所有的收藏专辑或图片收藏专辑
     *
     * @param page
     * @param limit
     * @param uid
     * @param type
     * @return
     */
    @RequestMapping("getAllCollection/{page}/{limit}")
    public Result<?> getAllCollection(@PathVariable long page, @PathVariable long limit, String uid, Integer type) {
        List<CollectionVo> collectionVoList = collectionService.getAllCollection(page, limit, uid, type);
        return new Result<List<CollectionVo>>().ok(collectionVoList);
    }

    /**
     * 收藏专辑
     *
     * @param collectionDTO
     * @return
     */
    @RequestMapping("collection")
    public Result<?> collection(@RequestBody CollectionDTO collectionDTO) {
        Map<String, String> res = collectionService.collection(collectionDTO);
        return new Result<>().ok(res);
    }

    /**
     * 取消收藏
     *
     * @param collectionDTO
     * @return
     */
    @RequestMapping("cancalCollection")
    public Result<?> cancelCollection(@RequestBody CollectionDTO collectionDTO) {
        Map<String, String> map = collectionService.cancelCollection(collectionDTO);
        return new Result<>().ok(map);
    }
}