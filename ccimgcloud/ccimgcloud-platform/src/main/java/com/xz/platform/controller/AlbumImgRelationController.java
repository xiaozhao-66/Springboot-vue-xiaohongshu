package com.xz.platform.controller;

import com.xz.common.utils.Result;
import com.xz.common.validator.ValidatorUtils;
import com.xz.common.validator.group.DefaultGroup;
import com.xz.platform.dto.AlbumImgRelationDTO;
import com.xz.platform.service.AlbumImgRelationService;
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
@RequestMapping("/albumimgrelation")
@Api(tags="专辑图片联系模块")
public class AlbumImgRelationController {
    @Autowired
    private AlbumImgRelationService albumImgRelationService;

    /**
     * 增加联系
     *
     * @param albumImgRelationDTO
     * @return
     */
    @RequestMapping("addAlbumImgRelation")
    public Result<?> addAlbumImgRelation(@RequestBody AlbumImgRelationDTO albumImgRelationDTO) {
        ValidatorUtils.validateEntity(albumImgRelationDTO, DefaultGroup.class);
        albumImgRelationService.addAlbumImgRelation(albumImgRelationDTO);
        return new Result<>().ok();
    }

    /**
     * 当前用户是否已经收藏图片至专辑中
     *
     * @return
     */
    @RequestMapping("isCollectImgToAlbum")
    public Result<?> isCollectImgToAlbum(String uid, String mid) {
        boolean flag = albumImgRelationService.isCollectImgToAlbum(uid, mid);
        return new Result<Boolean>().ok(flag);
    }


    /**
     * 删除关系
     *
     * @param albumImgRelationDTO
     * @return
     */
    @RequestMapping("deleteAlbumImgRelation")
    public Result<?> deleteAlbumImgRelation(@RequestBody AlbumImgRelationDTO albumImgRelationDTO) {
        ValidatorUtils.validateEntity(albumImgRelationDTO, DefaultGroup.class);
        albumImgRelationService.deleteAlbumImgRelation(albumImgRelationDTO);
        return new Result<>().ok();
    }
}