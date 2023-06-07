package com.xz.platform.service;

import com.xz.common.service.BaseService;
import com.xz.platform.dto.AlbumImgRelationDTO;
import com.xz.platform.entity.AlbumImgRelationEntity;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface AlbumImgRelationService extends BaseService<AlbumImgRelationEntity> {

    boolean isCollectImgToAlbum(String uid, String mid);

    void addAlbumImgRelation(AlbumImgRelationDTO albumImgRelationDTO);

    void deleteAlbumImgRelation(AlbumImgRelationDTO albumImgRelationDTO);
}