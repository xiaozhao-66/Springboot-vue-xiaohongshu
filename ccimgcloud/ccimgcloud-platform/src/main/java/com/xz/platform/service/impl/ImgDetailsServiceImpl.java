package com.xz.platform.service.impl;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.constant.cacheConstant.ImgDetailCacheNames;
import com.xz.common.exception.ErrorCode;
import com.xz.common.exception.RenException;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.DateUtils;
import com.xz.common.utils.PageUtils;
import com.xz.platform.common.client.EsClient;
import com.xz.platform.common.client.OSSClient;
import com.xz.platform.common.constant.Constant;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.common.utils.WebUtils;
import com.xz.platform.dao.*;
import com.xz.platform.dto.AlbumImgRelationDTO;
import com.xz.platform.dto.ImgDetailsDTO;
import com.xz.platform.entity.*;
import com.xz.platform.service.AlbumImgRelationService;
import com.xz.platform.service.ImgDetailsService;
import com.xz.platform.service.TagService;
import com.xz.platform.vo.ImgDetailInfoVo;
import com.xz.platform.vo.ImgDetailSearchVo;
import com.xz.platform.vo.ImgDetailVo;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Service
public class ImgDetailsServiceImpl extends BaseServiceImpl<ImgDetailsDao, ImgDetailsEntity> implements ImgDetailsService {

    @Autowired
    AlbumImgRelationDao albumImgRelationDao;


    @Autowired
    AlbumDao albumDao;

    @Autowired
    TagImgRelationDao tagImgRelationDao;

    @Autowired
    TagService tagService;

    @Autowired
    TagDao tagDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserRecordDao userRecordDao;

    @Autowired
    OSSClient ossClient;

    @Autowired
    EsClient esClient;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CollectionDao collectionDao;


    @Autowired
    AlbumImgRelationService albumImgRelationService;

    @Autowired
    RedisUtils redisUtils;


    private static final String imgDetailKey = ImgDetailCacheNames.IMG_DETAIL_LIST_KEY;


    @NotNull
    private ImgDetailVo getImgDetailVo(ImgDetailsEntity model) {

        ImgDetailVo imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
        List<String> imgList = JSON.parseArray(model.getImgsUrl(), String.class);
        if (!imgList.isEmpty()) {
            imgDetailVo.setImgsUrl(imgList);
        }
        UserEntity user = userDao.selectOne(new QueryWrapper<UserEntity>().eq("id", model.getUserId()));
        imgDetailVo.setUserId(user.getId());
        imgDetailVo.setUsername(user.getUsername());
        imgDetailVo.setAvatar(user.getAvatar());
        return imgDetailVo;
    }


    @NotNull
    private StringBuilder getStringBuilder(ImgDetailsDTO imgDetailsDTO) {
        StringBuilder stringBuilder = new StringBuilder();
        for (TagEntity tag : imgDetailsDTO.getTags()) {
            long id = tagService.saveTagByName(tag.getName());
            stringBuilder.append(id);
            stringBuilder.append(";");
        }
        return stringBuilder;
    }

