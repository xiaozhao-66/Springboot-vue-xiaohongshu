package com.xz.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.PageUtils;
import com.xz.common.utils.TreeUtils;
import com.xz.common.constant.cacheConstant.CategoryCacheNames;
import com.xz.platform.dao.CategoryDao;
import com.xz.platform.dao.ImgDetailsDao;
import com.xz.platform.dao.UserDao;
import com.xz.platform.entity.CategoryEntity;
import com.xz.platform.entity.ImgDetailsEntity;
import com.xz.platform.entity.UserEntity;
import com.xz.platform.service.CategoryService;
import com.xz.platform.vo.CategoryVo;
import com.xz.platform.vo.ImgDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {


    @Autowired
    ImgDetailsDao imgDetailsDao;

    @Autowired
    UserDao userDao;


    @Override
    @Cacheable(cacheNames = CategoryCacheNames.CATEGORY, key = CategoryCacheNames.CATEGORY_KEY)
    public List<CategoryVo> getTreeCategory() {
        List<CategoryEntity> categoryList = baseDao.selectList(null);
        List<CategoryVo> voList = ConvertUtils.sourceToTarget(categoryList, CategoryVo.class);
        return TreeUtils.build(voList);
    }


    @Override
    public Page<ImgDetailVo> getImgListByCategory(long page, long limit, String id, Integer type) {

        List<ImgDetailsEntity> imgDetailsEntityList = null;
        if (type == 1) {
            //随便看看
            imgDetailsEntityList = imgDetailsDao.selectList(new QueryWrapper<ImgDetailsEntity>().eq("category_pid", id).orderByDesc("update_date"));
        } else if (type == 2) {
            //获取热门
            imgDetailsEntityList = imgDetailsDao.selectList(new QueryWrapper<ImgDetailsEntity>().eq("category_pid", id).orderByDesc("agree_count"));
        } else {
            //获取二级分类下的所有笔记
            imgDetailsEntityList = imgDetailsDao.selectList(new QueryWrapper<ImgDetailsEntity>().eq("category_id", id).orderByDesc("agree_count"));
        }

        List<ImgDetailVo> res = new ArrayList<>();
        ImgDetailVo imgDetailVo = null;
        UserEntity userEntity = null;
        for (ImgDetailsEntity model : imgDetailsEntityList) {
            userEntity = userDao.selectById(model.getUserId());

            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            imgDetailVo.setUserId(userEntity.getId())
                    .setUsername(userEntity.getUsername())
                    .setAvatar(userEntity.getAvatar());
            res.add(imgDetailVo);
        }

        return PageUtils.getPages((int) page, (int) limit, res);
    }
}