package com.xz.recommend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.recommend.dao.CategoryDao;
import com.xz.recommend.dto.CategoryDTO;
import com.xz.recommend.entity.CategoryEntity;
import com.xz.recommend.service.CategoryService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Service
public class CategoryServiceImpl extends CrudServiceImpl<CategoryDao, CategoryEntity, CategoryDTO> implements CategoryService {


    @Override
    public QueryWrapper<CategoryEntity> getWrapper(Map<String, Object> params) {
        return null;
    }

    @Override
    @Cacheable(cacheNames = "categoryIds", key = "'categoryIds'", sync = true)
    public List<String> getAllCategoryTwo() {
        List<CategoryEntity> list = baseDao.selectList(new QueryWrapper<CategoryEntity>().ne("pid", 0));
        return list.stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
    }

}