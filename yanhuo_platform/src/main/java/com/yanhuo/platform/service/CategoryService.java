package com.yanhuo.platform.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.model.Category;
import com.yanhuo.xo.vo.CategoryVo;
import com.yanhuo.xo.vo.ImgDetailVo;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
public interface CategoryService extends IService<Category> {

    /**
     * 得到所有分类，返回树形结构
     *
     * @return
     */
    List<CategoryVo> getTreeCategory();

    /**
     * 通过分类获取所有的图片
     *
     * @param page
     * @param limit
     * @param id
     * @param type
     * @return
     */
    Page<ImgDetailVo> getImgListByCategory(long page, long limit, String id, Integer type);
}