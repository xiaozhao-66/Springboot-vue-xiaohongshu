package com.yanhuo.recommend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.yanhuo.common.constant.recommend.RecommendConstant;
import com.yanhuo.common.recommend.RecommendUtils2;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.JsonUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.recommend.dao.*;
import com.yanhuo.recommend.service.RecommendService;
import com.yanhuo.recommend.utils.KeywordsExtractor;
import com.yanhuo.xo.model.*;
import com.yanhuo.xo.vo.ImgDetailVo;
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
public class RecommendServiceImpl extends ServiceImpl<ImgDetailDao, ImgDetail> implements RecommendService {

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


    /**
     * 随机选择(因为linux中用不了机器学习推荐所以这里就随机推荐了)
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @Override
    public HashMap<String, Object> recommendToUserByCF(long page, long limit, String uid) {

        HashMap<String, Object> resMap = new HashMap<>(2);

        String ukey = RecommendConstant.BR_IMG_KEY + uid;
        if (Boolean.FALSE.equals(redisUtils.hasKey(ukey))) {
            return null;
        }

        //这里获取当前用户的前n条浏览记录(这里只选择一条)
        List<String> mids = redisUtils.lRange(ukey, 0, 5);

        List<ImgDetail> imgDetailRecords = this.listByIds(mids);

        String imgDetailKey = RecommendConstant.IMG_DETAIL_LIST_KEY;
        Map<Object, Object> imgMap = redisUtils.hGetAll(imgDetailKey);

        //所有的图片
        List<Object> imgDetailList = new ArrayList<>(imgMap.values());
        List<List<Object>> allImgDetailList = Lists.partition(imgDetailList, (int) limit);

        //每一份是50张图片（随机从50张中再选择6组合）
        List<Object> objectRecord = allImgDetailList.get((int) page - 1);

        Collections.shuffle(objectRecord);

        List<Object> selectImgDetailList = objectRecord.subList(0, 6);


        List<ImgDetail> imgDetails = new ArrayList<>();
        for (Object element:selectImgDetailList) {
            ImgDetail model = JsonUtils.parseObject((String) element, ImgDetail.class);
            if(!imgDetailRecords.contains(model)){
                imgDetails.add(model);
            }
        }

        List<Long> uids = imgDetails.stream().map(ImgDetail::getUserId).collect(Collectors.toList());
        List<User> userList = userDao.selectBatchIds(uids);

        HashMap<Long, User> userMap = new HashMap<>();
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        List<ImgDetailVo> imgDetailVoList = new ArrayList<>();
        ImgDetailVo imgDetailVo;
        User user;
        for (ImgDetail model : imgDetails) {
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            user = userMap.get(model.getUserId());
            imgDetailVo.setUserId(user.getId());
            imgDetailVo.setUsername(user.getUsername());
            imgDetailVo.setAvatar(user.getAvatar());
            imgDetailVoList.add(imgDetailVo);
        }

        resMap.put(RecommendConstant.RECORDS, imgDetailVoList);
        resMap.put(RecommendConstant.TOTAL, 24);
        return resMap;
    }



    /**
     * 新的推荐方式（使用机器学习模型做推荐系统）
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @Override
    public HashMap<String, Object> recommendToUser(long page, long limit, String uid) {
        HashMap<String, Object> resMap = new HashMap<>(2);

        String ukey = RecommendConstant.BR_IMG_KEY + uid;
        if (Boolean.FALSE.equals(redisUtils.hasKey(ukey))) {
            return null;
        }

        //这里获取当前用户的前n条浏览记录(这里只选择一条)
        List<String> mids = redisUtils.lRange(ukey, 0, 2);
        Set<ImgDetail> set = new HashSet<>();
        Set<ImgDetail> recommendSet;
        for (int i = 0; i < (mids.size() <= 1 ? 1 : 2); i++) {
            recommendSet = getRecommendImgDetails(page, limit, mids.get(i), mids);
            set.addAll(recommendSet);
        }

        List<ImgDetailVo> imgDetailVoList = new ArrayList<>();
        if (set.isEmpty()) {
            resMap.put(RecommendConstant.RECORDS, imgDetailVoList);
            resMap.put(RecommendConstant.TOTAL, set.size());
            return resMap;
        }

        ImgDetailVo imgDetailVo;
        User user;

        Set<Long> uids = set.stream().map(ImgDetail::getUserId).collect(Collectors.toSet());
        List<User> userList = userDao.selectBatchIds(uids);

        HashMap<Long, User> userMap = new HashMap<>();
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        for (ImgDetail model : set) {
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            user = userMap.get(model.getUserId());
            imgDetailVo.setUserId(user.getId());
            imgDetailVo.setUsername(user.getUsername());
            imgDetailVo.setAvatar(user.getAvatar());
            imgDetailVoList.add(imgDetailVo);
        }

        resMap.put(RecommendConstant.RECORDS, imgDetailVoList);
        resMap.put(RecommendConstant.TOTAL, set.size());
        return resMap;
    }


    private Set<ImgDetail> getRecommendImgDetails(long page, long limit, String mid, List<String> mids) {
        Set<ImgDetail> resSet = new HashSet<>();

        float[] embeddings1 = null;

        String recommendByUserKey = RecommendConstant.RECOMMEND + mid;
        if (Boolean.TRUE.equals(redisUtils.hasKey(recommendByUserKey))) {
            embeddings1 = JsonUtils.parseObject(redisUtils.get(recommendByUserKey), float[].class);
        } else {
            ImgDetail imgDetailsEntity = this.getById(mid);
            List<String> keywords1 = getContent(imgDetailsEntity);
            try {
                embeddings1 = RecommendUtils2.getEmbeddings(keywords1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            redisUtils.set(recommendByUserKey, JsonUtils.toJsonString(embeddings1));
        }

        //TODO 选择数据(这里的参数后面都要修改，数据量太少了)
        String imgDetailKey = RecommendConstant.IMG_DETAIL_LIST_KEY;
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
        Map<ImgDetail, Double> map = new HashMap<>();

        for (Object element : partition.get(0)) {
            //计算词向量部分需要的时间过长，修改使用redis存储词向量
            ImgDetail model = JsonUtils.parseObject((String) element, ImgDetail.class);
            float[] embeddings2 = null;
            String key = RecommendConstant.RECOMMEND + model.getId();
            if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
                embeddings2 = JsonUtils.parseObject(redisUtils.get(key), float[].class);
            } else {
                List<String> keywords2 = getContent(model);
                try {
                    embeddings2 = RecommendUtils2.getEmbeddings(keywords2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                redisUtils.set(key, JsonUtils.toJsonString(embeddings2));
            }

            Double value = RecommendUtils2.getSimilar(embeddings1, embeddings2);
            map.put(model, value);
        }

        Map<ImgDetail, Double> sortedMap = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new));

        for (ImgDetail model : sortedMap.keySet()) {
            if (!mids.contains(String.valueOf(model.getId()))) {
                resSet.add(model);
                if (resSet.size() >= RecommendConstant.SIZE) {
                    break;
                }
            }
        }

        return resSet;

    }


    private List<String> getContent(ImgDetail imgDetail) {
        StringBuilder sb = new StringBuilder();
        String content = imgDetail.getContent();
        sb.append(content);

        List<TagImgRelation> tagImgRelationList = tagImgRelationDao.selectList(new QueryWrapper<TagImgRelation>().eq("mid", imgDetail.getId()));
        List<Long> tids = tagImgRelationList.stream().map(TagImgRelation::getTid).collect(Collectors.toList());

        if(!tids.isEmpty()){
            List<Tag> tags = tagDao.selectBatchIds(tids);
            for (Tag tag : tags) {
                sb.append(tag.getName());
            }
        }

        List<Long> cidList = new ArrayList<>(2);
        cidList.add(imgDetail.getCategoryPid());
        cidList.add(imgDetail.getCategoryId());

        List<Category> categorys = categoryDao.selectBatchIds(cidList);

        for (Category category : categorys) {
            sb.append(category.getName());
        }
        return KeywordsExtractor.getKeywords(sb.toString());
    }

}