    @Override
    public Page<ImgDetailVo> getPage(long page, long limit) {
        List<ImgDetailsEntity> imgDetailsEntityList = baseDao.selectList(new QueryWrapper<ImgDetailsEntity>().eq("status", 1).orderByDesc("update_date"));

        List<ImgDetailVo> res = new ArrayList<>();
        ImgDetailVo imgDetailVo;
        for (ImgDetailsEntity model : imgDetailsEntityList) {
            imgDetailVo = getImgDetailVo(model);
            res.add(imgDetailVo);
        }
        return PageUtils.getPages((int) page, (int) limit, res);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImgDetailsEntity publish(ImgDetailsDTO imgDetailsDTO) {

        ImgDetailsEntity imgDetailsEntity = ConvertUtils.sourceToTarget(imgDetailsDTO, ImgDetailsEntity.class);
        imgDetailsEntity.setCollectionCount(0L);
        imgDetailsEntity.setCommentCount(0L);
        imgDetailsEntity.setAgreeCount(0L);
        imgDetailsEntity.setViewCount(0L);
        baseDao.insert(imgDetailsEntity);

        AlbumImgRelationEntity albumImgRelationEntity = new AlbumImgRelationEntity();
        albumImgRelationEntity.setAid(imgDetailsDTO.getAlbumId());
        albumImgRelationEntity.setMid(imgDetailsEntity.getId());
        albumImgRelationEntity.setSort(0);
        albumImgRelationDao.insert(albumImgRelationEntity);

        AlbumEntity albumEntity = albumDao.selectById(imgDetailsDTO.getAlbumId());
        albumEntity.setImgCount(imgDetailsDTO.getCount() + albumEntity.getImgCount());
        albumDao.updateById(albumEntity);

        TagImgRelationEntity tagImgRelationEntity = new TagImgRelationEntity();
        tagImgRelationEntity.setMid(imgDetailsEntity.getId());

        StringBuilder stringBuilder = getStringBuilder(imgDetailsDTO);

        tagImgRelationEntity.setTagIds(stringBuilder.toString());
        tagImgRelationDao.insert(tagImgRelationEntity);

        UserRecordEntity userRecord = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", imgDetailsDTO.getUserId()));

        userRecord.setTrendCount(userRecord.getTrendCount() + 1);

        userRecordDao.updateById(userRecord);

        return imgDetailsEntity;


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(ImgDetailsDTO imgDetailsDTO) {

        baseDao.updateById(ConvertUtils.sourceToTarget(imgDetailsDTO, ImgDetailsEntity.class));

        ImgDetailsEntity imgDetailsEntity = baseDao.selectById(imgDetailsDTO.getId());

        ImgDetailSearchVo imgDetailSearchVo = ConvertUtils.sourceToTarget(imgDetailsEntity, ImgDetailSearchVo.class);

        UserEntity userEntity = userDao.selectById(imgDetailsEntity.getUserId());
        imgDetailSearchVo.setUsername(userEntity.getUsername())
                .setAvatar(userEntity.getAvatar())
                .setOtherUserId(userEntity.getUserId())
                .setTime(imgDetailsEntity.getUpdateDate());

//        if (imgDetailsDTO.getType() == 0) {
//
//            redisUtils.hPut(imgDetailKey, String.valueOf(imgDetailsEntity.getId()), JSON.toJSONString(imgDetailsEntity));
//
//            try {
//                esClient.addData(imgDetailSearchVo);
//            } catch (Exception e) {
//                throw new RenException(Constant.ES_ERROR);
//            }
//        } else {
//
//            redisUtils.hDelete(imgDetailKey, String.valueOf(imgDetailsEntity.getId()));
//            redisUtils.hPut(imgDetailKey, String.valueOf(imgDetailsEntity.getId()), JSON.toJSONString(imgDetailsEntity));
//
//            try {
//                esClient.update(imgDetailSearchVo);
//            } catch (Exception e) {
//                throw new RenException(Constant.ES_ERROR);
//            }
//        }

    }

    @Override
    public List<ImgDetailVo> getAllImgByAlbum(long page, long limit, String albumId, Integer type) {

        return baseDao.getAllImgByAlbum(page, limit, albumId, type);
//（垃圾代码）
//        List<AlbumImgRelationEntity> albumImgRelation = albumImgRelationDao.selectList(new QueryWrapper<AlbumImgRelationEntity>().eq("aid", albumId));
//
//        List<String> idList = albumImgRelation.stream().map(e -> String.valueOf(e.getMid())).collect(Collectors.toList());
//
//
//        List<ImgDetailsEntity> imgDetailList = baseDao.selectList(new QueryWrapper<ImgDetailsEntity>().in("id", idList).orderByDesc("update_date"));
//
//        AlbumEntity albumEntity = albumDao.selectById(albumId);
//
//        List<ImgDetailVo> res = new ArrayList<>();
//        for (ImgDetailsEntity model : imgDetailList) {
//
//            ImgDetailVo imgDetailVo = getImgDetailVo(model);
//            imgDetailVo.setAlbumName(albumEntity.getName())
//                    .setImgCount(albumEntity.getImgCount())
//                    .setTime(DateUtils.timeUtile(model.getUpdateDate()));
//            res.add(imgDetailVo);
//        }
//
//        return PageUtils.getPages((int) page, (int) limit, res);
    }

    @Override
    public ImgDetailInfoVo getOne(String id) {

        //视频浏览记录
        String viewRecordKey = ImgDetailCacheNames.IMG_VIEW_RECORD + id;

        if (Boolean.TRUE.equals(redisUtils.hasKey(viewRecordKey))) {
            int count = Integer.parseInt(redisUtils.get(viewRecordKey));
            redisUtils.set(viewRecordKey, String.valueOf(count + 1));
        } else {
            redisUtils.set(viewRecordKey, "1");
        }

        String imgInfoKey = ImgDetailCacheNames.IMG_INFO_KEY + id;
        String agreeImgKey = ImgDetailCacheNames.AGREE_IMG_KEY + id;
        String albumImgRelationKey = ImgDetailCacheNames.ALBUM_IMG_RELATION_KEY + id;

        if (Boolean.TRUE.equals(redisUtils.hasKey(imgInfoKey))) {
            ImgDetailInfoVo imgDetailInfoVo = JSON.parseObject(redisUtils.get(imgInfoKey), ImgDetailInfoVo.class);
            int viewCount = Integer.parseInt(redisUtils.get(viewRecordKey));
            int agreeCount = 0;
            int collectCount = 0;
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeImgKey))) {
                agreeCount = JSON.parseArray(redisUtils.get(agreeImgKey), Long.class).size();
            }
            if (Boolean.TRUE.equals(redisUtils.hasKey(albumImgRelationKey))) {
                collectCount = JSON.parseArray(redisUtils.get(albumImgRelationKey), Long.class).size();
            }

            imgDetailInfoVo.setViewCount((long) viewCount);
            imgDetailInfoVo.setAgreeCount((long) agreeCount);
            imgDetailInfoVo.setCollectionCount((long) collectCount);
            return imgDetailInfoVo;
        }

