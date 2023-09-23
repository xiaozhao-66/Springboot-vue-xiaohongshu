package com.yanhuo.platform.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.platform.dao.AlbumDao;
import com.yanhuo.platform.service.*;
import com.yanhuo.xo.dto.platform.AlbumDTO;
import com.yanhuo.xo.model.Album;
import com.yanhuo.xo.model.AlbumImgRelation;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.vo.AlbumVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumDao, Album> implements AlbumService {

    @Autowired
    UserService userService;

    @Autowired
    AlbumImgRelationService albumImgRelationService;

    @Autowired
    ImgDetailService imgDetailService;

    @Autowired
    AgreeCollectService agreeCollectService;

    @Autowired
    RedisUtils redisUtils;

    @Override
    public List<AlbumVo> getAllAlbum(String uid) {
        List<Album> albumList = this.list(new QueryWrapper<Album>().eq("uid", uid).orderByDesc("update_date"));
        return ConvertUtils.sourceToTarget(albumList, AlbumVo.class);
    }

    @Override
    public AlbumVo getAlbum(String id) {
        Album album = this.getById(id);
        AlbumVo albumVo = ConvertUtils.sourceToTarget(album, AlbumVo.class);
        User user = userService.getById(album.getUid());
        albumVo.setUsername(user.getUsername());
        albumVo.setAvatar(user.getAvatar());
        return albumVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlbum(String id, String uid) {
        //得到当前专辑下的所有图片
        List<AlbumImgRelation> albumImgRelationEntityList = albumImgRelationService.list(new QueryWrapper<AlbumImgRelation>().eq("aid", id));
        List<String> idList = albumImgRelationEntityList.stream().map(e -> String.valueOf(e.getMid())).collect(Collectors.toList());

        String albumStateKey = PlatformConstant.ALBUM_STATE + id;
        redisUtils.delete(albumStateKey);

        imgDetailService.deleteImgs(idList, uid);
        this.removeById(id);
    }

    @Override
    public void saveAlbum(AlbumDTO albumDTO) {
        Album albumEntity = ConvertUtils.sourceToTarget(albumDTO, Album.class);
        this.save(albumEntity);
    }

    @Override
    public void updateAlbum(AlbumDTO albumDTO) {
        Album albumEntity = ConvertUtils.sourceToTarget(albumDTO, Album.class);
        this.updateById(albumEntity);
    }
}