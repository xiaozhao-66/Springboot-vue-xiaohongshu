package com.xz.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.platform.dao.UserOtherLoginRelationDao;
import com.xz.platform.dto.UserOtherLoginRelationDTO;
import com.xz.platform.entity.UserOtherLoginRelationEntity;
import com.xz.platform.service.UserOtherLoginRelationService;
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
public class UserOtherLoginRelationServiceImpl extends CrudServiceImpl<UserOtherLoginRelationDao, UserOtherLoginRelationEntity, UserOtherLoginRelationDTO> implements UserOtherLoginRelationService {

    @Override
    public QueryWrapper<UserOtherLoginRelationEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<UserOtherLoginRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }
}