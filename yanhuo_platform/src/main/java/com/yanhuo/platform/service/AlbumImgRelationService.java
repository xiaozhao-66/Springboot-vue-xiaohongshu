package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.platform.AlbumImgRelationDTO;
import com.yanhuo.xo.model.AlbumImgRelation;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface AlbumImgRelationService extends IService<AlbumImgRelation> {

    /**
     * 当前用户是否已经收藏图片至专辑中
     *
     * @return
     */
    boolean isCollectImgToAlbum(String uid, String mid);

    /**
     * 增加图片和专辑之间的联系
     *
     * @param albumImgRelationDTO
     * @return
     */
    void addAlbumImgRelation(AlbumImgRelationDTO albumImgRelationDTO);

    /**
     * 删除关系
     *
     * @param albumImgRelationDTO
     * @return
     */
    void deleteAlbumImgRelation(AlbumImgRelationDTO albumImgRelationDTO);
}