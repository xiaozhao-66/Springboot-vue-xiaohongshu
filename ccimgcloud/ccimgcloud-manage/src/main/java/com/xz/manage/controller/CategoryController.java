package com.xz.manage.controller;

import com.xz.common.constant.Constant;
import com.xz.common.page.PageData;
import com.xz.common.utils.Result;
import com.xz.common.validator.AssertUtils;
import com.xz.common.validator.ValidatorUtils;
import com.xz.common.validator.group.AddGroup;
import com.xz.common.validator.group.DefaultGroup;
import com.xz.common.validator.group.UpdateGroup;
import com.xz.manage.entity.CategoryEntity;
import com.xz.manage.service.CategoryService;
import com.xz.manage.vo.CategoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@RestController
@RequestMapping("/category")
@Api(tags="")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("getTreeCategory")
    public Result<?> getTreeCategory(){
        List<CategoryVo> categoryList = categoryService.getTreeCategory();
        return new Result<List<CategoryVo>>().ok(categoryList);
    }

    @RequestMapping("getCategory")
    public Result<?> getCategory(Integer level){
        List<CategoryVo> categoryList = categoryService.getCategory(level);
        return new Result<List<CategoryVo>>().ok(categoryList);
    }

    @RequestMapping("addCategory")
    public Result<?> addCategory(@RequestBody CategoryEntity categoryEntity) {
        categoryService.insert(categoryEntity);
        return new Result<>().ok();
    }
}