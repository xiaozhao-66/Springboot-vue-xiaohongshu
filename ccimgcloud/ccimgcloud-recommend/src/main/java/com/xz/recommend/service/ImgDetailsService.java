package com.xz.recommend.service;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.service.CrudService;
import com.xz.recommend.dto.ImgDetailsDTO;
import com.xz.recommend.entity.ImgDetailsEntity;
import com.xz.recommend.vo.ImgDetailVo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
public interface ImgDetailsService extends CrudService<ImgDetailsEntity, ImgDetailsDTO> {

    Page<ImgDetailVo> recommendToUser(long page,long limit,String uid);

    Page<ImgDetailVo> recommendToUser2(long page, long limit, String uid);

    HashMap<String,Object> newRecommendToUser(long page, long limit, String uid) throws ModelException, TranslateException, IOException;
}