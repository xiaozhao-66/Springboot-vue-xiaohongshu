package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.platform.AlbumDTO;
import com.yanhuo.xo.model.Album;
import com.yanhuo.xo.vo.AlbumVo;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface AlbumService extends IService<Album> {

    /**
     * 得到当前用户的所有专辑
     *
     * @param uid
     * @return
     */
    List<AlbumVo> getAllAlbum(String uid);

    /**
     * 得到专辑信息
     *
     * @param id
     * @return
     */
    AlbumVo getAlbum(String id);

    /**
     * 删除专辑
     *
     * @param id
     * @param uid
     * @return
     */
    void deleteAlbum(String id, String uid);

    /**
     * 保存专辑
     *
     * @param albumDTO
     * @return
     */
    void saveAlbum(AlbumDTO albumDTO);

    /**
     * 更新专辑
     *
     * @param albumDTO
     * @return
     */
    void updateAlbum(AlbumDTO albumDTO);
}