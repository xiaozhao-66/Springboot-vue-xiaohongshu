package com.xz.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.service.CrudService;
import com.xz.platform.dto.CategoryDTO;
import com.xz.platform.entity.CategoryEntity;
import com.xz.platform.vo.CategoryVo;
import com.xz.platform.vo.ImgDetailVo;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
public interface CategoryService extends CrudService<CategoryEntity, CategoryDTO> {

    List<CategoryVo> getTreeCategory();

    Page<ImgDetailVo> getImgListByCategory(long page, long limit, String id,Integer type);
}