        ImgDetailsEntity imgDetailsEntity = baseDao.selectById(id);

        List<String> imgList = JSON.parseArray(imgDetailsEntity.getImgsUrl(), String.class);
        UserEntity user = userDao.selectById(imgDetailsEntity.getUserId());

        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(imgDetailsEntity.getCategoryId());
        categoryIds.add(imgDetailsEntity.getCategoryPid());
        List<CategoryEntity> categoryList = categoryDao.selectBatchIds(categoryIds);
        HashMap<Integer, CategoryEntity> categoryMap = new HashMap<>();

        categoryList.forEach(item -> {
            if (item.getPid() == 0) {
                categoryMap.put(0, item);
            } else {
                categoryMap.put(1, item);
            }
        });


        ImgDetailInfoVo imgDetailInfoVo = ConvertUtils.sourceToTarget(imgDetailsEntity, ImgDetailInfoVo.class);

        imgDetailInfoVo.setImgsUrl(imgList)
                .setCategoryId(categoryMap.get(1).getId())
                .setCategoryName(categoryMap.get(1).getName())
                .setCategoryPid(categoryMap.get(0).getId())
                .setCategoryPName(categoryMap.get(0).getName())
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setAvatar(user.getAvatar())
                .setTime(DateUtils.timeUtile(imgDetailsEntity.getUpdateDate()));

        //得到当前图像的所有标签
        TagImgRelationEntity tagImgRelation = tagImgRelationDao.selectOne(new QueryWrapper<TagImgRelationEntity>().eq("mid", id));
        String strTagIds = tagImgRelation.getTagIds();

        List<String> ids = new ArrayList<>();
        if (StringUtils.isNotEmpty(strTagIds)) {
            String[] split = strTagIds.split(";");
            ids.addAll(Arrays.asList(split));
        }

        if (!ids.isEmpty()) {
            List<TagEntity> tagList = tagDao.selectBatchIds(ids);
            imgDetailInfoVo.setTagList(tagList);
        }

        //得到专辑
        List<AlbumEntity> albumList = albumDao.selectList(new QueryWrapper<AlbumEntity>().eq("uid", user.getId()));
        //图片绑定多个专辑
        List<AlbumImgRelationEntity> albumImgRelationList = albumImgRelationDao.selectList(new QueryWrapper<AlbumImgRelationEntity>().eq("mid", id));

        HashMap<Long, AlbumEntity> albumMap = new HashMap<>();

        albumList.forEach(item -> {
            albumMap.put(item.getId(), item);
        });


