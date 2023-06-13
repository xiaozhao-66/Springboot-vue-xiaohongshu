package com.xz.recommend.service;

import com.xz.common.service.BaseService;
import com.xz.recommend.entity.CategoryEntity;


import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
public interface CategoryService extends BaseService<CategoryEntity> {

    List<String> getAllCategoryTwo();

}