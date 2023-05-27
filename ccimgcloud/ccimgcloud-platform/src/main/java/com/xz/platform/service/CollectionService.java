package com.xz.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.service.CrudService;
import com.xz.platform.dto.CollectionDTO;
import com.xz.platform.entity.CollectionEntity;
import com.xz.platform.vo.CollectionVo;

import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface CollectionService extends CrudService<CollectionEntity, CollectionDTO> {

    Map<String,String> collection(CollectionDTO collectionDTO);

    Page<CollectionVo> getAllCollection(long page,long limit,String uid, Integer type);

    Map<String,String> cancalCollection(CollectionDTO collectionDTO);
}