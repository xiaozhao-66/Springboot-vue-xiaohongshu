package com.xz.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.constant.cacheConstant.AlbumCacheNames;
import com.xz.platform.dao.AlbumDao;
import com.xz.platform.dao.AlbumImgRelationDao;
import com.xz.platform.dao.CollectionDao;
import com.xz.platform.dao.UserDao;
import com.xz.platform.dto.AlbumDTO;
import com.xz.platform.dto.CollectionDTO;
import com.xz.platform.entity.AlbumEntity;
import com.xz.platform.entity.AlbumImgRelationEntity;
import com.xz.platform.entity.CollectionEntity;
import com.xz.platform.entity.UserEntity;
import com.xz.platform.service.AlbumService;
import com.xz.platform.service.ImgDetailsService;
import com.xz.platform.vo.AlbumVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class AlbumServiceImpl extends BaseServiceImpl<AlbumDao, AlbumEntity> implements AlbumService {

    @Autowired
    UserDao userDao;

    @Autowired
    ImgDetailsService imgDetailsService;

    @Autowired
    AlbumImgRelationDao albumImgRelationDao;

    @Autowired
    CollectionDao collectionDao;

    /**
     * TODO 使用缓存机制
     *
     * @param uid
     * @return
     */
    @Override
    public List<AlbumVo> getAllAlbum(String uid) {
        List<AlbumEntity> albumList = baseDao.selectList(new QueryWrapper<AlbumEntity>().eq("uid", uid).orderByDesc("update_date"));
        return ConvertUtils.sourceToTarget(albumList, AlbumVo.class);
    }

    @Override
    public AlbumVo getAlbum(String id) {
        AlbumEntity albumEntity = baseDao.selectById(id);
        AlbumVo albumVo = ConvertUtils.sourceToTarget(albumEntity, AlbumVo.class);
        UserEntity userEntity = userDao.selectById(albumEntity.getUid());
        albumVo.setUsername(userEntity.getUsername());
        albumVo.setAvatar(userEntity.getAvatar());
        return albumVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlbum(String id, String uid) {
        //得到当前专辑下的所有图片
        List<AlbumImgRelationEntity> albumImgRelationEntityList = albumImgRelationDao.selectList(new QueryWrapper<AlbumImgRelationEntity>().eq("aid", id));
        String[] idArr = albumImgRelationEntityList.stream().map(e -> String.valueOf(e.getMid())).toArray(String[]::new);
        imgDetailsService.deleteImgs(idArr, uid);

        //得到所有要删除的专辑和图片
        List<String> idList = Arrays.asList(idArr);
        if (!idList.isEmpty()) {
            collectionDao.deleteBatchIdList(idList);
        }
        collectionDao.delete(new QueryWrapper<CollectionEntity>().eq("collection_id", id));

        baseDao.deleteById(id);
    }


    @Override
    public void saveAlbum(AlbumDTO albumDTO) {
        AlbumEntity albumEntity = ConvertUtils.sourceToTarget(albumDTO, AlbumEntity.class);
        baseDao.insert(albumEntity);
    }

    @Override
    public void updateAlbum(AlbumDTO albumDTO) {
        AlbumEntity albumEntity = ConvertUtils.sourceToTarget(albumDTO, AlbumEntity.class);
        baseDao.updateById(albumEntity);
    }
}