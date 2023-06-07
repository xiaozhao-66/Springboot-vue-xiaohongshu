package com.xz.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.constant.cacheConstant.ImgDetailCacheNames;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.PageUtils;
import com.xz.platform.common.constant.Constant;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.dao.*;
import com.xz.platform.dto.CollectionDTO;
import com.xz.platform.entity.*;
import com.xz.platform.service.CollectionService;
import com.xz.platform.vo.CollectionVo;
import com.xz.platform.vo.ImgDetailInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class CollectionServiceImpl extends BaseServiceImpl<CollectionDao, CollectionEntity> implements CollectionService {


    @Autowired
    AlbumDao albumDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ImgDetailsDao imgDetailsDao;

    @Autowired
    AlbumImgRelationDao albumImgRelationDao;

    @Autowired
    RedisUtils redisUtils;


    @Override
    public Map<String, String> collection(CollectionDTO collectionDTO) {

        Map<String, String> res = new HashMap<>();
        CollectionEntity collection = baseDao.selectOne(new QueryWrapper<CollectionEntity>().and(e -> e.eq("uid", collectionDTO.getUid()).eq("collection_id", collectionDTO.getCollectionId())));
        if (collection != null) {
            res.put(Constant.MESSAGE, Constant.COLLECTION_USER_SUCCESS);
            return res;
        }
        CollectionEntity collectionEntity = ConvertUtils.sourceToTarget(collectionDTO, CollectionEntity.class);
        baseDao.insert(collectionEntity);
        AlbumEntity albumEntity = albumDao.selectById(collectionDTO.getCollectionId());
        albumEntity.setCollectionCount(albumEntity.getCollectionCount() + 1);
        albumDao.updateById(albumEntity);
        res.put(Constant.MESSAGE, Constant.COLLECTION_SUCCESS);
        return res;
    }

    /**
     * 获取所有的收藏
     *
     * @param uid
     * @param type
     * @return
     */
    @Override
    public List<CollectionVo> getAllCollection(long page, long limit, String uid, Integer type) {

        return baseDao.getAllCollection(page, limit, uid,type);

        //旧方法:查询所有的收藏数据
//        List<CollectionEntity> collections = baseDao.selectList(new QueryWrapper<CollectionEntity>().and(e -> e.eq("uid", uid).eq("type", type)));
//
//        if (collections.isEmpty()) {
//            return new Page<>();
//        }
//
//        List<CollectionVo> collectionVos = new ArrayList<>();
//        if(type==1){
//            for (CollectionEntity collectionEntity : collections) {
//
//                AlbumEntity albumEntity = albumDao.selectById(collectionEntity.getCollectionId());
//                UserEntity userEntity = userDao.selectById(albumEntity.getUid());
//                CollectionVo collectionVo = ConvertUtils.sourceToTarget(albumEntity, CollectionVo.class);
//                collectionVo.setUid(userEntity.getId())
//                            .setUsername(userEntity.getUsername())
//                            .setAvatar(userEntity.getAvatar())
//                            .setContent(albumEntity.getName())
//                            .setCollectionTime(collectionEntity.getCreateDate());
//                collectionVos.add(collectionVo);
//            }
//        }else{
//            for (CollectionEntity collectionEntity : collections) {
//                ImgDetailsEntity imgDetailsEntity = imgDetailsDao.selectById(collectionEntity.getCollectionId());
//                UserEntity userEntity = userDao.selectById(imgDetailsEntity.getUserId());
//                CollectionVo collectionVo = ConvertUtils.sourceToTarget(imgDetailsEntity, CollectionVo.class);
//                collectionVo.setUid(userEntity.getId())
//                        .setUsername(userEntity.getUsername())
//                        .setAvatar(userEntity.getAvatar())
//                        .setCollectionTime(collectionEntity.getCreateDate());
//                collectionVos.add(collectionVo);
//            }
//        }
//
//        collectionVos.sort((o1, o2) -> o2.getCollectionTime().compareTo(o1.getCollectionTime()));
//
//        return PageUtils.getPages((int) page, (int) limit, collectionVos);
    }

    @Override
    public Map<String, String> cancalCollection(CollectionDTO collectionDTO) {



        Map<String, String> res = new HashMap<>();

        if (collectionDTO.getType() == 0) {

            String key = ImgDetailCacheNames.IMG_DETAIL + collectionDTO.getCollectionId();

            if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
                String strObject = redisUtils.get(key);
                ImgDetailInfoVo imgDetailInfoVo = JSON.parseObject(strObject, ImgDetailInfoVo.class);
                imgDetailInfoVo.setCollectionCount(imgDetailInfoVo.getCollectionCount() - 1);
                redisUtils.setEx(key, JSON.toJSONString(imgDetailInfoVo), 60, TimeUnit.SECONDS);
            }

            CollectionEntity collectionEntity = baseDao.selectOne(new QueryWrapper<CollectionEntity>().and(e -> e.eq("uid", collectionDTO.getUid()).eq("collection_id", collectionDTO.getCollectionId()).eq("type", collectionDTO.getType())));
            if (collectionEntity == null) {
                res.put(Constant.MESSAGE, Constant.COLLECTION_ERROR);
                return res;
            }
            List<AlbumImgRelationEntity> albumImgRelationEntityList = albumImgRelationDao.selectList(new QueryWrapper<AlbumImgRelationEntity>().eq("mid", collectionDTO.getCollectionId()));

            for (AlbumImgRelationEntity albumImgRelationEntity : albumImgRelationEntityList) {

                AlbumEntity albumEntity = albumDao.selectById(albumImgRelationEntity.getAid());

                //找到当前用户专辑下绑定的图片
                if (albumEntity.getUid().equals(collectionDTO.getUid())) {
                    albumImgRelationDao.delete(new QueryWrapper<AlbumImgRelationEntity>().and(e -> e.eq("aid", albumImgRelationEntity.getAid()).eq("mid", collectionDTO.getCollectionId())));
                }
            }

        }

        CollectionEntity collection = baseDao.selectOne(new QueryWrapper<CollectionEntity>().and(e -> e.eq("uid", collectionDTO.getUid()).eq("collection_id", collectionDTO.getCollectionId())));
        if (collection == null) {
            res.put(Constant.MESSAGE, Constant.COLLECTION_USER_FAIL);
            return res;
        }

        //取消收藏专辑
        baseDao.delete(new QueryWrapper<CollectionEntity>().and(e -> e.eq("uid", collectionDTO.getUid()).eq("collection_id", collectionDTO.getCollectionId()).eq("type", collectionDTO.getType())));
        res.put(Constant.MESSAGE, Constant.COLLECTION_CANCEL);
        return res;
    }
}