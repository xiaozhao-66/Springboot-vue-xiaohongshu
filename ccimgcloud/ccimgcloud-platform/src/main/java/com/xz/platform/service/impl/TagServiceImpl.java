package com.xz.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.platform.dao.TagDao;
import com.xz.platform.dto.TagDTO;
import com.xz.platform.entity.TagEntity;
import com.xz.platform.service.TagService;
import com.xz.platform.vo.TagVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class TagServiceImpl extends BaseServiceImpl<TagDao, TagEntity> implements TagService {


    @Override
    public List<TagVo> getAllTag() {

        List<TagEntity> tagList = baseDao.selectTagsNums(22);
        return ConvertUtils.sourceToTarget(tagList, TagVo.class);
    }


    @Override
    public void saveTag(TagDTO tagDTO) {
        TagEntity tag = baseDao.selectOne(new QueryWrapper<TagEntity>().eq("name", tagDTO.getName()));
        if (tag != null) {
            tag.setCount(tag.getCount() + 1);
            baseDao.updateById(tag);
        } else {
            TagEntity tagEntity = ConvertUtils.sourceToTarget(tagDTO, TagEntity.class);
            tagEntity.setCount(1L);
            baseDao.insert(tagEntity);
        }
    }

    @Override
    public long saveTagByName(String name) {

        TagEntity tag = baseDao.selectOne(new QueryWrapper<TagEntity>().eq("name", name));
        if (tag != null) {
            tag.setCount(tag.getCount() + 1);
            baseDao.updateById(tag);
            return tag.getId();
        } else {
            TagEntity tagEntity = new TagEntity();
            tagEntity.setName(name);
            tagEntity.setCount(1L);
            baseDao.insert(tagEntity);
            return tagEntity.getId();
        }
    }
}