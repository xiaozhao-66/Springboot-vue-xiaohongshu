package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.utils.SendMessageMq;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.AddGroup;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.common.validator.group.UpdateGroup;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.xo.dto.platform.ImgDetailDTO;
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
 * @since 1.0.0 2023-03-13
 */
@RestController
@RequestMapping("/api/platform/imgDetail")
@Api(tags="图片信息模块")
public class ImgDetailController {

    @Autowired
    private ImgDetailService imgDetailService;


    @Autowired
    SendMessageMq sendMessageMq;


    /**
     * 获取所有的图片信息
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("getPage/{page}/{limit}")
    public Result<?> getPage(@PathVariable long page, @PathVariable long limit) {
        Page<ImgDetailVo> pageData = imgDetailService.getPage(page, limit);
        return Result.ok(pageData);
    }

    /**
     * 根据专辑获取所有的图片信息
     *
     * @param page
     * @param limit
     * @param albumId
     * @return
     */
    @RequestMapping("getAllImgByAlbum/{page}/{limit}")
    public Result<?> getAllImgByAlbum(@PathVariable long page, @PathVariable long limit, String albumId, Integer type) {
        Page<ImgDetailVo> pageInfo = imgDetailService.getAllImgByAlbum(page, limit, albumId, type);
        return Result.ok(pageInfo);
    }

    /**
     * 获取图片信息
     *
     * @param id
     * @return
     */
    @RequestMapping("getOne")
    public Result<?> getOne(String id) {
        ImgDetailVo imgDetailVo = imgDetailService.getImgDetail(id);
        return Result.ok(imgDetailVo);
    }

    /**
     * 发布图片
     *
     * @param imgDetailsDTO
     * @return
     */
    @RequestMapping("publish")
    public Result<?> publish(@RequestBody ImgDetailDTO imgDetailsDTO) {
        ValidatorUtils.validateEntity(imgDetailsDTO, AddGroup.class, DefaultGroup.class);
        Long id = imgDetailService.publish(imgDetailsDTO);
        return Result.ok(id);
    }


    /**
     * 删除图片
     *
     * @param ids
     * @param uid
     * @return
     */
    @RequestMapping("deleteImgs/{uid}")
    public Result<?> deleteImgs(@RequestBody List<String> ids, @PathVariable String uid) {
        imgDetailService.deleteImgs(ids, uid);
        return Result.ok(null);
    }

    /**
     * 更新图片
     *
     * @param imgDetailDTO
     * @return
     */
    @RequestMapping("updateImgDetail")
    public Result<?> updateImgDetail(@RequestBody ImgDetailDTO imgDetailDTO) {
        ValidatorUtils.validateEntity(imgDetailDTO, UpdateGroup.class, DefaultGroup.class);
        Long id = imgDetailService.updateImgDetail(imgDetailDTO);
        return Result.ok(id);
    }

    /**
     * 热榜信息
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("getHot/{page}/{limit}")
    public Result<?> getHot(@PathVariable long page, @PathVariable long limit) {
        Page<ImgDetailVo> pageInfo = imgDetailService.getHot(page, limit);
        return Result.ok(pageInfo);
    }

//暂时不用
//    @RequestMapping("updateStatus")
//    public Result<?> updateStatus(@RequestBody ImgDetailDTO imgDetailsDTO) {
//        imgDetailsService.updateStatus(imgDetailsDTO);
//        return Result.ok(null);
//    }

    // 暂时用不到mq
//    @RequestMapping("addMQTask")
//    public Result<?> addMQTask(@RequestBody ImgDetailDTO imgDetailDTO) {
//        sendMessageMq.sendMessage("yanhuo.upload.direct","yanhuo.upload",imgDetailDTO);
//        return Result.ok(null);
//    }
}