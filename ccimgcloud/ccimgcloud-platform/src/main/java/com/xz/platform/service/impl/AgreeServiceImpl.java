package com.xz.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.common.constant.cacheConstant.ImgDetailCacheNames;
import com.xz.common.exception.RenException;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.platform.common.constant.Constant;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.dao.*;
import com.xz.platform.dto.AgreeDTO;
import com.xz.platform.entity.*;
import com.xz.platform.service.AgreeService;
import com.xz.platform.vo.AgreeVo;
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
public class AgreeServiceImpl extends BaseServiceImpl<AgreeDao, AgreeEntity> implements AgreeService {

    @Autowired
    ImgDetailsDao imgDetailsDao;

    @Autowired
    CommentDao commentDao;

    @Autowired
    AgreeDao agreeDao;

    @Autowired
    UserRecordDao userRecordDao;

    @Autowired
    RedisUtils redisUtils;


    /**
     * 查看是否点赞
     *
     * @param agreeDTO
     * @return
     */
    @Override
    public boolean isAgree(AgreeDTO agreeDTO) {
        if (agreeDTO.getType() == 1) {
            String agreeImgKey = ImgDetailCacheNames.AGREE_IMG_KEY + agreeDTO.getAgreeId();
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeImgKey))) {
                List<Long> uids = JSON.parseArray(redisUtils.get(agreeImgKey), Long.class);
                return uids.contains(agreeDTO.getUid());
            }
        } else {
            String agreeCommentKey = ImgDetailCacheNames.AGREE_COMMENT_KEY + agreeDTO.getAgreeId();
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeCommentKey))) {
                List<Long> uids = JSON.parseArray(redisUtils.get(agreeCommentKey), Long.class);
                return uids.contains(agreeDTO.getUid());
            }
        }
        return false;
    }

    /**
     * 点赞评论或者是图片
     *
     * @param agreeDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void agree(AgreeDTO agreeDTO) {
        //如果点赞的是图片
        if (agreeDTO.getType() == 1) {
            String agreeImgKey = ImgDetailCacheNames.AGREE_IMG_KEY + agreeDTO.getAgreeId();
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeImgKey))) {
                //存储所有用户点赞信息
                List<Long> uids = JSON.parseArray(redisUtils.get(agreeImgKey), Long.class);
                uids.add(agreeDTO.getUid());
                redisUtils.set(agreeImgKey, JSON.toJSONString(uids));
            } else {
                List<Long> uids = new ArrayList<>();
                uids.add(agreeDTO.getUid());
                redisUtils.set(agreeImgKey, JSON.toJSONString(uids));
            }
        } else {
            String agreeCommentKey = ImgDetailCacheNames.AGREE_COMMENT_KEY + agreeDTO.getAgreeId();
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeCommentKey))) {
                List<Long> uids = JSON.parseArray(redisUtils.get(agreeCommentKey), Long.class);
                uids.add(agreeDTO.getUid());
                redisUtils.set(agreeCommentKey, JSON.toJSONString(uids));
            } else {
                List<Long> uids = new ArrayList<>();
                uids.add(agreeDTO.getUid());
                redisUtils.set(agreeCommentKey, JSON.toJSONString(uids));
            }
        }

        AgreeEntity agreeEntity = ConvertUtils.sourceToTarget(agreeDTO, AgreeEntity.class);
        baseDao.insert(agreeEntity);

        if (agreeDTO.getType() == 1) {

            ImgDetailsEntity imgEntity = imgDetailsDao.selectById(agreeDTO.getAgreeId());
            imgEntity.setAgreeCount(imgEntity.getAgreeCount() + 1);
            imgDetailsDao.updateById(imgEntity);

        } else {
            //点赞评论
            CommentEntity commentEntity = commentDao.selectById(agreeDTO.getAgreeId());
            commentEntity.setCount(commentEntity.getCount() + 1);
            commentDao.updateById(commentEntity);
        }

        //更改用户记录表 TODO 应该用redis存储
        UserRecordEntity userRecordEntity = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", agreeDTO.getAgreeUid()));
        userRecordEntity.setAgreeCollectionCount(userRecordEntity.getAgreeCollectionCount() + 1);
        userRecordDao.updateById(userRecordEntity);

        //如果当前点赞的用户是本用户不需要通知
        if (!agreeDTO.getUid().equals(agreeDTO.getAgreeUid())) {
            try {
                WebSocketServer.sendMessageTo(JSON.toJSONString(userRecordEntity), String.valueOf(agreeDTO.getAgreeUid()));
            } catch (Exception e) {
                throw new RenException(Constant.MSG_ERROR);
            }
        }

    }

    /**
     * 得到所有的赞和收藏
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @Override
    public List<AgreeVo> getAllAgreeAndCollection(long page, long limit, String uid) {

        return baseDao.getAllAgreeAndCollection(page, limit, uid);
//        旧的方法，多张表查询后整合服务（垃圾代码）
//        List<AgreeEntity> agreeList = agreeDao.selectList(new QueryWrapper<AgreeEntity>().eq("agree_uid", uid));
//
//        List<AgreeVo> agreeVoList = new ArrayList<>();
//
//        UserEntity userEntity = null;
//        CommentEntity commentEntity = null;
//        ImgDetailsEntity imgDetailsEntity = null;
//        AgreeVo agreeVo = null;
//
//        //得到所有的图片赞和评论赞
//        for (AgreeEntity model : agreeList) {
//
//            if (String.valueOf(model.getUid()).equals(uid)) {
//                continue;
//            }
//
//            agreeVo = new AgreeVo();
//            userEntity = userDao.selectById(model.getUid());
//            agreeVo.setAvatar(userEntity.getAvatar())
//                    .setUsername(userEntity.getUsername())
//                    .setUid(userEntity.getId())
//                    .setCreateDate(model.getCreateDate())
//                    .setTime(DateUtils.timeUtile(model.getCreateDate()));
//            //点赞的是图片
//            if (model.getType() == 1) {
//                imgDetailsEntity = imgDetailsDao.selectById(model.getAgreeId());
//                agreeVo.setType(1)
//                        .setCover(imgDetailsEntity.getCover())
//                        .setMid(imgDetailsEntity.getId());
//
//            } else {
//                commentEntity = commentDao.selectById(model.getAgreeId());
//                imgDetailsEntity = imgDetailsDao.selectById(commentEntity.getMid());
//
//                agreeVo.setType(0)
//                        .setCover(imgDetailsEntity.getCover())
//                        .setMid(imgDetailsEntity.getId())
//                        .setContent(commentEntity.getContent());
//            }
//
//            agreeVoList.add(agreeVo);
//        }
//
//
//        //得到当前用户发布的图片
//        List<ImgDetailsEntity> imgDetailsEntityList = imgDetailsDao.selectList(new QueryWrapper<ImgDetailsEntity>().eq("user_id", uid));
//        AlbumEntity albumEntity = null;
//
//        for (ImgDetailsEntity model : imgDetailsEntityList) {
//
//            //当前图片被哪些专辑收藏
//            List<AlbumImgRelationEntity> albumImgRelationList = albumImgRelationDao.selectList(new QueryWrapper<AlbumImgRelationEntity>().eq("mid", model.getId()));
//
//            for (AlbumImgRelationEntity albumImgRelationElement : albumImgRelationList) {
//                albumEntity = albumDao.selectById(albumImgRelationElement.getAid());
//
//                //表示被他人给收藏
//                if (!String.valueOf(albumEntity.getUid()).equals(uid)) {
//                    userEntity = userDao.selectById(albumEntity.getUid());
//                    agreeVo = new AgreeVo();
//                    agreeVo.setType(2)
//                            .setAvatar(userEntity.getAvatar())
//                            .setUsername(userEntity.getUsername())
//                            .setUid(userEntity.getId())
//                            .setCreateDate(albumImgRelationElement.getCreateDate())
//                            .setTime(DateUtils.timeUtile(albumImgRelationElement.getCreateDate()))
//                            .setCover(model.getCover())
//                            .setMid(model.getId());
//                    agreeVoList.add(agreeVo);
//                }
//            }
//
//        }
//
//        agreeVoList.sort((o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
//
//        return PageUtils.getPages((int) page, (int) limit, agreeVoList);
    }

    /**
     * 取消点赞
     *
     * @param agreeDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAgree(AgreeDTO agreeDTO) {

        //如果点赞的是图片
        if (agreeDTO.getType() == 1) {
            String agreeImgKey = ImgDetailCacheNames.AGREE_IMG_KEY + agreeDTO.getAgreeId();
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeImgKey))) {
                //存储所有用户点赞信息
                List<Long> uids = JSON.parseArray(redisUtils.get(agreeImgKey), Long.class);
                uids.remove(agreeDTO.getUid());
                redisUtils.set(agreeImgKey, JSON.toJSONString(uids));
            }
        } else {
            String agreeCommentKey = ImgDetailCacheNames.AGREE_COMMENT_KEY + agreeDTO.getAgreeId();
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeCommentKey))) {
                List<Long> uids = JSON.parseArray(redisUtils.get(agreeCommentKey), Long.class);
                uids.remove(agreeDTO.getUid());
                redisUtils.set(agreeCommentKey, JSON.toJSONString(uids));
            }
        }

        agreeDao.delete(new QueryWrapper<AgreeEntity>().and(e -> e.eq("uid", agreeDTO.getUid()).eq("agree_id", agreeDTO.getAgreeId()).eq("agree_uid", agreeDTO.getAgreeUid())));

        if (agreeDTO.getType() == 1) {
            ImgDetailsEntity imgEntity = imgDetailsDao.selectById(agreeDTO.getAgreeId());
            imgEntity.setAgreeCount(imgEntity.getAgreeCount() - 1);
            imgDetailsDao.updateById(imgEntity);
        } else {
            //点赞评论
            CommentEntity commentEntity = commentDao.selectById(agreeDTO.getAgreeId());
            commentEntity.setCount(commentEntity.getCount() - 1);
            commentDao.updateById(commentEntity);
        }

        //更改用户记录表
        UserRecordEntity userRecordEntity = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", agreeDTO.getAgreeUid()));
        userRecordEntity.setAgreeCollectionCount(userRecordEntity.getAgreeCollectionCount() - 1);
        userRecordDao.updateById(userRecordEntity);
    }
}