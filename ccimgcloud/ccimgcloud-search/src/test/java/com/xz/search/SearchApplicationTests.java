package com.xz.search;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.xz.search.entity.ImgDetailSearchEntity;
import com.xz.search.utils.EsClientUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SearchApplicationTests {


    @Autowired
    private RestHighLevelClient restHighLevelClient;



    @Test
    public void test1() throws IOException {
        boolean index = EsClientUtil.createIndex("index");
        System.out.println(index);
//        User user = new User(12,"zhangsan");
//        String s = JSON.toJSONString(user);
       // EsClientUtil.addDocByJson("index","1",s);

    }

    @Test
    public void test2() throws IOException {
        Map<String, Object> doc = EsClientUtil.getDoc("index", "1");
        for (String key:doc.keySet()
             ) {
            System.out.println(key+":"+doc.get(key));
        }

    }
    
    @Test
    public void test3() throws IOException{
        boolean index = EsClientUtil.deleteIndex("imgs");
    }

    @Test
    public void test4(){
        String s = RandomUtil.randomNumbers(8);
        System.out.println(s);
    }

    @Test
    public void test5() throws IOException {

        List<ImgDetailSearchEntity> list = new ArrayList<>();
        // 获取内容

        ImgDetailSearchEntity imgDetailSearchEntity1 = new ImgDetailSearchEntity();
        imgDetailSearchEntity1.setUsername("xiaozhao");
        imgDetailSearchEntity1.setContent("这是蜡笔小新壁纸");
        imgDetailSearchEntity1.setUserId(324L);

        ImgDetailSearchEntity imgDetailSearchEntity2 = new ImgDetailSearchEntity();
        imgDetailSearchEntity2.setUsername("xi231");
        imgDetailSearchEntity2.setContent("这是柯南壁纸");
        imgDetailSearchEntity2.setUserId(321234L);

        ImgDetailSearchEntity imgDetailSearchEntity3 = new ImgDetailSearchEntity();
        imgDetailSearchEntity3.setUsername("ccz");
        imgDetailSearchEntity3.setContent("新的动漫的头像");
        imgDetailSearchEntity3.setUserId(42114L);
        ImgDetailSearchEntity imgDetailSearchEntity4 = new ImgDetailSearchEntity();
        imgDetailSearchEntity4.setUsername("qwer");
        imgDetailSearchEntity4.setContent("好看的风景头像");
        imgDetailSearchEntity4.setUserId(321214L);

        list.add(imgDetailSearchEntity1);

        list.add(imgDetailSearchEntity2);
        list.add(imgDetailSearchEntity3);
        list.add(imgDetailSearchEntity4);
        // 内容放入 es 中
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m"); // 可更具实际业务是指
        for (int i = 0; i < list.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("imgs")
                            .id(String.valueOf(list.get(i).getUserId()))
                            .source(JSON.toJSONString(list.get(i)), XContentType.JSON)
            );
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        restHighLevelClient.close();

        System.out.println(list);
//        return !bulk.hasFailures();

    }

    @Test
    public void test6() throws IOException {


        HashMap<String, Object> map = new HashMap<>();
        map.put("page",1);
        map.put("limit",8);
        map.put("username","");
        map.put("userId","5");
        map.put("content","ee82");


        SearchRequest searchRequest = new SearchRequest("imgs");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 精确查询，添加查询条件
//        String keyUsername = (String)map.get("username");
//        String keyUserId = (String)map.get("userId");
        String keyContent = (String)map.get("username");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("userId", "42114");
//        MatchQueryBuilder userId1 = QueryBuilders.matchQuery("userId", keyUserId);
//        MatchQueryBuilder queryusername1 = QueryBuilders.matchQuery("userId",keyUsername);
        MatchQueryBuilder content1 = QueryBuilders.matchQuery("content","壁纸");
        MatchQueryBuilder content2 = QueryBuilders.matchQuery("username","ccz");
        BoolQueryBuilder should = boolQueryBuilder.should(content1).should(content2).should(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchSourceBuilder.query(should);    // 分页
        Integer pageIndex = (Integer) map.get("page");
        Integer pageSize = (Integer)map.get("limit");
        searchSourceBuilder.from(1);
        searchSourceBuilder.size(8);    // 高亮 =========
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder.field("content");
//        highlightBuilder.preTags("<span style='color:red'>");
//        highlightBuilder.postTags("</span>");
//        searchSourceBuilder.highlighter(highlightBuilder);        // 执行查询
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);    // 解析结果 ==========
        SearchHits hits = searchResponse.getHits();


        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit documentFields : hits.getHits()) {                // 使用新的字段值（高亮），覆盖旧的字段值
//            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();         // 高亮字段
//            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
//            HighlightField name = highlightFields.get("content");        // 替换
//            if (name != null) {
//                Text[] fragments = name.fragments();
//                StringBuilder new_name = new StringBuilder();
//                for (Text text : fragments) {
//                    new_name.append(text);
//                }
//
//                sourceAsMap.put("content", new_name.toString());
//            }
//            results.add(sourceAsMap);
            String sourceAsString = documentFields.getSourceAsString();
            ImgDetailSearchEntity imgDetailSearchEntity = JSON.parseObject(sourceAsString, ImgDetailSearchEntity.class);

            System.out.println(imgDetailSearchEntity);
        }

//        System.out.println("结果"+results);
//        for (Map<String, Object> model: results) {
//            for (String key: model.keySet()
//                 ) {
//                System.out.println(key+":"+model.get(key));
//            }
//        }
        // 返回查询的结果
        //return results;
    }
}
