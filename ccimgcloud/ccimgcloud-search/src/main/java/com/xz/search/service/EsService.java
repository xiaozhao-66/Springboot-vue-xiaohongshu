package com.xz.search.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.search.dto.ImgDetailSearchDTO;
import com.xz.search.dto.SearchRecordDTO;
import com.xz.search.entity.ImgDetailSearchEntity;
import com.xz.search.vo.ImgDetailSearchVo;
import com.xz.search.vo.SearchRecordVo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EsService {
    HashMap<String,Object> esSearch(long page, long limit, ImgDetailSearchDTO imgDetailSearchDTO) throws IOException;

    void addData(ImgDetailSearchVo imgDetailSearchVo) throws IOException;

    void addbulkData(List<ImgDetailSearchVo> dataList) throws IOException;

    void delData(String id) throws IOException;

    void update(ImgDetailSearchVo imgDetailSearchVo) throws IOException;

    List<ImgDetailSearchVo> esSearchList() throws IOException;

    void addSearchRecordData(String keyword) throws IOException;

    List<Map<String, Object>> esSearchRecord(String keyword) throws IOException;

}
