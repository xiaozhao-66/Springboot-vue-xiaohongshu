package com.xz.platform.controller;

import com.xz.common.utils.Result;
import com.xz.common.validator.ValidatorUtils;
import com.xz.common.validator.group.DefaultGroup;
import com.xz.platform.dto.AlbumDTO;
import com.xz.platform.service.AlbumService;
import com.xz.platform.vo.AlbumVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/album")
@Api(tags="专辑模块")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    /**
     * 得到当前用户的所有专辑
     * @param uid
     * @return
     */
    @RequestMapping("getAllAlbum")
    public Result<?> getAllAlbum(String uid) {
        List<AlbumVo> voList = albumService.getAllAlbum(uid);
        return new Result<List<AlbumVo>>().ok(voList);
    }

    /**
     * 保存专辑
     * @param albumDTO
     * @return
     */
    @RequestMapping("saveAlbum")
    public Result<?> saveAlbum(@RequestBody AlbumDTO albumDTO) {
        ValidatorUtils.validateEntity(albumDTO, DefaultGroup.class);
        albumService.saveAlbum(albumDTO);
        return new Result<>().ok();
    }

    /**
     * 得到专辑信息
     * @param id
     * @return
     */
    @RequestMapping("getAlbum")
    public Result<?> getAlbum(String id) {
        AlbumVo albumVo = albumService.getAlbum(id);
        return new Result<AlbumVo>().ok(albumVo);
    }


    /**
     * 删除专辑
     * @param id
     * @param uid
     * @return
     */
    @RequestMapping("deleteAlbum")
    public Result<?> deleteAlbum(String id, String uid) {
        albumService.deleteAlbum(id, uid);
        return new Result<>().ok();
    }


    /**
     * 更新专辑
     * @param albumDTO
     * @return
     */
    @RequestMapping("updateAlbum")
    public Result<?> updateAlbum(@RequestBody AlbumDTO albumDTO) {
        ValidatorUtils.validateEntity(albumDTO, DefaultGroup.class);
        albumService.updateAlbum(albumDTO);
        return new Result<>().ok();
    }
}