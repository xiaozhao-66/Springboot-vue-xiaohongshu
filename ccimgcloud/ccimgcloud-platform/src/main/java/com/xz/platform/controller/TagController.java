package com.xz.platform.controller;



import com.xz.common.utils.Result;
import com.xz.common.validator.ValidatorUtils;
import com.xz.common.validator.group.DefaultGroup;
import com.xz.platform.dto.TagDTO;
import com.xz.platform.service.TagService;
import com.xz.platform.vo.TagVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;



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