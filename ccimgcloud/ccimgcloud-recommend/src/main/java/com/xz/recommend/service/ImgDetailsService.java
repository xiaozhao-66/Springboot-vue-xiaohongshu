package com.xz.recommend.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.service.BaseService;
import com.xz.recommend.entity.ImgDetailsEntity;
import com.xz.recommend.vo.ImgDetailVo;



/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
public interface ImgDetailsService extends BaseService<ImgDetailsEntity> {

    Page<ImgDetailVo> recommendToUser(long page,long limit,String uid);

    Page<ImgDetailVo> recommendToUser2(long page, long limit, String uid);

}