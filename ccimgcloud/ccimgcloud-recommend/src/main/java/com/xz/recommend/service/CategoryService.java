package com.xz.recommend.service;

import com.xz.common.service.CrudService;
import com.xz.recommend.dto.CategoryDTO;
import com.xz.recommend.entity.CategoryEntity;


import java.util.List;
import java.util.Locale;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
public interface CategoryService extends CrudService<CategoryEntity, CategoryDTO> {

    List<String> getAllCategoryTwo();

}