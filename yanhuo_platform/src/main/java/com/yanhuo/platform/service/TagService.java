package com.yanhuo.platform.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.platform.TagDTO;
import com.yanhuo.xo.model.Tag;
import com.yanhuo.xo.vo.ImgDetailVo;
import com.yanhuo.xo.vo.TagVo;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface TagService extends IService<Tag> {

    /**
     * 得到所有的标签
     *
     * @return
     */
    List<TagVo> getAllTag();

    /**
     * 添加一条标签
     *
     * @param tagDTO
     */
    void saveTag(TagDTO tagDTO);

    /**
     * 保存一条标签
     *
     * @param name
     * @return
     */
    long saveTagByName(String name);

    /**
     * 根据标签获取推荐的视频
     *
     * @param page
     * @param limit
     * @param id
     * @param type
     * @return
     */
    Page<ImgDetailVo> getImgListByTag(long page, long limit, String id, Integer type);
}