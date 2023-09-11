package com.yanhuo.platform.controller;


import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.platform.service.AlbumImgRelationService;
import com.yanhuo.xo.dto.platform.AlbumImgRelationDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/api/platform/albumImgRelation")
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
        return Result.ok(null);
    }

    /**
     * 当前用户是否已经收藏图片至专辑中
     *
     * @return
     */
    @RequestMapping("isCollectImgToAlbum")
    public Result<?> isCollectImgToAlbum(String uid, String mid) {
        boolean flag = albumImgRelationService.isCollectImgToAlbum(uid, mid);
        return Result.ok(flag);
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
        return Result.ok(null);
    }
}