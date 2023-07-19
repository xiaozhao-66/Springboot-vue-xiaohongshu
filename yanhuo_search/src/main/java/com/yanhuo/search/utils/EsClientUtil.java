package com.yanhuo.search.utils;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.common.settings.Settings;
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
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: lockie
 * @Date: 2019/11/27 17:38
 * @Description: es工具类
 */
@Component
public class EsClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(EsClientUtil.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static RestHighLevelClient client;

    private static final String INDEX_KEY = "index";
    private static final String INDEX = "spider";
    private static final String SHARDS = "index.number_of_shards";
    private static final String REPLICAS = "index.number_of_replicas";

    private static final String ID = "id";
    private static final String JSON_STR = "json";
    private static final String CREATED = "created";
    private static final String UPDATED = "updated";
    private static final String DELETED = "deleted";
    private static final String CONFLICT = "conflict";
    private static final String NOT_FOUND = "not_found";


    public static final String PAGE = "page";
    public static final String LIMIT = "limit";

    @PostConstruct
    public void init() {
        client = this.restHighLevelClient;
    }

    /**=========================================    索引    ===============================================**/

    /**
     * 检查es索引是否存在
     *
     * @param indexName
     * @return
     */
    public static boolean checkIndexExist(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 创建es索引
     *
     * @param indexName
     * @return
     */
    public static boolean createIndex(String indexName) throws IOException {
        if (checkIndexExist(indexName)) {
            logger.error(indexName + " 索引已经存在");
            return false;
        }
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        // 指示是否所有节点都已确认请求
        if (response.isAcknowledged() || response.isShardsAcknowledged()) {
            logger.info("索引{}创建成功", indexName);
            return true;
        }
        return false;
    }

    /**
     * 创建索引，指定分片数，副本数
     *
     * @param indexName
     * @param shards
     * @param replicas
     * @return
     * @throws IOException
     */
    public static boolean createIndex(String indexName, int shards, int replicas) throws IOException {
        if (checkIndexExist(indexName)) {
            logger.error(indexName + " 索引已经存在");
            return false;
        }
        Settings.Builder builder = Settings.builder().put(SHARDS, shards).put(REPLICAS, replicas);
        CreateIndexRequest request = new CreateIndexRequest(indexName).settings(builder);
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        if (response.isAcknowledged() || response.isShardsAcknowledged()) {
            logger.info("索引{}创建成功", indexName);
            return true;
        }
        return false;
    }

    /**
     * 删除索引
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public static boolean deleteIndex(String indexName) throws IOException {
        if (!checkIndexExist(indexName)) {
            logger.error(indexName + " 索引不存在");
            return false;
        }
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        if (response.isAcknowledged()) {
            logger.info("索引{}删除成功", indexName);
            return true;
        }
        return false;
    }

    /**
     * 开启索引
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public static boolean openIndex(String indexName) throws IOException {
        if (!checkIndexExist(indexName)) {
            logger.error(indexName + " 索引不存在");
            return false;
        }
        OpenIndexRequest request = new OpenIndexRequest(indexName);
        OpenIndexResponse response = client.indices().open(request, RequestOptions.DEFAULT);
        // 指示是否所有节点都已确认请求
        if (response.isAcknowledged() || response.isShardsAcknowledged()) {
            logger.info("索引{}开启成功", indexName);
            return true;
        }
        return false;
    }

    /**
     * 关闭索引
     * @param indexName
     * @return
     * @throws IOException
     */
//    public static boolean closeIndex(String indexName) throws IOException {
//        if (!checkIndexExist(indexName)) {
//            logger.error(indexName + " 索引不存在");
//            return false;
//        }
//        CloseIndexRequest request = new CloseIndexRequest(indexName);
//        AcknowledgedResponse response = client.indices().close(request, RequestOptions.DEFAULT);
//        // 指示是否所有节点都已确认请求
//        if (response.isAcknowledged()) {
//            logger.info("索引{}关闭成功",indexName);
//            return true;
//        }
//        return false;
//    }

    /**=========================================    索引    ===============================================**/


    /**
     * =========================================    mapping    ===============================================
     **/

    private static XContentBuilder createBuilder() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        builder.startObject("properties");
        builder.startObject("message");
        builder.field("type", "text");
        // 为message字段设置分词器
//                    builder.field("analyzer", "ik_smart");
        builder.endObject();

        builder.startObject("timestamp");
        builder.field("type", "dateTime");
        // 设置日期时间格式为Long类型
        builder.field("format", "epoch_millis");
        builder.endObject();
        builder.endObject();
        builder.endObject();
        return builder;
    }

    public void setFieldsMapping(String indexName) throws IOException {
        if (!checkIndexExist(indexName)) {
            return;
        }
        try {
            PutMappingRequest request = new PutMappingRequest(indexName);
            request.source(createBuilder());
            AcknowledgedResponse response = client.indices().putMapping(request, RequestOptions.DEFAULT);
            if (response.isAcknowledged()) {
                logger.info(indexName + " mapping新增成功");
            }
        } catch (IOException e) {
            logger.error("文档设置类型映射失败");
            return;
        }
    }

    /**
     * 新增或者更新mapping
     * @param indexName
     * @param mapping
     * @return
     * @throws IOException
     */
//    public static boolean createOrUpdateMapping(String indexName, IElasticSearchMapping mapping) throws IOException {
//        try {
//            // mapping不存在则新增，存在则更新
//            if (!checkMappingExist(indexName)) {
//                putMapping(indexName, mapping);
//                return true;
//            } else {
//                postMapping(indexName, mapping);
//                return true;
//            }
//
//        } catch (IOException e) {
//            logger.error("创建或者更新mapping失败！", e);
//            return false;
//        }
//    }


    /**
     * 检查es的mapping是否存在
     *
     * @param indexName
     * @return
     */
//    public static boolean checkMappingExist(String indexName) throws IOException {
//        GetMappingsRequest request = new GetMappingsRequest().indices(indexName);
//        GetMappingsResponse response = client.indices().getMapping(request, RequestOptions.DEFAULT);
//        Map<String, MappingMetaData> mappings = response.mappings();
//        Map<String, Object> sourceAsMap = mappings.get(indexName).sourceAsMap();
//        if (mappings != null && (sourceAsMap != null && sourceAsMap.size() > 0)) {
//            return true;
//        } else {
//            return false;
//        }
//    }


    /**
     * 创建mapping，put方式
     * @param indexName
     * @param mapping
     * @return
     */
//    public static boolean putMapping(String indexName, IElasticSearchMapping mapping) throws IOException {
//        if (!checkIndexExist(indexName)) {
//            logger.error(indexName + " 索引不存在");
//            return false;
//        }
//        String mappingJson = Strings.toString(mapping.getMapping());
//        PutMappingRequest putMappingRequest = new PutMappingRequest(indexName).source(mappingJson, XContentType.JSON);
//        AcknowledgedResponse response = client.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
//        return response.isAcknowledged();
//    }

    /**
     * 更新mapping,post方式
     * @param indexName
     * @param mapping
     * @return
     */
//    public static boolean postMapping(String indexName, IElasticSearchMapping mapping) throws IOException {
//        if (!checkIndexExist(indexName)) {
//            logger.error(indexName + " 索引不存在");
//            return false;
//        }
//        XContentBuilder xContentBuilder = mapping.getMapping();
//        Map<String, Object> map = XContentHelper.convertToMap(BytesReference.bytes(xContentBuilder), true, xContentBuilder.contentType()).v2();
//        Map<String, Object> indexMap = (Map<String, Object>) map.get(mapping.getIndexType());
//        PutMappingRequest request = new PutMappingRequest(indexName).source(indexMap);
//        AcknowledgedResponse response = client.indices().putMapping(request, RequestOptions.DEFAULT);
//        return response.isAcknowledged();
//    }


    /**
     * =========================================    mapping    ===============================================
     **/


    /**
     * 增加文档
     *
     * @param indexName
     * @param id
     * @param jsonString
     * @throws IOException
     */
    public static void addDocByJson(String indexName, String id, String jsonString) throws IOException {
        if (!checkIndexExist(indexName)) {
            createIndex(indexName);
        }
        // request的opType默认是INDEX(传入相同id会覆盖原document，CREATE则会将旧的删除)
        IndexRequest request = new IndexRequest(indexName).id(id).source(jsonString, XContentType.JSON);
        IndexResponse response = null;
        try {
            response = client.index(request, RequestOptions.DEFAULT);

            String index = response.getIndex();
            String docId = response.getId();
            if (response.getResult().getLowercase().equals(CREATED)) {
                logger.info("文档新增成功! index：{}, id：{}", index, docId);
            } else if (response.getResult().equals("updated")) {
                logger.info("文档修改成功！index：{}, id：{}", index, docId);
            }

            // 分片处理信息
            ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                logger.error(id + " 文档未写入全部分片副本");
            }
            // 获取分片副本写入失败
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    logger.error(id + " 写入副本失败原因：{}", failure.reason());
                }
            }
        } catch (ElasticsearchException e) {
            if (e.status().equals("conflict")) {
                logger.error("版本冲突");
            }
            logger.error("新增文档失败！{}", e);
        }
    }

    /**
     * 查找文档
     *
     * @param indexName
     * @param id
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getDoc(String indexName, String id) throws IOException {
        Map<String, Object> resultMap = new HashedMap();
        GetRequest request = new GetRequest(indexName, id);
        // 实时
        request.realtime(false);
        // 检索之前执行刷新
        request.refresh(true);

        GetResponse response = null;
        try {
            response = client.get(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            if (e.status().equals(NOT_FOUND)) {
                logger.error(id + " 文档未找到");
            }
            if (e.status().equals(CONFLICT)) {
                logger.error("版本冲突");
            }
            logger.error("查询文档失败！{}", e);
        }

        if (response != null) {
            if (response.isExists()) {
                resultMap = response.getSourceAsMap();
            } else {
                logger.error("文档未找到");
            }
        }
        return resultMap;
    }

    /**
     * 删除文档
     *
     * @param indexName
     * @param id
     * @throws IOException
     */
    public static void deleteDoc(String indexName, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(indexName, id);
        DeleteResponse response = null;
        try {
            response = client.delete(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            if (e.status().equals(CONFLICT)) {
                logger.error("版本冲突");
            }
            logger.error("删除文档失败！{}", e);
        }

        if (response != null) {
            if (response.getResult().getLowercase().equals(NOT_FOUND)) {
                logger.error("不存在该文档");
            }

            // 副本删除
            ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                logger.error("部分分片副本未处理");
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    logger.error("删除失败原因:{}", failure.reason());
                }
            }
            logger.info("删除文档成功,index：{},id：{}", indexName, id);
        }
    }

    /**
     * 通过一个JSON字符串更新文档，如果文档不存在，则新建文档
     *
     * @param indexName
     * @param id
     * @param jsonString
     * @throws IOException
     */
    public static void updateDocByJson(String indexName, String id, String jsonString) throws IOException {
        if (!checkIndexExist(indexName)) {
            createIndex(indexName);
        }
        UpdateRequest request = new UpdateRequest(indexName, id);
        request.doc(jsonString, XContentType.JSON);
        // 如果要更新的文档不存在，则根据传入的参数新建一个文档
        request.docAsUpsert(true);
        try {
            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
            String index = response.getIndex();
            String docId = response.getId();
            if (response.getResult().getLowercase().equals(CREATED)) {
                logger.info("文档增加成功，index：{}，id：{}", index, docId);
            } else if (response.getResult().getLowercase().equals(UPDATED)) {
                logger.info("文档更新成功，index：{}，id：{}", index, docId);
            } else if (response.getResult().getLowercase().equals(DELETED)) {
                logger.error("文档已经被删除，无法更新");
            }

            ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                logger.error("分片副本未全部处理");
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    logger.error("未处理原因：{}", failure.reason());
                }
            }
        } catch (ElasticsearchException e) {
            if (e.status().equals("not_found")) {
                logger.error("不存在该文档");
            } else if (e.status().equals("conflict")) {
                logger.error("版本冲突");
            }
        }
    }

    /**
     * 批量增加文档，可以支持同时增加不同索引文档，格式 [{"index":"xxx","id":"xx","json":"xxx"}, {"index":"xxx","id":"xx","json":"xxx"}]
     *
     * @param params
     * @throws IOException
     */
    public static void bulkAdd(List<Map<String, String>> params) throws IOException {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, String> map : params) {
            String index = map.getOrDefault(INDEX_KEY, INDEX);
            String id = map.get(ID);
            String jsonString = map.get(JSON_STR);
            if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(jsonString)) {
                IndexRequest request = new IndexRequest(index).id(id).source(jsonString, XContentType.JSON);
                bulkRequest.add(request);
            }
        }
        // 超时时间，2分钟
        bulkRequest.timeout(TimeValue.timeValueMinutes(2L));
        // 刷新策略
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        if (bulkRequest.numberOfActions() == 0) {
            logger.error("批量增加失败，参数为空");
            return;
        }
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        // 全部操作成功
        if (!bulkResponse.hasFailures()) {
            logger.info("批量增加操作成功");
        } else {
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    logger.error("批量增加失败，原因：{}", failure.getMessage());
                } else {
                    logger.info("文档批量增加成功");
                }
            }
        }
    }

    /**
     * 批量更新文档，，可以支持同时增加不同索引文档，格式 [{"index":"xxx","id":"xx","json":"xxx"}, {"index":"xxx","id":"xx","json":"xxx"}]
     *
     * @param params
     * @throws IOException
     */
    public static void bulkUpdate(List<Map<String, String>> params) throws IOException {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, String> map : params) {
            String index = map.getOrDefault(INDEX_KEY, INDEX);
            String id = map.get(ID);
            String jsonString = map.get(JSON_STR);
            if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(jsonString)) {
                UpdateRequest request = new UpdateRequest(index, id).doc(jsonString, XContentType.JSON);
                request.docAsUpsert(true);
                bulkRequest.add(request);
            }
        }
        if (bulkRequest.numberOfActions() == 0) {
            logger.error("批量更新失败！，参数为空");
            return;
        }
        bulkRequest.timeout(TimeValue.timeValueMinutes(2L));
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        if (!bulkResponse.hasFailures()) {
            logger.info("批量更新成功！");
            return;
        } else {
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    logger.error("批量更新失败，原因：{}", failure.getMessage());
                } else {
                    logger.info("文档批量更新成功");
                }
            }
        }
    }

    /**
     * 批量删除,支持不同索引 [{"index":"xx","id":"xx"}, {"index":"xx","id":"xx"}]
     *
     * @param params
     * @throws IOException
     */
    public static void bulkDelete(List<Map<String, String>> params) throws IOException {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, String> param : params) {
            String index = param.getOrDefault(INDEX_KEY, INDEX);
            String id = param.get(ID);
            if (StringUtils.isNotBlank(id)) {
                DeleteRequest request = new DeleteRequest(index, id);
                bulkRequest.add(request);
            }
        }
        if (bulkRequest.numberOfActions() == 0) {
            logger.error("批量删除失败，参数为空");
            return;
        }
        bulkRequest.timeout(TimeValue.timeValueMinutes(2L));
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        if (!bulkResponse.hasFailures()) {
            logger.info("批量删除成功");
        } else {
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    logger.error("批量删除失败，原因：{}", failure.getMessage());
                } else {
                    logger.info("文档批量删除成功");
                }
            }
        }
    }

    /**
     * 批量获取,支持获取不同索引的文档，格式：[{"index":"xx","id":"xx"}, {"index":"xx","id":"xx"}]
     *
     * @param params
     * @return
     * @throws IOException
     */
    public static List<Map<String, Object>> multiGet(List<Map<String, String>> params) throws IOException {
        List<Map<String, Object>> resultList = new ArrayList<>();

        MultiGetRequest request = new MultiGetRequest();
        for (Map<String, String> param : params) {
            String index = param.getOrDefault(INDEX_KEY, INDEX);
            String id = param.get(ID);
            if (StringUtils.isNotBlank(id)) {
                request.add(new MultiGetRequest.Item(index, id));
            }
        }
        request.realtime(false);
        request.refresh(true);
        MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
        List<Map<String, Object>> list = parseMGetResponse(response);
        if (CollectionUtils.isNotEmpty(list)) {
            resultList.addAll(list);
        }
        return resultList;
    }

    private static List<Map<String, Object>> parseMGetResponse(MultiGetResponse response) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        MultiGetItemResponse[] responses = response.getResponses();
        for (MultiGetItemResponse itemResponse : responses) {
            GetResponse getResponse = itemResponse.getResponse();
            if (getResponse != null) {
                if (!getResponse.isExists()) {
                    logger.error("文档查找失败, index：{}, id：{}", getResponse.getIndex(), getResponse.getId());
                } else {
                    resultList.add(getResponse.getSourceAsMap());
                }
            } else {
                MultiGetResponse.Failure failure = itemResponse.getFailure();
                ElasticsearchException e = (ElasticsearchException) failure.getFailure();
                if (e.status().equals("not_found")) {
                    logger.error("文档不存在,index={}, id={}", getResponse.getIndex(), getResponse.getId());
                } else if (e.status().equals("conflict")) {
                    logger.error("文档冲突,index={}, id={}", getResponse.getIndex(), getResponse.getId());
                }
            }
        }
        return resultList;
    }


    public List<Map<String, Object>> highlightSearch(Map<String, Object> map) throws IOException {
        String index = (String) map.getOrDefault(INDEX_KEY, INDEX);
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 精确查询，添加查询条件
        String keyUsername = (String) map.get("username");
        String keyUserId = (String) map.get("userId");
        String keyContent = (String) map.get("content");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", key);
        MatchQueryBuilder userId1 = QueryBuilders.matchQuery("userId", keyUserId);
        MatchQueryBuilder queryusername1 = QueryBuilders.matchQuery("userId", keyUsername);
        MatchQueryBuilder content1 = QueryBuilders.matchQuery("keyContent", keyContent);
        BoolQueryBuilder should = boolQueryBuilder.should(userId1).should(queryusername1).should(content1);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchSourceBuilder.query(should);    // 分页
        Integer pageIndex = (Integer) map.get(PAGE);
        Integer pageSize = (Integer) map.get(LIMIT);
        searchSourceBuilder.from(pageIndex);
        searchSourceBuilder.size(pageSize);    // 高亮 =========
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("keyContent");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);        // 执行查询
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);    // 解析结果 ==========
        SearchHits hits = searchResponse.getHits();
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit documentFields : hits.getHits()) {                // 使用新的字段值（高亮），覆盖旧的字段值
            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();         // 高亮字段
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField name = highlightFields.get("keyContent");        // 替换
            if (name != null) {
                Text[] fragments = name.fragments();
                StringBuilder new_name = new StringBuilder();
                for (Text text : fragments) {
                    new_name.append(text);
                }

                sourceAsMap.put("keyContent", new_name.toString());
            }
            results.add(sourceAsMap);
        }
        return results;
    }
}