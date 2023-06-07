package com.xz.platform.controller;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.utils.Result;
import com.xz.common.validator.ValidatorUtils;
import com.xz.common.validator.group.AddGroup;
import com.xz.common.validator.group.DefaultGroup;
import com.xz.common.validator.group.UpdateGroup;
import com.xz.platform.dto.ImgDetailsDTO;
import com.xz.platform.service.ImgDetailsService;
import com.xz.platform.vo.ImgDetailInfoVo;
import com.xz.platform.vo.ImgDetailSearchVo;
import com.xz.platform.vo.ImgDetailVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@RestController
@RequestMapping("imgdetails")
@Api(tags="图片信息模块")
public class ImgDetailsController {

    @Autowired
    private ImgDetailsService imgDetailsService;


    /**
     * 获取所有的图片信息
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("getPage/{page}/{limit}")
    public Result<?> getPage(@PathVariable long page, @PathVariable long limit) {

        Page<ImgDetailVo> pageData = imgDetailsService.getPage(page, limit);
        return new Result<Page<ImgDetailVo>>().ok(pageData);
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
    public Result<?> getAllImgByAlbum(@PathVariable long page, @PathVariable long limit, String albumId) {
        List<ImgDetailVo> imgDetailVoList = imgDetailsService.getAllImgByAlbum(page, limit, albumId);
        return new Result<List<ImgDetailVo>>().ok(imgDetailVoList);
    }

    /**
     * 获取图片信息
     *
     * @param id
     * @return
     */
    @RequestMapping("getOne")
    public Result<?> getOne(String id) {
        ImgDetailInfoVo imgDetailVo = imgDetailsService.getOne(id);
        return new Result<ImgDetailInfoVo>().ok(imgDetailVo);
    }

    /**
     * 发布图片
     *
     * @param imgDetailsDTO
     * @return
     */
    @RequestMapping("publish")
    public Result<?> publish(@RequestBody ImgDetailsDTO imgDetailsDTO) throws MalformedModelException, ModelNotFoundException, IOException, TranslateException {
        ValidatorUtils.validateEntity(imgDetailsDTO, AddGroup.class, DefaultGroup.class);
        imgDetailsService.publish(imgDetailsDTO);
        return new Result<>().ok();
    }

    /**
     * 搜索图片
     * @param page
     * @param limit
     * @param keyword
     * @return
     */
//    @RequestMapping("search/{page}/{limit}")
//    public Result<?> search(@PathVariable long page,@PathVariable long limit,String keyword){
//        List<ImgDetailSearchVo> list = imgDetailsService.search(page,limit,keyword);
//        return new Result<List<ImgDetailSearchVo>>().ok(list);
//    }

    /**
     * 删除图片
     *
     * @param ids
     * @param uid
     * @return
     */
    @RequestMapping("deleteImgs/{uid}")
    public Result<?> deleteImgs(@RequestBody String[] ids, @PathVariable String uid) {
        imgDetailsService.deleteImgs(ids, uid);
        return new Result<>().ok();
    }

    /**
     * 更新图片
     *
     * @param imgDetailsDTO
     * @return
     */
    @RequestMapping("updateImgDetail")
    public Result<?> updateImgDetail(@RequestBody ImgDetailsDTO imgDetailsDTO) throws MalformedModelException, ModelNotFoundException, IOException, TranslateException {
        ValidatorUtils.validateEntity(imgDetailsDTO, UpdateGroup.class, DefaultGroup.class);
        imgDetailsService.updateImgDetail(imgDetailsDTO);
        return new Result<>().ok();
    }

    /**
     * 批量增加图片信息
     *
     * @return
     */
    @RequestMapping("addBulkData")
    public Result<?> addBulkData() {
        imgDetailsService.addBulkData();
        return new Result<>().ok();
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
        Page<ImgDetailVo> pageInfo = imgDetailsService.getHot(page, limit);
        return new Result<>().ok(pageInfo);
    }
}