package com.xz.platform.service;

import com.xz.common.service.BaseService;
import com.xz.platform.dto.TagDTO;
import com.xz.platform.entity.TagEntity;
import com.xz.platform.vo.TagVo;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface TagService extends BaseService<TagEntity> {

    List<TagVo> getAllTag();

    void saveTag(TagDTO tagDTO);

    long saveTagByName(String name);
}