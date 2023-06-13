package com.xz.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.ConvertUtils;
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

import java.util.*;

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

        Page<ImgDetailVo> pages = new Page<>();
        Page<ImgDetailsEntity> imgDetailPage;
        if (type == 1) {
            //随便看看
            imgDetailPage = imgDetailsDao.selectPage(new Page<>(page, limit), new QueryWrapper<ImgDetailsEntity>().eq("category_pid", id).orderByDesc("update_date"));
        } else if (type == 2) {
            //获取热门
            imgDetailPage = imgDetailsDao.selectPage(new Page<>(page, limit), new QueryWrapper<ImgDetailsEntity>().eq("category_pid", id).orderByDesc("agree_count").orderByDesc("update_date"));
        } else {
            //获取二级分类下的所有笔记
            imgDetailPage = imgDetailsDao.selectPage(new Page<>(page, limit), new QueryWrapper<ImgDetailsEntity>().eq("category_id", id).orderByDesc("agree_count").orderByDesc("update_date"));
        }

        List<ImgDetailsEntity> imgDetailList = imgDetailPage.getRecords();

        Collection<Long> uids = new HashSet<>();

        imgDetailList.forEach(item -> {
            uids.add(item.getUserId());
        });

        List<UserEntity> userList = userDao.selectBatchIds(uids);
        HashMap<Long, UserEntity> userMap = new HashMap<>();

        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        List<ImgDetailVo> res = new ArrayList<>();
        ImgDetailVo imgDetailVo;
        UserEntity userEntity;
        for (ImgDetailsEntity model : imgDetailPage.getRecords()) {
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            userEntity = userMap.get(model.getUserId());
            imgDetailVo.setUserId(userEntity.getId())
                    .setUsername(userEntity.getUsername())
                    .setAvatar(userEntity.getAvatar());
            res.add(imgDetailVo);
        }
        pages.setRecords(res);
        pages.setTotal(imgDetailPage.getTotal());

        return pages;
    }
}