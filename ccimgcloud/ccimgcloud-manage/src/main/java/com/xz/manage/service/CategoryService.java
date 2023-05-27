package com.xz.manage.service;

import com.xz.common.service.BaseService;
import com.xz.manage.entity.CategoryEntity;
import com.xz.manage.vo.CategoryVo;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
public interface CategoryService extends BaseService<CategoryEntity> {

    List<CategoryVo> getTreeCategory();

    List<CategoryVo> getCategory(Integer level);
}