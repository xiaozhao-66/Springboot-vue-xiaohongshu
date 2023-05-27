package com.xz.recommend.service.impl;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.xz.common.recommend.RecommendUtils2;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.PageUtils;
import com.xz.common.utils.RedisUtils;
import com.xz.recommend.common.client.EsClient;
import com.xz.recommend.common.client.RecommendClient;
import com.xz.recommend.dao.*;
import com.xz.recommend.dto.ImgDetailsDTO;
import com.xz.recommend.entity.*;
import com.xz.recommend.service.CategoryService;
import com.xz.recommend.service.ImgDetailsService;
import com.xz.recommend.utils.IKSegmenterUtil;
import com.xz.recommend.utils.PearsonUtils;
import com.xz.recommend.utils.RecommendUtils;
import com.xz.recommend.vo.ImgDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Service
public class ImgDetailsServiceImpl extends CrudServiceImpl<ImgDetailsDao, ImgDetailsEntity, ImgDetailsDTO> implements ImgDetailsService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserDao userDao;

    @Autowired
    BrowseRecordDao browseRecordDao;

    @Autowired
    TagImgRelationDao tagImgRelationDao;

    @Autowired
    TagDao tagDao;

    @Autowired
    RecommendClient recommendClient;

    @Autowired
    EsClient esClient;


    @Override
    public QueryWrapper<ImgDetailsEntity> getWrapper(Map<String, Object> params) {
        return null;
    }

    /**
     * @param uid 用户id
     * @return
     */
    public Map<String, HashMap<String, Integer>> createMap(String uid) {
        HashMap<String, HashMap<String, Integer>> res = new HashMap<>();
        HashMap<String, Integer> umap = new HashMap<>();
        String key = "br:" + uid + ":";
        Set<String> listKey = redisUtils.getListKey(key);
        //首先计算当前用户浏览记录，统计用户浏览记录
        if (!listKey.isEmpty()) {
            for (String k : listKey) {
                String[] split = k.split(":");
                umap.put(split[2], Integer.valueOf(redisUtils.get(k)));
            }
        }
        res.put(uid, umap);
        return res;
    }

    /**
     * 根据标签，分类，内容计算
     * @param imgDetailsEntity
     * @return
     */
    private String computerSource(ImgDetailsEntity imgDetailsEntity) {

        List<String> keyWords = IKSegmenterUtil.parse(imgDetailsEntity.getContent(), true);
        Set<String> contentSet = new HashSet<>(keyWords);

        TagImgRelationEntity tagImgRelationEntity = tagImgRelationDao.selectOne(new QueryWrapper<TagImgRelationEntity>().eq("mid", imgDetailsEntity.getId()));

        String tagIdstr = tagImgRelationEntity.getTagIds();
        String[] ids = tagIdstr.split(";");
        List<TagEntity> tags = tagDao.selectBatchIds(Arrays.asList(ids));
        for (TagEntity tag : tags) {
            contentSet.add(tag.getName());
        }

        CategoryEntity categoryEntity = categoryService.selectById(imgDetailsEntity.getCategoryPid());
        contentSet.add(categoryEntity.getName());
        CategoryEntity categoryEntity2 = categoryService.selectById(imgDetailsEntity.getCategoryId());
        contentSet.add(categoryEntity2.getName());

        StringBuilder sb = new StringBuilder();
        for (String e : contentSet) {
            sb.append(e);
            sb.append("|");
        }
        return sb.toString();
    }


    private String computerSource(List<ImgDetailsEntity> imgDetailsEntityList) {

        Set<String> contentSet = new HashSet<>();

        for (ImgDetailsEntity model : imgDetailsEntityList) {

            List<String> keyWords = IKSegmenterUtil.parse(model.getContent(), true);
            contentSet.addAll(keyWords);

            TagImgRelationEntity tagImgRelationEntity = tagImgRelationDao.selectOne(new QueryWrapper<TagImgRelationEntity>().eq("mid", model.getId()));

            String tagIdstr = tagImgRelationEntity.getTagIds();
            String[] ids = tagIdstr.split(";");
            List<TagEntity> tags = tagDao.selectBatchIds(Arrays.asList(ids));
            for (TagEntity tag : tags) {
                contentSet.add(tag.getName());
            }

            CategoryEntity categoryEntity = categoryService.selectById(model.getCategoryPid());
            contentSet.add(categoryEntity.getName());
            CategoryEntity categoryEntity2 = categoryService.selectById(model.getCategoryId());
            contentSet.add(categoryEntity2.getName());
        }
        StringBuilder sb = new StringBuilder();
        for (String e : contentSet) {
            sb.append(e);
            sb.append("|");
        }
        return sb.toString();
    }


    /**
     * 根据最近邻接点相似计算
     *
     * @param uid
     * @return
     */
    public List<ImgDetailsEntity> imgDetailsEntityList(String uid) {
        //首先计算当前用户浏览记录，统计用户浏览记录
        Map<String, HashMap<String, Integer>> map = createMap(uid);
        //如果当前用户的浏览记录为空直接返回
        if (map.get(uid).isEmpty()) {
            return new ArrayList<>();
        }
        //统计所有二级分类
        List<String> allCategoryTwo = categoryService.getAllCategoryTwo();

        //当前用户的分数
        double[] currentList = RecommendUtils.getArray(allCategoryTwo, map.get(uid));

        //随机选择50个用户
        List<String> uids = userDao.selectRand(50);

        Map<String, Double> sourceMap = new HashMap<>();

        Map<String, HashMap<String, Integer>> userMap = null;
        //找到相似的用户并将相似用户浏览记录中用户没有浏览的数据给用户查看
        for (String id : uids) {
            userMap = createMap(id);
            double[] otherList = RecommendUtils.getArray(allCategoryTwo, userMap.get(id));
            //计算分数
            Double relate = PearsonUtils.getPearsonCorrelationScore(currentList, otherList);
            if (!relate.isNaN()) {
                sourceMap.put(id, relate);
            }

        }

        Map<String, Double> sourceMap2 = sourceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new));

        List<ImgDetailsEntity> res = new ArrayList<>();

        //得到当前用户前30条浏览记录
        List<String> currentMidList = browseRecordDao.selectNums(uid, 30);
        List<ImgDetailsEntity> imgDetailList = baseDao.selectBatchIds(currentMidList);

        List<String> stringList = null;
        List<ImgDetailsEntity> otherImgDetailList = null;
        for (String key : sourceMap2.keySet()) {
            if (StringUtils.equals(uid, key)) {
                continue;
            }
            //得到用户所有的浏览记录
            stringList = browseRecordDao.selectNums(key, 100);
            otherImgDetailList = baseDao.selectBatchIds(stringList);

            for (ImgDetailsEntity e : otherImgDetailList) {
                if (!imgDetailList.contains(e) && !res.contains(e)) {
                    res.add(e);
                }
            }
        }

        return res;
    }

    // 推荐算法1(旧的推荐方式:使用协同过滤算法)
    @Override
    public Page<ImgDetailVo> recommendToUser(long page, long limit, String uid) {

        List<ImgDetailsEntity> res = imgDetailsEntityList(uid);
        //返回前20条数据(如果刷新则重新计算)
        return getImgDetailVoPage((int) page, (int) limit, res);
    }

    /**
     * 推荐算法2(旧的推荐方式:根据文本相似性计算)
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @Override
    public Page<ImgDetailVo> recommendToUser2(long page, long limit, String uid) {


        String ukey = "brimg:" + uid;
        //这里获取当前用户的前20条浏览记录
        Set<String> mids = redisUtils.zReverseRange(ukey, 0, 20);

        List<ImgDetailsEntity> imgDetailList = baseDao.selectBatchIds(mids);

        //结合协同过滤算法做推荐
        List<ImgDetailsEntity> imgDetailsByComList = imgDetailsEntityList(uid);

        imgDetailList.addAll(imgDetailsByComList);

        RecommendUtils mySimHash1 = new RecommendUtils(computerSource(imgDetailList), 64);

        //选择最新的1000数据
        List<ImgDetailsEntity> imgDetailsEntityList = baseDao.selectLimit(1000);
        Collections.shuffle(imgDetailsEntityList);
        //数据量太少，数据量多了的话就随机选择前500条
        List<List<ImgDetailsEntity>> partition = Lists.partition(imgDetailsEntityList, 50);

        //更改使用es获取数据
//        List<ImgDetailSearchVo> imgDetailSearchVos = null;
//        try {
//            imgDetailSearchVos = esClient.esSearchList();
//
//        }catch (Exception e) {
//             throw new RuntimeException("es数据查找异常");
//        }
//        List<Long> ids = imgDetailSearchVos.stream().map(ImgDetailSearchVo::getId).collect(Collectors.toList());
//        List<ImgDetailsEntity> imgDetailsEntityList = baseDao.selectBatchIds(ids);


        //计算相似性
        Map<ImgDetailsEntity, Double> map = new HashMap<>();

        for (ImgDetailsEntity model : partition.get(0)) {
            RecommendUtils mySimHash2 = new RecommendUtils(computerSource(model), 64);
            Double similar = mySimHash1.getSimilar(mySimHash2);
            map.put(model, similar);
        }


        Map<ImgDetailsEntity, Double> map2 = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new));

        List<ImgDetailsEntity> list = new ArrayList<>();
        for (ImgDetailsEntity key : map2.keySet()) {
            if (!imgDetailList.contains(key)) {
                list.add(key);
            }
        }
        return getImgDetailVoPage((int) page, (int) limit, list);
    }


    @NotNull
    private Page<ImgDetailVo> getImgDetailVoPage(int page, int limit, List<ImgDetailsEntity> list) {
        List<ImgDetailVo> resultList = new ArrayList<>();
        ImgDetailVo imgDetailVo = null;
        List<String> imgList = null;
        UserEntity user = null;
        for (ImgDetailsEntity model : list) {
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            imgDetailVo.setImgsUrl(imgList);
            user = userDao.selectOne(new QueryWrapper<UserEntity>().eq("id", model.getUserId()));
            imgDetailVo.setUserId(user.getId());
            imgDetailVo.setUsername(user.getUsername());
            imgDetailVo.setAvatar(user.getAvatar());
            resultList.add(imgDetailVo);
        }

        return Objects.requireNonNull(PageUtils.getPages(page, limit, resultList));
    }

    /**
     * 新的推荐方式（使用机器学习模型做推荐系统）
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @Override
    public HashMap<String,Object>  newRecommendToUser(long page, long limit, String uid) throws ModelException, TranslateException, IOException {


        HashMap<String,Object> resMap = new HashMap<>(2);

        String ukey = "brimg:" + uid;
        //这里获取当前用户的前10条浏览记录
        Set<String> mids = redisUtils.zReverseRange(ukey, 0, 10);

        List<ImgDetailsEntity> imgDetailList = baseDao.selectBatchIds(mids);

        //得到用户浏览记录的分类情况
        String content1 = getContent(imgDetailList);
        float[] embeddings1 = RecommendUtils2.getEmbeddings(content1);

        //TODO 选择最新数据(这里的参数后面都要修改，数据量太少了)
        Page<ImgDetailsEntity> pageInfo = baseDao.selectPage(new Page<>((int) page, 30), new QueryWrapper<ImgDetailsEntity>().orderByDesc("update_date"));

        List<ImgDetailsEntity> imgDetailsEntityList = pageInfo.getRecords();
        Collections.shuffle(imgDetailsEntityList);

        resMap.put("total",30);


        List<List<ImgDetailsEntity>> partition = Lists.partition(imgDetailsEntityList, (int)limit);

        //计算相似性
        Map<ImgDetailsEntity, Double> map = new HashMap<>();

        for (ImgDetailsEntity model : partition.get(0)) {
              //计算词向量部分需要的时间过长，修改使用redis存储词向量
            float[] embeddings2 = null;
            String key = "recommend:"+model.getId();
            if(Boolean.TRUE.equals(redisUtils.hasKey(key))){
                embeddings2 = JSON.parseObject(redisUtils.get(key),float[].class);
            }else{
                String content2 = getContent(model);
                embeddings2 = RecommendUtils2.getEmbeddings(content2);
                redisUtils.set(key,JSON.toJSONString(embeddings2));
            }

            Double value = RecommendUtils2.getSimilar(embeddings1, embeddings2);
            map.put(model, value);
        }

        Map<ImgDetailsEntity, Double> map2 = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new));

        List<ImgDetailsEntity> list = new ArrayList<>();
        for (ImgDetailsEntity key : map2.keySet()) {
            if (!imgDetailList.contains(key)) {
                list.add(key);
            }
        }

        List<ImgDetailVo> records = new ArrayList<>();
        ImgDetailVo imgDetailVo = null;
        List<String> imgList = null;
        UserEntity user = null;
        for (ImgDetailsEntity model : list) {
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            imgDetailVo.setImgsUrl(imgList);
            user = userDao.selectOne(new QueryWrapper<UserEntity>().eq("id", model.getUserId()));
            imgDetailVo.setUserId(user.getId());
            imgDetailVo.setUsername(user.getUsername());
            imgDetailVo.setAvatar(user.getAvatar());
            records.add(imgDetailVo);
        }

        resMap.put("records",records);
        return resMap;
    }



    private String getContent(ImgDetailsEntity imgDetailsEntity) {

        Set<String> contentSet = new HashSet<>();

        TagImgRelationEntity tagImgRelationEntity = tagImgRelationDao.selectOne(new QueryWrapper<TagImgRelationEntity>().eq("mid", imgDetailsEntity.getId()));

        String tagIdstr = tagImgRelationEntity.getTagIds();
        String[] ids = tagIdstr.split(";");
        List<TagEntity> tags = tagDao.selectBatchIds(Arrays.asList(ids));
        for (TagEntity tag : tags) {
            contentSet.add(tag.getName());
        }

        CategoryEntity categoryEntity = categoryService.selectById(imgDetailsEntity.getCategoryPid());
        contentSet.add(categoryEntity.getName());
        CategoryEntity categoryEntity2 = categoryService.selectById(imgDetailsEntity.getCategoryId());
        contentSet.add(categoryEntity2.getName());

        StringBuilder sb = new StringBuilder();
        for (String e : contentSet) {
            sb.append(e);
            sb.append("|");
        }
        return sb.toString();
    }

    /**
     * 只使用分类和标签计算
     * @param imgDetailsEntityList
     * @return
     */
    private String getContent(List<ImgDetailsEntity> imgDetailsEntityList) {

        Set<String> contentSet = new HashSet<>();

        for (ImgDetailsEntity model : imgDetailsEntityList) {

            TagImgRelationEntity tagImgRelationEntity = tagImgRelationDao.selectOne(new QueryWrapper<TagImgRelationEntity>().eq("mid", model.getId()));

            String tagIdstr = tagImgRelationEntity.getTagIds();
            String[] ids = tagIdstr.split(";");
            List<TagEntity> tags = tagDao.selectBatchIds(Arrays.asList(ids));
            for (TagEntity tag : tags) {
                contentSet.add(tag.getName());
            }

            CategoryEntity categoryEntity = categoryService.selectById(model.getCategoryPid());
            contentSet.add(categoryEntity.getName());
            CategoryEntity categoryEntity2 = categoryService.selectById(model.getCategoryId());
            contentSet.add(categoryEntity2.getName());
        }
        StringBuilder sb = new StringBuilder();
        for (String e : contentSet) {
            sb.append(e);
            sb.append("|");
        }
        return sb.toString();
    }



}