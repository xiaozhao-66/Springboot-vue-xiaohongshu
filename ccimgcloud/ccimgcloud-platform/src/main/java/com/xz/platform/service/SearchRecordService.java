package com.xz.platform.service;

import com.xz.common.service.CrudService;
import com.xz.platform.dto.MessageDTO;
import com.xz.platform.dto.SearchRecordDTO;
import com.xz.platform.entity.MessageEntity;
import com.xz.platform.entity.SearchRecordEntity;

import java.util.List;

/**
 * @author 48423
 */
public interface SearchRecordService extends CrudService<SearchRecordEntity, SearchRecordDTO> {
    List<String> getAllSearchRecord(String uid);

    void addSearchRecord(SearchRecordDTO searchRecordDTO);

    void deleteSearchRecord(List<String> words,String uid);
}
