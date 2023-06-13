package com.xz.platform.service;


import com.xz.common.service.BaseService;
import com.xz.platform.dto.CollectionDTO;
import com.xz.platform.entity.CollectionEntity;
import com.xz.platform.vo.CollectionVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface CollectionService extends BaseService<CollectionEntity> {

    Map<String,String> collection(CollectionDTO collectionDTO);

    List<CollectionVo> getAllCollection(long page, long limit, String uid, Integer type);

    Map<String,String> cancelCollection(CollectionDTO collectionDTO);
}