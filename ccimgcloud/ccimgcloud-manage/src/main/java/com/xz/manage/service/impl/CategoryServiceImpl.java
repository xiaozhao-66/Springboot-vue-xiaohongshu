package com.xz.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.TreeUtils;
import com.xz.manage.dao.CategoryDao;
import com.xz.manage.entity.CategoryEntity;
import com.xz.manage.service.CategoryService;
import com.xz.manage.vo.CategoryVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public List<CategoryVo> getTreeCategory() {
        List<CategoryEntity> categoryList = baseDao.selectList(null);
        List<CategoryVo> voList = ConvertUtils.sourceToTarget(categoryList, CategoryVo.class);
        return TreeUtils.build(voList);
    }

    @Override
    public List<CategoryVo> getCategory(Integer level) {
        List<CategoryEntity> categoryList = null;
        if(level==0){
            categoryList = baseDao.selectList(new QueryWrapper<CategoryEntity>().eq("pid",0));
        }else {
            categoryList = baseDao.selectList(new QueryWrapper<CategoryEntity>().ne("pid",0));
        }
        List<CategoryVo> voList = ConvertUtils.sourceToTarget(categoryList, CategoryVo.class);
        return voList;
    }


}