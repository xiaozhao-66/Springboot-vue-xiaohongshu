package com.xz.platform.service.impl;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.constant.cacheConstant.AlbumCacheNames;
import com.xz.common.constant.cacheConstant.ImgDetailCacheNames;
import com.xz.common.exception.ErrorCode;
import com.xz.common.exception.RenException;
import com.xz.common.recommend.RecommendUtils2;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.DateUtils;
import com.xz.common.utils.PageUtils;
import com.xz.platform.common.client.EsClient;
import com.xz.platform.common.constant.Constant;
import com.xz.common.utils.RedisUtils;
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
    EsClient esClient;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CollectionDao collectionDao;

    @Autowired
    AlbumImgRelationService albumImgRelationService;

    @Autowired
    RedisUtils redisUtils;


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
        List<ImgDetailsEntity> imgDetailsEntityList = baseDao.selectList(new QueryWrapper<ImgDetailsEntity>().orderByDesc("update_date"));

        List<ImgDetailVo> res = new ArrayList<>();
        ImgDetailVo imgDetailVo = null;
        for (ImgDetailsEntity model : imgDetailsEntityList) {
            imgDetailVo = getImgDetailVo(model);
            res.add(imgDetailVo);
        }
        return PageUtils.getPages((int) page, (int) limit, res);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(ImgDetailsDTO imgDetailsDTO) throws MalformedModelException, ModelNotFoundException, TranslateException, IOException {

        String followKey = ImgDetailCacheNames.FOLLOW_TREND+imgDetailsDTO.getUserId();
        Set<String> listKey = redisUtils.getListKey(followKey);
        if(!listKey.isEmpty()){
            for(String e : listKey){
                redisUtils.delete(e);
            }
        }

        String userTrendKey = ImgDetailCacheNames.USER_TREND+imgDetailsDTO.getUserId();
        if(redisUtils.hasKey(userTrendKey)){
            redisUtils.delete(userTrendKey);
        }

        ImgDetailsEntity imgDetailsEntity = ConvertUtils.sourceToTarget(imgDetailsDTO, ImgDetailsEntity.class);
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


        ImgDetailSearchVo imgDetailVo = ConvertUtils.sourceToTarget(imgDetailsEntity, ImgDetailSearchVo.class);

        UserEntity userEntity = userDao.selectById(imgDetailsEntity.getUserId());
        imgDetailVo.setUsername(userEntity.getUsername())
                .setAvatar(userEntity.getAvatar())
                .setAgreeCount(0L)
                .setOtherUserId(userEntity.getUserId())
                .setTime(imgDetailsEntity.getUpdateDate());

        try {
            esClient.addData(imgDetailVo);
        } catch (Exception e) {
            throw new RenException(Constant.ES_ERROR);
        }


    }

    @Override
    public List<ImgDetailVo> getAllImgByAlbum(long page, long limit, String albumId) {


        return baseDao.getAllImgByAlbum(page, limit, albumId);

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

        //首先先查看缓存中是否有数据
        String key = ImgDetailCacheNames.IMG_DETAIL+ id;

        if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
            String strObject = redisUtils.get(key);
            ImgDetailInfoVo imgDetailInfoVo = JSON.parseObject(strObject, ImgDetailInfoVo.class);
            //imgDetailInfoVo.setViewCount(imgDetailInfoVo.getViewCount() + 1);
            redisUtils.setEx(key, JSON.toJSONString(imgDetailInfoVo), 60, TimeUnit.SECONDS);
            return imgDetailInfoVo;
        } else {

            ImgDetailsEntity imgDetailsEntity = baseDao.selectById(id);
            CategoryEntity categoryEntity = categoryDao.selectById(imgDetailsEntity.getCategoryId());
            CategoryEntity categoryParentEntity = categoryDao.selectById(imgDetailsEntity.getCategoryPid());
            List<String> imgList = JSON.parseArray(imgDetailsEntity.getImgsUrl(), String.class);
            UserEntity user = userDao.selectById(imgDetailsEntity.getUserId());

            ImgDetailInfoVo imgDetailInfoVo = ConvertUtils.sourceToTarget(imgDetailsEntity, ImgDetailInfoVo.class);


            imgDetailInfoVo.setCategoryName(categoryEntity.getName())
                    .setCategoryPName(categoryParentEntity.getName())
                    .setImgsUrl(imgList)
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
            List<AlbumImgRelationEntity> albumImgRelationList = null;
            //修改
            for (AlbumEntity model : albumList) {
                albumImgRelationList = albumImgRelationDao.selectList(new QueryWrapper<AlbumImgRelationEntity>().eq("aid", model.getId()));
                for (AlbumImgRelationEntity e : albumImgRelationList) {
                    if (StringUtils.equals(String.valueOf(e.getMid()), id)) {
                        imgDetailInfoVo.setAlbum(model);
                        break;
                    }
                }
            }

            //设置key
            redisUtils.setEx(key, JSON.toJSONString(imgDetailInfoVo), 60, TimeUnit.SECONDS);
            return imgDetailInfoVo;
        }

    }

    @Override
    public void addBulkData() {
        List<ImgDetailsEntity> imgDetailsEntityList = baseDao.selectList(null);
        List<ImgDetailSearchVo> res = new ArrayList<>();
        ImgDetailSearchVo imgDetailVo = null;
        UserEntity user = null;
        List<String> imgList = null;
        for (ImgDetailsEntity model : imgDetailsEntityList) {
            imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            user = userDao.selectOne(new QueryWrapper<UserEntity>().eq("id", model.getUserId()));

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
        List<AlbumImgRelationEntity> albumImgRelationList = null;

        //删除图片收藏
        collectionDao.deleteBatchIdList(idList);
        //---
        for (ImgDetailsEntity model : imgDetailsEntityList) {
            albumImgRelationList = albumImgRelationDao.selectList(new QueryWrapper<AlbumImgRelationEntity>().eq("mid", model.getId()));
            if (String.valueOf(model.getUserId()).equals(uid)) {
                for (AlbumImgRelationEntity albumImgRelationModel : albumImgRelationList) {
                    AlbumEntity albumEntity = albumDao.selectById(albumImgRelationModel.getAid());
                    if (albumEntity.getImgCount() >= model.getCount()) {
                        albumEntity.setImgCount(albumEntity.getImgCount() - model.getCount());
                        albumDao.updateById(albumEntity);
                    }
                    albumImgRelationDao.delete(new QueryWrapper<AlbumImgRelationEntity>().and(e -> e.eq("mid", model.getId()).eq("aid", albumEntity.getId())));
                    //删除缓存中的数据
                    String key = ImgDetailCacheNames.IMG_DETAIL + model.getId();
                    if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
                        redisUtils.delete(key);
                    }

                    //删除关注用户中的数据
                    String followKey = ImgDetailCacheNames.FOLLOW_TREND+model.getUserId();
                    Set<String> listKey = redisUtils.getListKey(followKey);
                    if(!listKey.isEmpty()){
                        for(String e : listKey){
                            redisUtils.delete(e);
                        }
                    }

                    //删除用户动态中的数据
                    String userTrendKey = ImgDetailCacheNames.USER_TREND+model.getUserId();
                    if(Boolean.TRUE.equals(redisUtils.hasKey(userTrendKey))){
                        redisUtils.delete(userTrendKey);
                    }


                    //删除推荐系统中缓存的数据
                    String recommendKey = ImgDetailCacheNames.RECOMMEND+model.getId();
                    if(Boolean.TRUE.equals(redisUtils.hasKey(recommendKey))){
                        redisUtils.delete(recommendKey);
                    }

                    //删除es种的数据
                    try {
                        esClient.delData(String.valueOf(model.getId()));
                    } catch (Exception e) {
                        throw new RenException(ErrorCode.JOB_ERROR);
                    }
                    baseDao.deleteById(model.getId());
                }
            } else {
                //找到当前图片所属专辑
                for (AlbumImgRelationEntity albumImgRelationModel : albumImgRelationList) {
                    AlbumEntity albumEntity = albumDao.selectById(albumImgRelationModel.getAid());
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

        String key = ImgDetailCacheNames.HOT;
        if(Boolean.TRUE.equals(redisUtils.hasKey(key))) {
            String objStr = redisUtils.get(key);
            List<ImgDetailVo> imgDetailVos = JSON.parseArray(objStr, ImgDetailVo.class);
            return PageUtils.getPages((int) page, (int) limit, imgDetailVos);
        }

        List<ImgDetailsEntity> imgDetailsEntityList = baseDao.selectList(new QueryWrapper<ImgDetailsEntity>().orderByDesc("agree_count").ge("agree_count", 1));
        List<ImgDetailVo> list = new ArrayList<>();
        ImgDetailVo imgDetailVo = null;
        List<String> imgList = null;
        UserEntity userEntity = null;
        for (ImgDetailsEntity model : imgDetailsEntityList) {
            imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            userEntity = userDao.selectById(model.getUserId());
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            imgDetailVo.setImgsUrl(imgList)
                    .setUsername(userEntity.getUsername())
                    .setAvatar(userEntity.getAvatar());
            list.add(imgDetailVo);
        }
        return PageUtils.getPages((int) page, (int) limit, list);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateImgDetail(ImgDetailsDTO imgDetailsDTO) throws MalformedModelException, ModelNotFoundException, TranslateException, IOException {
        String key = ImgDetailCacheNames.IMG_DETAIL+ imgDetailsDTO.getId();

        if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
            redisUtils.delete(key);
        }

        String followKey = ImgDetailCacheNames.FOLLOW_TREND+imgDetailsDTO.getUserId();
        Set<String> listKey = redisUtils.getListKey(followKey);
        if(!listKey.isEmpty()){
            for(String e : listKey){
                redisUtils.delete(e);
            }
        }

        String userTrendKey = ImgDetailCacheNames.USER_TREND+imgDetailsDTO.getUserId();
        if(redisUtils.hasKey(userTrendKey)){
            redisUtils.delete(userTrendKey);
        }


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

        tagImgRelation.setTagIds(stringBuilder.toString());
        tagImgRelationDao.updateById(tagImgRelation);


        ImgDetailSearchVo imgDetailVo = ConvertUtils.sourceToTarget(imgDetailsEntity, ImgDetailSearchVo.class);

        UserEntity userEntity = userDao.selectById(imgDetailsEntity.getUserId());
        imgDetailVo.setUsername(userEntity.getUsername());
        imgDetailVo.setAvatar(userEntity.getAvatar());
        imgDetailVo.setOtherUserId(userEntity.getUserId());

        String recommendKey = ImgDetailCacheNames.RECOMMEND+imgDetailsEntity.getId();

        if(Boolean.TRUE.equals(redisUtils.hasKey(recommendKey))){
            redisUtils.delete(recommendKey);
        }



        try {
            esClient.update(imgDetailVo);
        } catch (Exception e) {
            throw new RenException(Constant.ES_ERROR);
        }

    }

//    @Override
//    public List<ImgDetailSearchVo> search(long page, long limit, String keyword) {
//        try {
//            return esClient.esSearch(page, limit, keyword);
//        } catch (Exception e) {
//            throw new RenException(Constant.ES_ERROR);
//        }
//    }
}