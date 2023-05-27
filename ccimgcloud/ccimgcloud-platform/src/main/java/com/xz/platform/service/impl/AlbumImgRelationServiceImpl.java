package com.xz.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.common.exception.RenException;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.constant.cacheConstant.ImgDetailCacheNames;
import com.xz.platform.common.constant.Constant;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.dao.*;
import com.xz.platform.dto.AlbumImgRelationDTO;
import com.xz.platform.entity.*;
import com.xz.platform.service.AlbumImgRelationService;
import com.xz.platform.vo.ImgDetailInfoVo;
import com.xz.platform.websocket.WebSocketServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class AlbumImgRelationServiceImpl extends CrudServiceImpl<AlbumImgRelationDao, AlbumImgRelationEntity, AlbumImgRelationDTO> implements AlbumImgRelationService {

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


    @Override
    public QueryWrapper<AlbumImgRelationEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<AlbumImgRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public boolean isCollectImgToAlbum(String uid, String mid) {

        CollectionEntity collectionEntity = collectionDao.selectOne(new QueryWrapper<CollectionEntity>().and(e -> e.eq("uid", uid).eq("collection_id", mid)));
        return collectionEntity != null;
    }

    /**
     * 保存图片至专辑中
     *
     * @param albumImgRelationDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAlbumImgRelation(AlbumImgRelationDTO albumImgRelationDTO) {

        String key = ImgDetailCacheNames.IMG_DETAIL + albumImgRelationDTO.getMid();

        if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
            String strObject = redisUtils.get(key);
            ImgDetailInfoVo imgDetailInfoVo = JSON.parseObject(strObject, ImgDetailInfoVo.class);
            imgDetailInfoVo.setCollectionCount(imgDetailInfoVo.getCollectionCount() + 1);
            redisUtils.setEx(key, JSON.toJSONString(imgDetailInfoVo), 60, TimeUnit.SECONDS);

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