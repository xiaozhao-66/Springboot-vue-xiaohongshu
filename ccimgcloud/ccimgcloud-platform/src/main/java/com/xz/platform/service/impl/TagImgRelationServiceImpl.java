package com.xz.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.platform.dao.TagImgRelationDao;
import com.xz.platform.dto.TagImgRelationDTO;
import com.xz.platform.entity.TagImgRelationEntity;
import com.xz.platform.service.TagImgRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class TagImgRelationServiceImpl extends CrudServiceImpl<TagImgRelationDao, TagImgRelationEntity, TagImgRelationDTO> implements TagImgRelationService {

    @Override
    public QueryWrapper<TagImgRelationEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<TagImgRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }
}