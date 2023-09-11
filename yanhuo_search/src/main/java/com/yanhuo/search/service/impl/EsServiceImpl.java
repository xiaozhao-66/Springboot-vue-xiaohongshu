package com.yanhuo.search.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.yanhuo.common.constant.search.SearchConstant;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.search.service.EsService;
import com.yanhuo.search.utils.EsClientUtil;
import com.yanhuo.search.vo.SearchRecordVo;
import com.yanhuo.xo.dto.search.ImgDetailSearchDTO;
import com.yanhuo.xo.vo.ImgDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EsServiceImpl implements EsService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    RedisUtils redisUtils;

    @Override
    public HashMap<String, Object> esSearch(long page, long limit, ImgDetailSearchDTO imgDetailSearchDTO) throws IOException {
        HashMap<String, Object> map = new HashMap<>();

        SearchRequest searchRequest = new SearchRequest(SearchConstant.NEW_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 精确查询，添加查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (imgDetailSearchDTO.getKeyword() != null) {
            MatchQueryBuilder contentQueryBuilder = QueryBuilders.matchQuery("content", imgDetailSearchDTO.getKeyword());
            MatchQueryBuilder usernameQueryBuilder = QueryBuilders.matchQuery("username", imgDetailSearchDTO.getKeyword());
            BoolQueryBuilder should = boolQueryBuilder.should(contentQueryBuilder).should(usernameQueryBuilder);
            searchSourceBuilder.query(should);    // 分页
        }
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        if (imgDetailSearchDTO.getType() == 1) {
            //点赞排序
            searchSourceBuilder.sort(new FieldSortBuilder("agreeCount").order(SortOrder.DESC));
        } else if (imgDetailSearchDTO.getType() == 2) {
            searchSourceBuilder.sort(new FieldSortBuilder("time").order(SortOrder.DESC));
        }


        searchSourceBuilder.from((int) (page - 1) * (int) limit);
        searchSourceBuilder.size((int) limit);    // 高亮 =========
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);    // 解析结果 ==========
        SearchHits hits = searchResponse.getHits();
        TotalHits totalHits = hits.getTotalHits();

        List<ImgDetailVo> res = new ArrayList<>();

        for (SearchHit documentFields : hits.getHits()) {
            String sourceAsString = documentFields.getSourceAsString();
            ImgDetailVo imgDetailSearchVo = JSON.parseObject(sourceAsString, ImgDetailVo.class);

            String agreeImgKey = SearchConstant.AGREE_IMG_KEY + imgDetailSearchVo.getId();

            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeImgKey))) {
                int agreeCount = JSON.parseArray(redisUtils.get(agreeImgKey), Long.class).size();
                imgDetailSearchVo.setAgreeCount((long) agreeCount);
            }

            res.add(imgDetailSearchVo);
        }

        map.put(SearchConstant.RECORDS, res);
        map.put(SearchConstant.TOTAL, totalHits.value);
        return map;
    }


    @Override
    public void addData(ImgDetailVo imgDetailSearchVo) throws IOException {
// 创建请求
        IndexRequest request = new IndexRequest(SearchConstant.NEW_INDEX);
        // 制定规则 PUT /liuyou_index/_doc/1
        request.id(String.valueOf(imgDetailSearchVo.getId()));// 设置文档ID
        request.timeout(TimeValue.timeValueMillis(1000));// request.timeout("1s")
        // 将我们的数据放入请求中
        request.source(JSON.toJSONString(imgDetailSearchVo), XContentType.JSON);
        // 客户端发送请求，获取响应的结果
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);

        log.info(String.valueOf(response.status()));// 获取建立索引的状态信息 CREATED
        log.info(String.valueOf(response));// 查看返回内容
    }

    @Override
    public void addbulkData(List<ImgDetailVo> dataList) throws IOException {

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m"); // 可更具实际业务是指
        for (int i = 0; i < dataList.size(); i++) {
            ImgDetailVo imgDetailVo = dataList.get(i);
            bulkRequest.add(
                    new IndexRequest(SearchConstant.NEW_INDEX)
                            .id(String.valueOf(imgDetailVo.getId()))
                            .source(JSON.toJSONString(imgDetailVo), XContentType.JSON)
            );
        }
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//        restHighLevelClient.close();
    }

    /**
     * 删除文档中的数据
     *
     * @param id 文档
     * @throws IOException 异常
     */
    @Override
    public void delData(String id) throws IOException {
        DeleteRequest request = new DeleteRequest(SearchConstant.NEW_INDEX, id);
        request.timeout("1s");
        restHighLevelClient.delete(request, RequestOptions.DEFAULT);
    }

    @Override
    public void update(ImgDetailVo imgDetailSearchVo) throws IOException {
        String voStr = JSON.toJSONString(imgDetailSearchVo);
        EsClientUtil.updateDocByJson(SearchConstant.NEW_INDEX, String.valueOf(imgDetailSearchVo.getId()), voStr);
    }

    @Override
    public List<ImgDetailVo> esSearchList() throws IOException {
        SearchRequest searchRequest = new SearchRequest(SearchConstant.NEW_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1000);    // 高亮 =========
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);    // 解析结果 ==========
        SearchHits hits = searchResponse.getHits();
        List<ImgDetailVo> res = new ArrayList<>();

        for (SearchHit documentFields : hits.getHits()) {
            // 使用新的字段值（高亮），覆盖旧的字段值
            String sourceAsString = documentFields.getSourceAsString();
            ImgDetailVo imgDetailSearchVo = JSON.parseObject(sourceAsString, ImgDetailVo.class);
            res.add(imgDetailSearchVo);
        }

        return res;
    }

    public List<SearchRecordVo> esSearchRecordList(String keyword) throws IOException {
        List<SearchRecordVo> results = new ArrayList<SearchRecordVo>();

        if (!EsClientUtil.checkIndexExist(SearchConstant.RECORD_INDEX)) {
            return results;
        }

        SearchRequest searchRequest = new SearchRequest(SearchConstant.RECORD_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (keyword != null) {
            // 精确查询，添加查询条件
            searchSourceBuilder.query(QueryBuilders.matchQuery("keyword", keyword));
        }

        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchSourceBuilder.sort(new FieldSortBuilder("count").order(SortOrder.DESC));

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);    // 解析结果 ==========
        SearchHits hits = searchResponse.getHits();

        for (SearchHit documentFields : hits.getHits()) {
            String sourceAsString = documentFields.getSourceAsString();
            SearchRecordVo searchRecordVo = JSON.parseObject(sourceAsString, SearchRecordVo.class);
            results.add(searchRecordVo);
        }
        return results;
    }

    //添加搜索记录
    @Override
    public void addSearchRecordData(String keyword) throws IOException {
        if (keyword == null) {
            return;
        }
        IndexRequest request = new IndexRequest(SearchConstant.RECORD_INDEX);

        //修改
        List<SearchRecordVo> searchRecordVos = esSearchRecordList(keyword);
        for (SearchRecordVo model : searchRecordVos) {
            if (model.getKeyword().equals(keyword.trim())) {
                model.setCount(model.getCount() + 1);
                //进行修改
                updateSearchRecord(model);
                return;
            }
        }

        SearchRecordVo model = new SearchRecordVo();
        model.setKeyword(keyword.trim());
        model.setHighLightKeyword(keyword.trim());
        model.setCount(1);
        model.setTime(String.valueOf(System.currentTimeMillis()));

        String id = RandomUtil.randomNumbers(8);
        model.setId(id);

        // 制定规则 PUT /liuyou_index/_doc/1
        request.id(id);// 设置文档ID
        request.timeout(TimeValue.timeValueMillis(1000));// request.timeout("1s")
        // 将我们的数据放入请求中
        request.source(JSON.toJSONString(model), XContentType.JSON);
        // 客户端发送请求，获取响应的结果
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);

        log.info(String.valueOf(response.status()));// 获取建立索引的状态信息 CREATED
        log.info(String.valueOf(response));// 查看返回内容
    }

    @Override
    public List<Map<String, Object>> esSearchRecord(String keyword) throws IOException {
        List<Map<String, Object>> results = new ArrayList<>();

        if (!EsClientUtil.checkIndexExist(SearchConstant.RECORD_INDEX)) {
            return results;
        }
        SearchRequest searchRequest = new SearchRequest(SearchConstant.RECORD_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (keyword != null) {
            // 精确查询，添加查询条件
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            MatchQueryBuilder contentQueryBuilder = QueryBuilders.matchQuery("highLightKeyword", keyword);
            BoolQueryBuilder should = boolQueryBuilder.should(contentQueryBuilder);
            searchSourceBuilder.query(should);    // 分页
        }

        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchSourceBuilder.sort(new FieldSortBuilder("count").order(SortOrder.DESC));

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(8);    // 高亮 =========

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("highLightKeyword");
        highlightBuilder.preTags("<span style='color:black'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);        // 执行查询

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);    // 解析结果 ==========
        SearchHits hits = searchResponse.getHits();

        for (SearchHit documentFields : hits.getHits()) {
            // 使用新的字段值（高亮），覆盖旧的字段值
            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();         // 高亮字段
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField name = highlightFields.get("highLightKeyword");

            if (name != null) {
                Text[] fragments = name.fragments();
                StringBuilder new_name = new StringBuilder();
                for (Text text : fragments) {
                    new_name.append(text);
                }
                sourceAsMap.put("highLightKeyword", new_name.toString());
            }
            results.add(sourceAsMap);
        }
        return results;
    }

    @Override
    public List<ImgDetailVo> getPage(long page, long limit, Integer type) throws IOException {
        SearchRequest searchRequest = new SearchRequest(SearchConstant.NEW_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        if (type == 1) {
            //点赞排序
            searchSourceBuilder.sort(new FieldSortBuilder("agreeCount").order(SortOrder.DESC));
        } else {
            searchSourceBuilder.sort(new FieldSortBuilder("time").order(SortOrder.DESC));
        }


        searchSourceBuilder.from((int) (page - 1) * (int) limit);
        searchSourceBuilder.size((int) limit);    // 高亮 =========
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);    // 解析结果 ==========
        SearchHits hits = searchResponse.getHits();


        List<ImgDetailVo> res = new ArrayList<>();

        for (SearchHit documentFields : hits.getHits()) {
            String sourceAsString = documentFields.getSourceAsString();
            ImgDetailVo imgDetailSearchVo = JSON.parseObject(sourceAsString, ImgDetailVo.class);
            res.add(imgDetailSearchVo);
        }
        return res;
    }


    public void updateSearchRecord(SearchRecordVo searchRecordVo) throws IOException {
        String voStr = JSON.toJSONString(searchRecordVo);
        EsClientUtil.updateDocByJson(SearchConstant.RECORD_INDEX, searchRecordVo.getId(), voStr);
    }
}
