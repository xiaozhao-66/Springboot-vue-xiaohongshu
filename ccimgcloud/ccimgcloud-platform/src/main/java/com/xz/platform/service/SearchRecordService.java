package com.xz.platform.service;

import com.xz.common.service.BaseService;
import com.xz.platform.dto.SearchRecordDTO;
import com.xz.platform.entity.SearchRecordEntity;

import java.util.List;

/**
 * @author 48423
 */
public interface SearchRecordService extends BaseService<SearchRecordEntity> {
    List<String> getAllSearchRecord(String uid);

    void addSearchRecord(SearchRecordDTO searchRecordDTO);

    void deleteSearchRecord(List<String> words,String uid);
}
