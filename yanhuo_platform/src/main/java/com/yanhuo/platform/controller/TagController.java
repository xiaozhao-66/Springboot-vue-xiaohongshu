package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.platform.service.TagService;
import com.yanhuo.xo.dto.platform.TagDTO;
import com.yanhuo.xo.model.Tag;
import com.yanhuo.xo.vo.ImgDetailVo;
import com.yanhuo.xo.vo.TagVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/api/platform/tag")
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
        return Result.ok(voList);
    }

    /**
     * 得到当前标签信息
     *
     * @param id
     * @return
     */
    @RequestMapping("getOneTag")
    public Result<?> getOneTag(String id) {
        Tag tag = tagService.getById(id);
        return Result.ok(tag);
    }

    /**
     * 根据标签id获取图片信息
     *
     * @param id
     * @param type
     * @return
     */
    @RequestMapping("getImgListByTagId/{page}/{limit}")
    public Result<?> getImgListByTag(@PathVariable long page, @PathVariable long limit, String id, Integer type) {
        Page<ImgDetailVo> imgDetailVoList = tagService.getImgListByTag(page, limit, id, type);
        return Result.ok(imgDetailVoList);
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
        return Result.ok(null);
    }

}