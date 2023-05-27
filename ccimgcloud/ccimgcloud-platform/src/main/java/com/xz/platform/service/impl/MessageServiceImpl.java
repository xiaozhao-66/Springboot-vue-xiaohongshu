package com.xz.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.platform.dao.MessageDao;
import com.xz.platform.dto.MessageDTO;
import com.xz.platform.entity.MessageEntity;
import com.xz.platform.service.MessageService;
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
public class MessageServiceImpl extends CrudServiceImpl<MessageDao, MessageEntity, MessageDTO> implements MessageService {

    @Override
    public QueryWrapper<MessageEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MessageEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

}