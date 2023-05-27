package com.xz.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.utils.Result;
import com.xz.platform.service.CategoryService;
import com.xz.platform.vo.CategoryVo;
import com.xz.platform.vo.ImgDetailVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@RestController
@RequestMapping("category")
@Api(tags="分类模块")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 得到所有分类，返回树形结构
     *
     * @return
     */
    @RequestMapping("getTreeCategory")
    public Result<?> getTreeCategory() {
        List<CategoryVo> categoryList = categoryService.getTreeCategory();
        return new Result<List<CategoryVo>>().ok(categoryList);
    }

    /**
     * 通过分类获取所有的图片
     *
     * @param page
     * @param limit
     * @param id
     * @param type
     * @return
     */
    @RequestMapping("getImgListByCategory/{page}/{limit}")
    public Result<?> getImgListByCategory(@PathVariable long page, @PathVariable long limit, String id, Integer type) {
        Page<ImgDetailVo> pageInfo = categoryService.getImgListByCategory(page, limit, id, type);
        return new Result<Page<ImgDetailVo>>().ok(pageInfo);
    }
}