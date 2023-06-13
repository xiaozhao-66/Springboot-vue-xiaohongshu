package com.xz.platform.service;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.service.BaseService;
import com.xz.platform.dto.ImgDetailsDTO;
import com.xz.platform.entity.ImgDetailsEntity;
import com.xz.platform.vo.ImgDetailInfoVo;
import com.xz.platform.vo.ImgDetailVo;

import java.io.IOException;
import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
public interface ImgDetailsService extends BaseService<ImgDetailsEntity> {

    Page<ImgDetailVo> getPage(long page, long limit);

    ImgDetailsEntity publish(ImgDetailsDTO imgDetailsDTO);

    void updateStatus(ImgDetailsDTO imgDetailsDTO);

    List<ImgDetailVo> getAllImgByAlbum(long page, long limit, String albumId, Integer type);

    ImgDetailInfoVo getOne(String id);

    void addBulkData();

    void deleteImgs(String[] ids,String uid);

    Page<ImgDetailVo> getHot(long page, long limit);

    ImgDetailsEntity updateImgDetail(ImgDetailsDTO imgDetailsDTO) throws MalformedModelException, ModelNotFoundException, TranslateException, IOException;

}