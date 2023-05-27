package com.xz.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.platform.common.utils.KMP;
import com.xz.platform.dao.MessageDao;
import com.xz.platform.dao.SearchRecordDao;
import com.xz.platform.dto.MessageDTO;
import com.xz.platform.dto.SearchRecordDTO;
import com.xz.platform.entity.MessageEntity;
import com.xz.platform.entity.SearchRecordEntity;
import com.xz.platform.service.MessageService;
import com.xz.platform.service.SearchRecordService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 48423
 */
@Service
public class SearchRecordServiceImpl extends CrudServiceImpl<SearchRecordDao, SearchRecordEntity, SearchRecordDTO> implements SearchRecordService {
    @Override
    public QueryWrapper<SearchRecordEntity> getWrapper(Map<String, Object> params) {
        return null;
    }

    private List<String> splitRecord(String content) {

        String[] records = content.split(";");
        return Arrays.asList(records);
    }

    @Override
    public List<String> getAllSearchRecord(String uid) {

        SearchRecordEntity searchRecord = baseDao.selectOne(new QueryWrapper<SearchRecordEntity>().eq("uid", uid));

        String[] records = searchRecord.getSearchContent().split(";");

        return Arrays.asList(records);
    }

    @Override
    public void addSearchRecord(SearchRecordDTO searchRecordDTO) {

        SearchRecordEntity searchRecord = baseDao.selectOne(new QueryWrapper<SearchRecordEntity>().eq("uid", searchRecordDTO.getUid()));

        StringBuilder stringBuilder = new StringBuilder();

        if (searchRecord == null) {

            SearchRecordEntity searchRecordEntity = new SearchRecordEntity();
            stringBuilder.append(searchRecordDTO.getKeyword());
            searchRecordEntity.setUid(searchRecordDTO.getUid());
            searchRecordEntity.setSearchContent(stringBuilder.toString());
            baseDao.insert(searchRecordEntity);
        } else {
            //查找当前搜索的记录是否在历史记录中存在
            String[] records = searchRecord.getSearchContent().split(";");

            HashSet<String> resSet = new HashSet<>(Arrays.asList(records));

            if (resSet.contains(searchRecordDTO.getKeyword())) {
                resSet.remove(searchRecordDTO.getKeyword());
            } else {
                if (resSet.size() > 20) {
                    resSet.remove(records[records.length - 1]);
                }
            }

            stringBuilder.append(searchRecordDTO.getKeyword()).append(";");

            for (String e : resSet) {
                stringBuilder.append(e).append(";");
            }

            searchRecord.setSearchContent(stringBuilder.toString());
            baseDao.updateById(searchRecord);
        }
    }

    @Override
    public void deleteSearchRecord(List<String> words, String uid) {

        SearchRecordEntity searchRecord = baseDao.selectOne(new QueryWrapper<SearchRecordEntity>().eq("uid", uid));

        if (searchRecord == null) {
            return;
        }

        String[] records = searchRecord.getSearchContent().split(";");

        HashSet<String> resSet = new HashSet<>(Arrays.asList(records));

        for (String word : words) {
            resSet.remove(word);
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (String e : resSet) {
            stringBuilder.append(e).append(";");
        }

        searchRecord.setSearchContent(stringBuilder.toString());
        baseDao.updateById(searchRecord);

    }
}
