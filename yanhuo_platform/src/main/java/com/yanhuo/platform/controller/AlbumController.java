package com.yanhuo.platform.controller;


import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.platform.service.AlbumService;
import com.yanhuo.xo.dto.platform.AlbumDTO;
import com.yanhuo.xo.vo.AlbumVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/platform/album")
@Api(tags="专辑模块")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    /**
     * 得到当前用户的所有专辑
     *
     * @param uid
     * @return
     */
    @RequestMapping("getAllAlbum")
    public Result<?> getAllAlbum(String uid) {
        List<AlbumVo> voList = albumService.getAllAlbum(uid);
        return Result.ok(voList);
    }

    /**
     * 保存专辑
     *
     * @param albumDTO
     * @return
     */
    @RequestMapping("saveAlbum")
    public Result<?> saveAlbum(@RequestBody AlbumDTO albumDTO) {
        ValidatorUtils.validateEntity(albumDTO, DefaultGroup.class);
        albumService.saveAlbum(albumDTO);
        return Result.ok(null);
    }

    /**
     * 得到专辑信息
     *
     * @param id
     * @return
     */
    @RequestMapping("getAlbum")
    public Result<?> getAlbum(String id) {
        AlbumVo albumVo = albumService.getAlbum(id);
        return Result.ok(albumVo);
    }


    /**
     * 删除专辑
     *
     * @param id
     * @param uid
     * @return
     */
    @RequestMapping("deleteAlbum")
    public Result<?> deleteAlbum(String id, String uid) {
        albumService.deleteAlbum(id, uid);
        return Result.ok(null);
    }


    /**
     * 更新专辑
     *
     * @param albumDTO
     * @return
     */
    @RequestMapping("updateAlbum")
    public Result<?> updateAlbum(@RequestBody AlbumDTO albumDTO) {
        ValidatorUtils.validateEntity(albumDTO, DefaultGroup.class);
        albumService.updateAlbum(albumDTO);
        return Result.ok(null);
    }
}