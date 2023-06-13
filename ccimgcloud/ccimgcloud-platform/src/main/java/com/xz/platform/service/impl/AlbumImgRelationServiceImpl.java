package com.xz.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.common.exception.RenException;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.constant.cacheConstant.ImgDetailCacheNames;
import com.xz.platform.common.constant.Constant;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.dao.*;
import com.xz.platform.dto.AlbumImgRelationDTO;
import com.xz.platform.entity.*;
import com.xz.platform.service.AlbumImgRelationService;
import com.xz.platform.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class AlbumImgRelationServiceImpl extends BaseServiceImpl<AlbumImgRelationDao, AlbumImgRelationEntity> implements AlbumImgRelationService {

    @Autowired
    AlbumDao albumDao;

    @Autowired
    ImgDetailsDao imgDetailsDao;

    @Autowired
    UserRecordDao userRecordDao;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    CollectionDao collectionDao;


    /**
     * 判断是否收藏
     * @param uid
     * @param mid
     * @return
     */
    @Override
    public boolean isCollectImgToAlbum(String uid, String mid) {

        String albumImgRelationKey = ImgDetailCacheNames.ALBUM_IMG_RELATION_KEY+mid;

        if(Boolean.TRUE.equals(redisUtils.hasKey(albumImgRelationKey))){
            //存储所有用户收藏信息
            List<Long> uids = JSON.parseArray(redisUtils.get(albumImgRelationKey), Long.class);
            return uids.contains(Long.valueOf(uid));
        }
        return false;

    }

    /**
     * 保存图片至专辑中
     * @param albumImgRelationDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAlbumImgRelation(AlbumImgRelationDTO albumImgRelationDTO) {

        String albumImgRelationKey = ImgDetailCacheNames.ALBUM_IMG_RELATION_KEY+albumImgRelationDTO.getMid();
        if(Boolean.TRUE.equals(redisUtils.hasKey(albumImgRelationKey))){
            //存储所有用户点赞信息
            List<Long> uids = JSON.parseArray(redisUtils.get(albumImgRelationKey), Long.class);
            uids.add(albumImgRelationDTO.getUid());
            redisUtils.set(albumImgRelationKey,JSON.toJSONString(uids));
        }else{
            List<Long> uids = new ArrayList<>();
            uids.add(albumImgRelationDTO.getUid());
            redisUtils.set(albumImgRelationKey,JSON.toJSONString(uids));
        }

        //如果当前图片是本人发布的则无法添加至专辑中
        ImgDetailsEntity imgDetail = imgDetailsDao.selectOne(new QueryWrapper<ImgDetailsEntity>().eq("id", albumImgRelationDTO.getMid()).select("user_id"));
        if(imgDetail.getUserId().equals(albumImgRelationDTO.getUid())){
            return;
        }

        AlbumImgRelationEntity albumImgRelationEntity = ConvertUtils.sourceToTarget(albumImgRelationDTO, AlbumImgRelationEntity.class);
        baseDao.insert(albumImgRelationEntity);

        //查找当前专辑
        ImgDetailsEntity imgDetailsEntity = imgDetailsDao.selectById(albumImgRelationDTO.getMid());
        imgDetailsEntity.setCollectionCount(imgDetailsEntity.getCollectionCount() + 1);
        imgDetailsDao.updateById(imgDetailsEntity);

        AlbumEntity albumEntity = albumDao.selectById(albumImgRelationDTO.getAid());
        Long imgCount = albumEntity.getImgCount() + imgDetailsEntity.getCount();
        albumEntity.setImgCount(imgCount);
        albumEntity.setCollectionCount(albumEntity.getCollectionCount() + 1);
        albumDao.updateById(albumEntity);


        //收藏至收藏表中
        CollectionEntity collectionEntity = new CollectionEntity();
        collectionEntity.setUid(albumImgRelationDTO.getUid());
        collectionEntity.setCollectionId(albumImgRelationDTO.getMid());
        collectionEntity.setType(0);
        collectionDao.insert(collectionEntity);

        //通知专辑用户保存
        UserRecordEntity userRecordEntity = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", imgDetailsEntity.getUserId()));
        userRecordEntity.setAgreeCollectionCount(userRecordEntity.getAgreeCollectionCount() + 1);
        userRecordDao.updateById(userRecordEntity);

        if (!albumImgRelationDTO.getUid().equals(imgDetailsEntity.getUserId())) {
            try {
                WebSocketServer.sendMessageTo(JSON.toJSONString(userRecordEntity), String.valueOf(userRecordEntity.getUid()));
            } catch (Exception e) {
                throw new RenException(Constant.MSG_ERROR);
            }
        }
    }

    @Override
    public void deleteAlbumImgRelation(AlbumImgRelationDTO albumImgRelationDTO) {
        AlbumImgRelationEntity albumImgRelationEntity = baseDao.selectOne(new QueryWrapper<AlbumImgRelationEntity>().and(e -> e.eq("aid", albumImgRelationDTO.getAid()).eq("mid", albumImgRelationDTO.getMid())));
        baseDao.deleteById(albumImgRelationEntity);
    }
}