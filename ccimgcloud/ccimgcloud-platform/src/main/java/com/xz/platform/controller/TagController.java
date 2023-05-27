package com.xz.platform.controller;


import com.xz.common.constant.Constant;
import com.xz.common.page.PageData;

import com.xz.common.utils.Result;
import com.xz.common.validator.AssertUtils;
import com.xz.common.validator.ValidatorUtils;
import com.xz.common.validator.group.AddGroup;
import com.xz.common.validator.group.DefaultGroup;
import com.xz.common.validator.group.UpdateGroup;
import com.xz.platform.dto.TagDTO;
import com.xz.platform.service.TagService;
import com.xz.platform.vo.AlbumVo;
import com.xz.platform.vo.TagVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/tag")
@Api(tags="标签模块")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 获取所有标签
     *
     * @return
     */
    @RequestMapping("getAllTag")
    public Result<?> getAllTag() {
        List<TagVo> voList = tagService.getAllTag();
        return new Result<List<TagVo>>().ok(voList);
    }

    /**
     * 添加标签
     *
     * @param tagDTO
     * @return
     */
    @RequestMapping("saveTag")
    public Result<?> saveTag(@RequestBody TagDTO tagDTO) {
        ValidatorUtils.validateEntity(tagDTO, DefaultGroup.class);
        tagService.saveTag(tagDTO);
        return new Result<>().ok();
    }
}