package com.yanhuo.search.service;


import com.yanhuo.xo.dto.search.ImgDetailSearchDTO;
import com.yanhuo.xo.vo.ImgDetailVo;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EsService {


    /**
     * 分页查询数据
     *
     * @param page
     * @param limit
     * @param type
     * @return
     * @throws IOException
     */
    List<ImgDetailVo> getPage(long page, long limit, Integer type) throws IOException;

    /**
     * es数据查找
     *
     * @param page
     * @param limit
     * @param imgDetailSearchDTO
     * @return
     * @throws IOException
     */
    HashMap<String, Object> esSearch(long page, long limit, ImgDetailSearchDTO imgDetailSearchDTO) throws IOException;

    /**
     * 新增一条数据
     *
     * @param imgDetailSearchVo
     * @throws IOException
     */
    void addData(ImgDetailVo imgDetailSearchVo) throws IOException;

    /**
     * 批量增加数据
     *
     * @param dataList
     * @throws IOException
     */
    @RequestMapping("addBulkData")
    void addbulkData(List<ImgDetailVo> dataList) throws IOException;

    /**
     * 删除索引中的一条数据
     *
     * @param id
     * @throws IOException
     */
    void delData(String id) throws IOException;

    /**
     * 修改数据
     *
     * @param imgDetailSearchVo
     * @throws IOException
     */
    void update(ImgDetailVo imgDetailSearchVo) throws IOException;

    /**
     * 查询所有数据
     *
     * @return
     * @throws IOException
     */
    List<ImgDetailVo> esSearchList() throws IOException;

    /**
     * 增加搜索记录
     *
     * @param keyword
     * @throws IOException
     */
    void addSearchRecordData(String keyword) throws IOException;


    /**
     * 搜索记录
     *
     * @param keyword
     * @return
     * @throws IOException
     */
    List<Map<String, Object>> esSearchRecord(String keyword) throws IOException;
}
