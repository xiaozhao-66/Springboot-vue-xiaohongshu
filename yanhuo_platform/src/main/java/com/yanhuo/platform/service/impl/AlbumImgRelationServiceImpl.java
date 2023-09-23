package com.yanhuo.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.constant.Constant;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.JsonUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.platform.dao.AlbumImgRelationDao;
import com.yanhuo.platform.service.AgreeCollectService;
import com.yanhuo.platform.service.AlbumImgRelationService;
import com.yanhuo.platform.service.AlbumService;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.platform.websocket.WebSocketServer;
import com.yanhuo.xo.dto.platform.AlbumImgRelationDTO;
import com.yanhuo.xo.model.AgreeCollect;
import com.yanhuo.xo.model.Album;
import com.yanhuo.xo.model.AlbumImgRelation;
import com.yanhuo.xo.model.ImgDetail;
import com.yanhuo.xo.vo.UserRecordVo;
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
public class AlbumImgRelationServiceImpl extends ServiceImpl<AlbumImgRelationDao, AlbumImgRelation> implements AlbumImgRelationService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    ImgDetailService imgDetailService;

    @Autowired
    AlbumService albumService;

    @Autowired
    AgreeCollectService agreeCollectService;

    /**
     * 判断是否收藏
     *
     * @param uid
     * @param mid
     * @return
     */
    @Override
    public boolean isCollectImgToAlbum(String uid, String mid) {

        String albumImgRelationKey = PlatformConstant.ALBUM_IMG_RELATION_KEY + mid;

        return redisUtils.sIsMember(albumImgRelationKey, uid);

    }

    /**
     * 保存图片至专辑中
     *
     * @param albumImgRelationDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAlbumImgRelation(AlbumImgRelationDTO albumImgRelationDTO) {

        String imgDetailListKey = PlatformConstant.IMG_DETAIL_LIST_KEY;

        if (redisUtils.hExists(imgDetailListKey, String.valueOf(albumImgRelationDTO.getMid()))) {
            ImgDetail imgDetail = JsonUtils.parseObject((String) redisUtils.hGet(imgDetailListKey, String.valueOf(albumImgRelationDTO.getMid())), ImgDetail.class);
            imgDetail.setCollectionCount(imgDetail.getCollectionCount() + 1);
            redisUtils.hPut(imgDetailListKey, String.valueOf(albumImgRelationDTO.getMid()), JsonUtils.toJsonString(imgDetail));
        }

        String albumImgRelationKey = PlatformConstant.ALBUM_IMG_RELATION_KEY + albumImgRelationDTO.getMid();

        boolean isMember = isCollectImgToAlbum(String.valueOf(albumImgRelationDTO.getUid()), String.valueOf(albumImgRelationDTO.getMid()));
        if (!isMember) {
            redisUtils.sAdd(albumImgRelationKey, String.valueOf(albumImgRelationDTO.getUid()));
        }

        //如果当前图片是本人发布的则无法添加至专辑中
        ImgDetail imgDetail = imgDetailService.getById(albumImgRelationDTO.getMid());
        if (imgDetail.getUserId().equals(albumImgRelationDTO.getUid())) {
            return;
        }

        AlbumImgRelation albumImgRelation = ConvertUtils.sourceToTarget(albumImgRelationDTO, AlbumImgRelation.class);
        this.save(albumImgRelation);

        //查找当前专辑
        imgDetail.setCollectionCount(imgDetail.getCollectionCount() + 1);
        imgDetailService.updateById(imgDetail);

        Album album = albumService.getById(albumImgRelationDTO.getAid());
        Long imgCount = album.getImgCount() + imgDetail.getCount();
        album.setImgCount(imgCount);
        album.setCollectionCount(album.getCollectionCount() + 1);
        albumService.updateById(album);

        AgreeCollect agreeCollect = new AgreeCollect();
        agreeCollect.setUid(albumImgRelationDTO.getUid());
        agreeCollect.setAgreeCollectId(albumImgRelationDTO.getMid());
        agreeCollect.setAgreeCollectUid(imgDetail.getUserId());
        agreeCollect.setType(2);
        agreeCollectService.save(agreeCollect);

        String userRecordKey = PlatformConstant.USER_RECORD + imgDetail.getUserId();

        if (Boolean.TRUE.equals(redisUtils.hasKey(userRecordKey)) && !albumImgRelationDTO.getUid().equals(imgDetail.getUserId())) {
            //如果当前点赞的用户是本用户不需要通知
            UserRecordVo userRecordVo = JsonUtils.parseObject(redisUtils.get(userRecordKey), UserRecordVo.class);
            userRecordVo.setAgreeCollectionCount(userRecordVo.getAgreeCollectionCount() + 1);
            redisUtils.set(userRecordKey, JSON.toJSONString(userRecordVo));
            try {
                WebSocketServer.sendMessageTo(JSON.toJSONString(userRecordVo), String.valueOf(agreeCollect.getAgreeCollectUid()));
            } catch (Exception e) {
                throw new YanHuoException(Constant.MSG_ERROR);
            }
        }
    }

    @Override
    public void deleteAlbumImgRelation(AlbumImgRelationDTO albumImgRelationDTO) {
        AlbumImgRelation albumImgRelationEntity = this.getOne(new QueryWrapper<AlbumImgRelation>().and(e -> e.eq("aid", albumImgRelationDTO.getAid()).eq("mid", albumImgRelationDTO.getMid())));
        this.removeById(albumImgRelationEntity);
    }
}