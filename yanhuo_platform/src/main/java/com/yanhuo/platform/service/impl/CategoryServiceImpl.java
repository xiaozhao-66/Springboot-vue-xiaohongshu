package com.yanhuo.platform.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.TreeUtils;
import com.yanhuo.platform.dao.CategoryDao;
import com.yanhuo.platform.service.CategoryService;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.model.Category;
import com.yanhuo.xo.model.ImgDetail;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.vo.CategoryVo;
import com.yanhuo.xo.vo.ImgDetailVo;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

    @Autowired
    ImgDetailService imgDetailService;

    @Autowired
    UserService userService;

    @Override
    @Cacheable(cacheNames = PlatformConstant.CATEGORY, key = PlatformConstant.CATEGORY_KEY)
    public List<CategoryVo> getTreeCategory() {
        List<Category> categoryList = this.list();
        List<CategoryVo> voList = ConvertUtils.sourceToTarget(categoryList, CategoryVo.class);
        return TreeUtils.build(voList);
    }


    @Override
    public Page<ImgDetailVo> getImgListByCategory(long page, long limit, String id, Integer type) {

        Page<ImgDetailVo> pages = new Page<>();
        Page<ImgDetail> imgDetailPage;
        if (type == 1) {
            //随便看看
            imgDetailPage = imgDetailService.page(new Page<>(page, limit), new QueryWrapper<ImgDetail>().eq("category_pid", id).orderByDesc("update_date"));
        } else if (type == 2) {
            //获取热门
            imgDetailPage = imgDetailService.page(new Page<>(page, limit), new QueryWrapper<ImgDetail>().eq("category_pid", id).orderByDesc("agree_count").orderByDesc("update_date"));
        } else {
            //获取二级分类下的所有笔记
            imgDetailPage = imgDetailService.page(new Page<>(page, limit), new QueryWrapper<ImgDetail>().eq("category_id", id).orderByDesc("agree_count").orderByDesc("update_date"));
        }

        List<ImgDetail> imgDetailList = imgDetailPage.getRecords();
        Collection<Long> uids = new HashSet<>();
        imgDetailList.forEach(item -> {
            uids.add(item.getUserId());
        });

        List<User> userList = userService.listByIds(uids);
        HashMap<Long, User> userMap = new HashMap<>();
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        List<ImgDetailVo> res = new ArrayList<>();
        ImgDetailVo imgDetailVo;
        User user;
        for (ImgDetail model : imgDetailPage.getRecords()) {
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            user = userMap.get(model.getUserId());
            imgDetailVo.setUserId(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar());
            res.add(imgDetailVo);
        }
        pages.setRecords(res);
        pages.setTotal(imgDetailPage.getTotal());

        return pages;
    }

}