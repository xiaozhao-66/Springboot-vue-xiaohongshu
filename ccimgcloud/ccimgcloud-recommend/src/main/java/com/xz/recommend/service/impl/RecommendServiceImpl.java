package com.xz.recommend.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.xz.common.constant.cacheConstant.ImgDetailCacheNames;
import com.xz.common.recommend.RecommendUtils2;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.RedisUtils;
import com.xz.recommend.dao.*;
import com.xz.recommend.entity.*;
import com.xz.recommend.service.RecommendService;
import com.xz.recommend.utils.KeywordsExtractor;
import com.xz.recommend.vo.ImgDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 新的推荐系统
 * @author 48423
 */
@Service
@Slf4j
public class RecommendServiceImpl extends BaseServiceImpl<ImgDetailsDao, ImgDetailsEntity> implements RecommendService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    UserDao userDao;

    @Autowired
    TagImgRelationDao tagImgRelationDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    TagDao tagDao;

    private static final String records = "records";
    private static final String total = "total";
    private static final int size = 4;

    /**
     * 新的推荐方式（使用机器学习模型做推荐系统）
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @Override
    public HashMap<String, Object> newRecommendToUser(long page, long limit, String uid) {


        HashMap<String, Object> resMap = new HashMap<>(2);

        String ukey = ImgDetailCacheNames.BR_IMG_KEY + uid;
        if (Boolean.FALSE.equals(redisUtils.hasKey(ukey))) {
            return null;
        }

        //这里获取当前用户的前n条浏览记录(这里只选择一条)
        List<String> mids = redisUtils.lRange(ukey, 0, 2);

        Set<ImgDetailsEntity> set = new HashSet<>();

        Set<ImgDetailsEntity> recommendSet;
        for (int i = 0; i < (mids.size() <= 1 ? 1 : 2); i++) {
            recommendSet = getRecommendImgDetails(page, limit, mids.get(i), mids);
            set.addAll(recommendSet);
        }

        List<ImgDetailVo> imgDetailVoList = new ArrayList<>();

        if (set.isEmpty()) {
            resMap.put(records, imgDetailVoList);
            resMap.put(total, set.size());
            return resMap;
        }
        
        ImgDetailVo imgDetailVo;
        UserEntity user;

        Set<Long> uids = set.stream().map(ImgDetailsEntity::getUserId).collect(Collectors.toSet());
        List<UserEntity> userList = userDao.selectBatchIds(uids);

        HashMap<Long, UserEntity> userMap = new HashMap<>();

        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        for (ImgDetailsEntity model : set) {
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            user = userMap.get(model.getUserId());
            imgDetailVo.setUserId(user.getId());
            imgDetailVo.setUsername(user.getUsername());
            imgDetailVo.setAvatar(user.getAvatar());
            imgDetailVoList.add(imgDetailVo);
        }

        resMap.put(records, imgDetailVoList);
        resMap.put(total, set.size());
        return resMap;
    }


    private Set<ImgDetailsEntity> getRecommendImgDetails(long page, long limit, String mid, List<String> mids) {

        Set<ImgDetailsEntity> resSet = new HashSet<>();

        float[] embeddings1 = null;

        String recommendByUserKey = ImgDetailCacheNames.RECOMMEND + mid;
        if (Boolean.TRUE.equals(redisUtils.hasKey(recommendByUserKey))) {
            embeddings1 = JSON.parseObject(redisUtils.get(recommendByUserKey), float[].class);
        } else {
            ImgDetailsEntity imgDetailsEntity = baseDao.selectById(mid);
            List<String> keywords1 = getContent(imgDetailsEntity);
            try {
                embeddings1 = RecommendUtils2.getEmbeddings(keywords1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            redisUtils.set(recommendByUserKey, JSON.toJSONString(embeddings1));
        }

        //TODO 选择数据(这里的参数后面都要修改，数据量太少了)
        String imgDetailKey = ImgDetailCacheNames.IMG_DETAIL_LIST_KEY;
        Map<Object, Object> imgMap = redisUtils.hGetAll(imgDetailKey);

        //所有的图片
        List<Object> imgDetailList = new ArrayList<>(imgMap.values());

        List<List<Object>> allImgDetailList = Lists.partition(imgDetailList, (int) limit);

        //每个里面有50条数据
        List<Object> imgDetailRecord = allImgDetailList.get((int) page - 1);

        if ((int) page >= allImgDetailList.size()) {
            return resSet;
        }

        Collections.shuffle(imgDetailRecord);

        //从20条记录中选择3条
        List<List<Object>> partition = Lists.partition(imgDetailRecord, 20);
        //计算相似性
        Map<ImgDetailsEntity, Double> map = new HashMap<>();

        for (Object element : partition.get(0)) {
            //计算词向量部分需要的时间过长，修改使用redis存储词向量
            ImgDetailsEntity model = JSON.parseObject((String) element, ImgDetailsEntity.class);
            float[] embeddings2 = null;
            String key = ImgDetailCacheNames.RECOMMEND + model.getId();
            if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
                embeddings2 = JSON.parseObject(redisUtils.get(key), float[].class);
            } else {
                List<String> keywords2 = getContent(model);
                try {
                    embeddings2 = RecommendUtils2.getEmbeddings(keywords2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                redisUtils.set(key, JSON.toJSONString(embeddings2));
            }

            Double value = RecommendUtils2.getSimilar(embeddings1, embeddings2);
            map.put(model, value);
        }

        Map<ImgDetailsEntity, Double> sortedMap = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new));

        for (ImgDetailsEntity model : sortedMap.keySet()) {
            if (!mids.contains(String.valueOf(model.getId()))) {
                resSet.add(model);
                if (resSet.size() >= size) {
                    break;
                }
            }
        }

        return resSet;

    }


    private List<String> getContent(ImgDetailsEntity imgDetailsEntity) {

        StringBuilder sb = new StringBuilder();
        String content = imgDetailsEntity.getContent();
        sb.append(content);

        TagImgRelationEntity tagImgRelationEntity = tagImgRelationDao.selectOne(new QueryWrapper<TagImgRelationEntity>().eq("mid", imgDetailsEntity.getId()));
        String tagIdstr = tagImgRelationEntity.getTagIds();
        String[] ids = tagIdstr.split(";");
        List<TagEntity> tags = tagDao.selectBatchIds(Arrays.asList(ids));

        for (TagEntity tag : tags) {
            sb.append(tag.getName());
        }

        List<Long> cidList = new ArrayList<>(2);
        cidList.add(imgDetailsEntity.getCategoryPid());
        cidList.add(imgDetailsEntity.getCategoryId());

        List<CategoryEntity> categorys = categoryDao.selectBatchIds(cidList);

        for (CategoryEntity category : categorys) {
            sb.append(category.getName());
        }

        return KeywordsExtractor.getKeywords(sb.toString());
    }

}