        for (AlbumImgRelationEntity item : albumImgRelationList) {
            if (albumMap.containsKey(item.getAid())) {
                AlbumEntity albumEntity = albumMap.get(item.getAid());
                imgDetailInfoVo.setAlbum(albumEntity);
                break;
            }
        }
        redisUtils.setEx(imgInfoKey, JSON.toJSONString(imgDetailInfoVo), 5, TimeUnit.DAYS);
        return imgDetailInfoVo;
    }

    /**
     * 项目运行前先要把所有数据导入到es中
     */
    @Override
    public void addBulkData() {
        List<ImgDetailsEntity> imgDetailsEntityList = baseDao.selectList(null);
        List<ImgDetailSearchVo> res = new ArrayList<>();
        ImgDetailSearchVo imgDetailVo;
        UserEntity user;
        List<String> imgList;

        List<Long> uids = imgDetailsEntityList.stream().map(ImgDetailsEntity::getUserId).collect(Collectors.toList());
        List<UserEntity> userList = userDao.selectBatchIds(uids);
        HashMap<Long, UserEntity> userMap = new HashMap<>();

        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        for (ImgDetailsEntity model : imgDetailsEntityList) {
            imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            user = userMap.get(model.getUserId());
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailSearchVo.class);
            imgDetailVo.setImgsUrl(imgList)
                    .setUserId(user.getId())
                    .setOtherUserId(user.getUserId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setTime(model.getUpdateDate());
            res.add(imgDetailVo);
        }
        try {
            esClient.addBulkData(res);
        } catch (Exception e) {
            throw new RenException(Constant.ES_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteImgs(String[] ids, String uid) {

        if (ids.length <= 0) {
            return;
        }
        //删除图片
        List<String> idList = Arrays.asList(ids);

        List<ImgDetailsEntity> imgDetailsEntityList = baseDao.selectBatchIds(idList);

        List<AlbumImgRelationEntity> albumImgRelationList;

        //删除图片收藏
        collectionDao.deleteBatchIdList(idList);


        //---
        for (ImgDetailsEntity model : imgDetailsEntityList) {
            //图片绑定专辑
            albumImgRelationList = albumImgRelationDao.selectList(new QueryWrapper<AlbumImgRelationEntity>().eq("mid", model.getId()));

            //所有绑定的专辑
            List<Long> albumIds = albumImgRelationList.stream().map(AlbumImgRelationEntity::getAid).collect(Collectors.toList());

            List<AlbumEntity> albumList = albumDao.selectBatchIds(albumIds);

            HashMap<Long, AlbumEntity> albumMap = new HashMap<>();

            albumList.forEach(item -> {
                albumMap.put(item.getId(), item);
            });


            if (String.valueOf(model.getUserId()).equals(uid)) {

                for (AlbumImgRelationEntity albumImgRelationModel : albumImgRelationList) {
                    AlbumEntity albumEntity = albumMap.get(albumImgRelationModel.getAid());

                    if (albumEntity.getImgCount() >= model.getCount()) {
                        albumEntity.setImgCount(albumEntity.getImgCount() - model.getCount());
                        albumDao.updateById(albumEntity);
                    }

                    albumImgRelationDao.delete(new QueryWrapper<AlbumImgRelationEntity>().and(e -> e.eq("mid", model.getId()).eq("aid", albumEntity.getId())));


                    String viewRecordKey = ImgDetailCacheNames.IMG_VIEW_RECORD + model.getId();
                    if (Boolean.TRUE.equals(redisUtils.hasKey(viewRecordKey))) {
                        redisUtils.delete(viewRecordKey);
                    }

                    //删除推荐系统中缓存的数据
                    String recommendKey = ImgDetailCacheNames.RECOMMEND + model.getId();
                    if (Boolean.TRUE.equals(redisUtils.hasKey(recommendKey))) {
                        redisUtils.delete(recommendKey);
                    }

                    //删除hmap中的图片信息
                    redisUtils.hDelete(imgDetailKey, String.valueOf(model));

                    if (model.getStatus() == 1) {
                        //删除es中的数据
                        try {
                            esClient.delData(String.valueOf(model.getId()));
                        } catch (Exception e) {
                            throw new RenException(ErrorCode.JOB_ERROR);
                        }
                    }

                    //删除oss对象存储中的图片
                    List<String> fileNames = JSON.parseArray(model.getImgsUrl(), String.class);

                    for (String item : fileNames) {
                        if (WebUtils.isHttpUrl(item)) {
                            ossClient.deleteFile(item);
                        }
                    }

                    baseDao.deleteById(model.getId());
                }
            } else {
                //找到当前图片所属专辑
                for (AlbumImgRelationEntity albumImgRelationModel : albumImgRelationList) {
                    AlbumEntity albumEntity = albumMap.get(albumImgRelationModel.getAid());
                    if (String.valueOf(albumEntity.getUid()).equals(uid)) {
                        albumImgRelationDao.delete(new QueryWrapper<AlbumImgRelationEntity>().and(e -> e.eq("mid", model.getId()).eq("aid", albumImgRelationModel.getAid())));
                    }
                }
            }
        }
    }

    /**
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Page<ImgDetailVo> getHot(long page, long limit) {

        Page<ImgDetailVo> resPage = new Page<>();

        String key = ImgDetailCacheNames.HOT;
        if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
            String objStr = redisUtils.get(key);
            List<ImgDetailVo> imgDetailVos = JSON.parseArray(objStr, ImgDetailVo.class);
            return PageUtils.getPages((int) page, (int) limit, imgDetailVos);
        }


        Page<ImgDetailsEntity> imgDetailsEntityPage = baseDao.selectPage(new Page<>(page, limit), new QueryWrapper<ImgDetailsEntity>().orderByDesc("agree_count").ge("agree_count", 1));

        List<ImgDetailVo> list = new ArrayList<>();

        List<ImgDetailsEntity> imgDetailList = imgDetailsEntityPage.getRecords();

        List<Long> uids = imgDetailList.stream().map(ImgDetailsEntity::getUserId).collect(Collectors.toList());

        List<UserEntity> userList = userDao.selectBatchIds(uids);

        HashMap<Long, UserEntity> userMap = new HashMap<>();
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });


        ImgDetailVo imgDetailVo;
        List<String> imgList;
        UserEntity userEntity;

        for (ImgDetailsEntity model : imgDetailList) {
            imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            userEntity = userMap.get(model.getUserId());
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            imgDetailVo.setImgsUrl(imgList)
                    .setUsername(userEntity.getUsername())
                    .setAvatar(userEntity.getAvatar());
            list.add(imgDetailVo);
        }
        resPage.setRecords(list);
        resPage.setTotal(imgDetailsEntityPage.getTotal());

        return resPage;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImgDetailsEntity updateImgDetail(ImgDetailsDTO imgDetailsDTO) throws MalformedModelException, ModelNotFoundException, TranslateException, IOException {

        ImgDetailsEntity imgDetailsEntity = ConvertUtils.sourceToTarget(imgDetailsDTO, ImgDetailsEntity.class);
        baseDao.updateById(imgDetailsEntity);

        //删除原有的绑定关系
        if (imgDetailsDTO.getAlbum() != null) {
            AlbumEntity albumEntity = albumDao.selectById(imgDetailsDTO.getAlbum().getId());
            albumEntity.setImgCount(albumEntity.getImgCount() - imgDetailsDTO.getCount());
            albumDao.updateById(albumEntity);
            AlbumImgRelationDTO albumImgRelationDTO = new AlbumImgRelationDTO();
            albumImgRelationDTO.setAid(imgDetailsDTO.getAlbum().getId());
            albumImgRelationDTO.setMid(imgDetailsEntity.getId());
            albumImgRelationService.deleteAlbumImgRelation(albumImgRelationDTO);
        }

        AlbumEntity albumEntity = albumDao.selectById(imgDetailsDTO.getAlbumId());
        albumEntity.setImgCount(imgDetailsDTO.getCount() + albumEntity.getImgCount());
        albumDao.updateById(albumEntity);

        //插入新的绑定关系
        AlbumImgRelationEntity albumImgRelationEntity = new AlbumImgRelationEntity();
        albumImgRelationEntity.setAid(imgDetailsDTO.getAlbumId());
        albumImgRelationEntity.setMid(imgDetailsEntity.getId());
        albumImgRelationEntity.setSort(0);
        albumImgRelationDao.insert(albumImgRelationEntity);

        //修改标签
        TagImgRelationEntity tagImgRelation = tagImgRelationDao.selectOne(new QueryWrapper<TagImgRelationEntity>().eq("mid", imgDetailsEntity.getId()));

        StringBuilder stringBuilder = getStringBuilder(imgDetailsDTO);

        if (stringBuilder.toString().length() > 0) {
            tagImgRelation.setTagIds(stringBuilder.toString());
            tagImgRelationDao.updateById(tagImgRelation);
        }

        ImgDetailSearchVo imgDetailVo = ConvertUtils.sourceToTarget(imgDetailsEntity, ImgDetailSearchVo.class);

        UserEntity userEntity = userDao.selectById(imgDetailsEntity.getUserId());
        imgDetailVo.setUsername(userEntity.getUsername());
        imgDetailVo.setAvatar(userEntity.getAvatar());
        imgDetailVo.setOtherUserId(userEntity.getUserId());

        String recommendKey = ImgDetailCacheNames.RECOMMEND + imgDetailsEntity.getId();

        if (Boolean.TRUE.equals(redisUtils.hasKey(recommendKey))) {
            redisUtils.delete(recommendKey);
        }

        return imgDetailsEntity;
    }
}