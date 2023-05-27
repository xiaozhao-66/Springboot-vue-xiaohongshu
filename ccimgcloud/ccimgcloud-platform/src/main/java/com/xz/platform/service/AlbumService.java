package com.xz.platform.service;

import com.xz.common.service.CrudService;
import com.xz.platform.dto.AlbumDTO;
import com.xz.platform.entity.AlbumEntity;
import com.xz.platform.vo.AlbumVo;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface AlbumService extends CrudService<AlbumEntity, AlbumDTO> {

    List<AlbumVo> getAllAlbum(String uid);

    AlbumVo getAlbum(String id);

    void deleteAlbum(String id,String uid);

    void saveAlbum(AlbumDTO albumDTO);

    void updateAlbum(AlbumDTO albumDTO);
}