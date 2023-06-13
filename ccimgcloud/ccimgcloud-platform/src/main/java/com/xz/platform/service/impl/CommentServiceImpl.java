package com.xz.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.constant.cacheConstant.ImgDetailCacheNames;
import com.xz.common.exception.RenException;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.DateUtils;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.common.constant.Constant;
import com.xz.platform.dao.*;
import com.xz.platform.dto.CommentDTO;
import com.xz.platform.entity.AgreeEntity;
import com.xz.platform.entity.CommentEntity;
import com.xz.platform.entity.ImgDetailsEntity;
import com.xz.platform.entity.UserRecordEntity;
import com.xz.platform.service.CommentService;
import com.xz.platform.vo.CommentVo;
import com.xz.platform.vo.ImgDetailInfoVo;
import com.xz.platform.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class CommentServiceImpl extends BaseServiceImpl<CommentDao, CommentEntity> implements CommentService {


    @Autowired
    ImgDetailsDao imgDetailsDao;

    @Autowired
    AgreeDao agreeDao;

    @Autowired
    UserRecordDao userRecordDao;

    @Autowired
    RedisUtils redisUtils;

    @Override
    public IPage<CommentVo> getAllOneCommentByImgId(long page, long limit, String mid, String uid) {

        QueryWrapper<CommentEntity> rootQueryWrapper = new QueryWrapper<CommentEntity>().and(e -> e.eq("pid", "0").eq("mid", mid)).orderByDesc("count").orderByDesc("create_date");

        Page<CommentVo> resPage = new Page<>();
        Page<CommentEntity> commentOnePage = baseDao.selectPage(new Page<>(page, limit), rootQueryWrapper);

        if (commentOnePage.getTotal() == 0) {
            return resPage;
        }

        List<AgreeEntity> agreeList = agreeDao.selectList(new QueryWrapper<AgreeEntity>().and(e -> e.eq("uid", uid).eq("type", 0)));
        List<Long> agreeIds = agreeList.stream().map(AgreeEntity::getAgreeId).collect(Collectors.toList());
        List<CommentVo> commentVoList = new ArrayList<>();
        CommentVo commentVo;

        for (CommentEntity model : commentOnePage.getRecords()) {
            commentVo = ConvertUtils.sourceToTarget(model, CommentVo.class);

            commentVo.setTime(DateUtils.timeUtile(model.getCreateDate()));

            //判断当前评论是否点赞
            commentVo.setIsAgree(agreeIds.contains(model.getId()));

            commentVoList.add(commentVo);
        }
        resPage.setRecords(commentVoList);
        resPage.setTotal(commentOnePage.getTotal());

        return resPage;
    }

    @Override
    public IPage<CommentVo> getAllTwoCommentByOneId(long page, long limit, String id, String uid) {

        QueryWrapper<CommentEntity> twoQueryWrapper = new QueryWrapper<CommentEntity>().eq("pid", id).orderByDesc("create_date");

        Page<CommentVo> resPage = new Page<>();

        Page<CommentEntity> commentTwoPage = baseDao.selectPage(new Page<>(page, limit), twoQueryWrapper);
        List<CommentVo> commentVoList = new ArrayList<>();

        List<AgreeEntity> agreeList = agreeDao.selectList(new QueryWrapper<AgreeEntity>().and(e -> e.eq("uid", uid).eq("type", 0)));
        List<Long> agreeIds = agreeList.stream().map(AgreeEntity::getAgreeId).collect(Collectors.toList());

        CommentVo commentVo;
        for (CommentEntity model : commentTwoPage.getRecords()) {
            commentVo = ConvertUtils.sourceToTarget(model, CommentVo.class);
            commentVo.setTime(DateUtils.timeUtile(model.getCreateDate()));

            //判断当前评论是否点赞
            commentVo.setIsAgree(agreeIds.contains(model.getId()));

            commentVoList.add(commentVo);
        }
        resPage.setRecords(commentVoList);
        resPage.setTotal(commentTwoPage.getTotal());
        return resPage;
    }

    @Override
    public List<CommentVo> getAllTwoComment(String id, String uid) {
        QueryWrapper<CommentEntity> twoQueryWrapper = new QueryWrapper<CommentEntity>().eq("pid", id).orderByDesc("create_date");
        List<CommentEntity> commentTwoList = baseDao.selectList(twoQueryWrapper);
        List<CommentVo> commentVoList = new ArrayList<>();

        List<AgreeEntity> agreeList = agreeDao.selectList(new QueryWrapper<AgreeEntity>().and(e -> e.eq("uid", uid).eq("type", 0)));
        List<Long> agreeIds = agreeList.stream().map(AgreeEntity::getAgreeId).collect(Collectors.toList());

        CommentVo commentVo;
        for (CommentEntity model : commentTwoList) {
            commentVo = ConvertUtils.sourceToTarget(model, CommentVo.class);
            commentVo.setTime(DateUtils.timeUtile(model.getCreateDate()));

            //判断当前评论是否点赞
            commentVo.setIsAgree(agreeIds.contains(model.getId()));

            commentVoList.add(commentVo);
        }
        return commentVoList;
    }


    /**
     * 得到用户所有的评论信息
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @Override
    public List<CommentVo> getAllReplyComment(long page, long limit, String uid) {

        return baseDao.getAllReplyComment(page, limit, uid);

//        HashMap<Long,CommentVo> map = new HashMap<>();
//        List<CommentVo> replyCommentList = new ArrayList<>();
//        //得到当前用户发布的所有作品
//        List<ImgDetailsEntity> imgDetailList = imgDetailsDao.selectList(new QueryWrapper<ImgDetailsEntity>().eq("user_id", uid));
//
//        for (ImgDetailsEntity model : imgDetailList) {
//            //得到当前笔记的所有一级评论
//            List<CommentEntity> commentList = baseDao.selectList(new QueryWrapper<CommentEntity>().and(e -> e.eq("mid", model.getId()).eq("pid", 0)));
//
//            for (CommentEntity e : commentList) {
//                if (StringUtils.equals(String.valueOf(e.getUid()), uid)) {
//                    continue;
//                }
//
//                CommentVo commentVo = ConvertUtils.sourceToTarget(e, CommentVo.class);
//                commentVo.setCover(model.getCover())
//                        .setReplyContent(e.getContent())
//                        .setCreateDate(e.getCreateDate())
//                        .setTime(DateUtils.timeUtile(e.getCreateDate()));
//
//                map.put(commentVo.getId(),commentVo);
//            }
//
//            //得到当前笔记的所有二级评论
//            List<CommentEntity> twoCommentList = baseDao.selectList(new QueryWrapper<CommentEntity>().and(e -> e.eq("mid", model.getId()).ne("pid", 0)));
//
//            for (CommentEntity comment : twoCommentList) {
//
//                //如果二级评论是当前笔记的作者评论
//                if (StringUtils.equals(String.valueOf(comment.getUid()), uid)) {
//                    continue;
//                }
//                //得到原评论
//                CommentEntity sourceComment = baseDao.selectById(comment.getReplyId());
//
//                CommentVo commentVo = ConvertUtils.sourceToTarget(comment, CommentVo.class);
//                commentVo.setCover(model.getCover())
//                        .setReplyUid(sourceComment.getUid())
//                        .setReplyContent(comment.getContent())
//                        .setContent(sourceComment.getContent())
//                        .setTime(DateUtils.timeUtile(comment.getCreateDate()));
//
//                map.put(commentVo.getId(),commentVo);
//            }
//
//        }
//
//        //得到当前用户的所有评论(有一级评论和二级评论)
//        List<CommentEntity> commentList = baseDao.selectList(new QueryWrapper<CommentEntity>().eq("uid", uid));
//
//        for (CommentEntity model : commentList) {
//            //得到所有的回复评论
//            List<CommentEntity> commentList2 = baseDao.selectList(new QueryWrapper<CommentEntity>().eq("reply_id", model.getId()));
//            for (CommentEntity e : commentList2) {
//
//                if (StringUtils.equals(String.valueOf(e.getUid()), uid)||map.containsKey(e.getId())) {
//                    continue;
//                }
//
//                CommentVo commentVo = ConvertUtils.sourceToTarget(e, CommentVo.class);
//                commentVo.setCover(imgDetailsDao.selectById(model.getMid()).getCover())
//                        .setReplyUid(model.getUid())
//                        .setReplyContent(e.getContent())
//                        .setContent(model.getContent())
//                        .setTime(DateUtils.timeUtile(e.getCreateDate()));
//                map.put(commentVo.getId(),commentVo);
//            }
//
//        }
//
//        for (Long k:map.keySet()) {
//            replyCommentList.add(map.get(k));
//        }
//
//        replyCommentList.sort((o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
//
//
//        return PageUtils.getPages((int) page, (int) limit, replyCommentList);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommentVo addComment(CommentDTO commentDTO) {

        String imgInfoKey = ImgDetailCacheNames.IMG_INFO_KEY + commentDTO.getMid();
        if (Boolean.TRUE.equals(redisUtils.hasKey(imgInfoKey))) {
            ImgDetailInfoVo imgDetailInfoVo = JSON.parseObject(redisUtils.get(imgInfoKey), ImgDetailInfoVo.class);
            imgDetailInfoVo.setCommentCount(imgDetailInfoVo.getCommentCount()+1);
            redisUtils.setEx(imgInfoKey, JSON.toJSONString(imgDetailInfoVo), 5, TimeUnit.DAYS);
        }

        CommentEntity commentEntity = ConvertUtils.sourceToTarget(commentDTO, CommentEntity.class);
        baseDao.insert(commentEntity);


        ImgDetailsEntity imgDetailsEntity = imgDetailsDao.selectById(commentDTO.getMid());
        imgDetailsEntity.setCommentCount(imgDetailsEntity.getCommentCount() + 1);
        imgDetailsDao.updateById(imgDetailsEntity);

        UserRecordEntity userRecordEntity = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", imgDetailsEntity.getUserId()));
        userRecordEntity.setNoreplyCount(userRecordEntity.getNoreplyCount() + 1);
        userRecordDao.updateById(userRecordEntity);

        //通知笔记发布的作者
        if (!commentDTO.getUid().equals(imgDetailsEntity.getUserId())) {
            try {
                WebSocketServer.sendMessageTo(JSON.toJSONString(userRecordEntity), String.valueOf(userRecordEntity.getUid()));
            } catch (Exception e) {
                throw new RenException(Constant.MSG_ERROR);
            }
        }

        //如果是二级评论
        if (commentDTO.getReplyId() != 0) {
            CommentEntity rootComment = baseDao.selectById(commentDTO.getPid());
            rootComment.setTwoNums(rootComment.getTwoNums() + 1);
            baseDao.updateById(rootComment);
            //通知回复的用户(不是当前用户才通知)
            CommentEntity replyComment = baseDao.selectById(commentDTO.getReplyId()); //原评论
            if (!replyComment.getUid().equals(commentDTO.getUid())) {
                UserRecordEntity replyCommentUser = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", replyComment.getUid()));
                replyCommentUser.setNoreplyCount(replyCommentUser.getNoreplyCount() + 1);
                userRecordDao.updateById(replyCommentUser);
                try {
                    WebSocketServer.sendMessageTo(JSON.toJSONString(replyCommentUser), String.valueOf(replyCommentUser.getUid()));
                } catch (Exception e) {
                    throw new RenException(Constant.MSG_ERROR);
                }
            }
        }

        return ConvertUtils.sourceToTarget(commentEntity, CommentVo.class);
    }
}