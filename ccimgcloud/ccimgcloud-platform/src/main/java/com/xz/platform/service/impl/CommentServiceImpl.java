package com.xz.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.exception.RenException;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.DateUtils;
import com.xz.common.utils.PageUtils;
import com.xz.platform.common.constant.Constant;
import com.xz.platform.dao.CommentDao;
import com.xz.platform.dao.ImgDetailsDao;
import com.xz.platform.dao.UserDao;
import com.xz.platform.dao.UserRecordDao;
import com.xz.platform.dto.AgreeDTO;
import com.xz.platform.dto.CommentDTO;
import com.xz.platform.entity.CommentEntity;
import com.xz.platform.entity.ImgDetailsEntity;
import com.xz.platform.entity.UserRecordEntity;
import com.xz.platform.service.AgreeService;
import com.xz.platform.service.CommentService;
import com.xz.platform.vo.CommentVo;
import com.xz.platform.websocket.WebSocketServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class CommentServiceImpl extends CrudServiceImpl<CommentDao, CommentEntity, CommentDTO> implements CommentService {

    @Autowired
    UserDao userDao;

    @Autowired
    ImgDetailsDao imgDetailsDao;


    @Autowired
    AgreeService agreeService;

    @Autowired
    UserRecordDao userRecordDao;

    @Override
    public QueryWrapper<CommentEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<CommentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        return wrapper;
    }

    private boolean isAgree(String uid, String mid) {
        AgreeDTO agreeDTO = new AgreeDTO();
        agreeDTO.setUid(Long.valueOf(uid));
        agreeDTO.setAgreeId(Long.valueOf(mid));
        agreeDTO.setType(0);
        return agreeService.isAgree(agreeDTO);
    }

    @Override
    public IPage<CommentVo> getAllOneCommentByImgId(long page, long limit, String mid, String uid) {

        QueryWrapper<CommentEntity> rootQueryWrapper = new QueryWrapper<CommentEntity>().and(e -> e.eq("pid", "0").eq("mid", mid)).orderByDesc("count").orderByDesc("create_date");

        List<CommentEntity> commentOneList = baseDao.selectList(rootQueryWrapper);

        if (commentOneList.isEmpty()) {
            return new Page<>();
        }

        List<CommentVo> commentVoList = new ArrayList<>();
        CommentVo commentVo = null;
        for (CommentEntity model : commentOneList) {
            commentVo = ConvertUtils.sourceToTarget(model, CommentVo.class);
            QueryWrapper<CommentEntity> twoQueryWrapper = new QueryWrapper<CommentEntity>().eq("pid", model.getId());
            Long aLong = baseDao.selectCount(twoQueryWrapper);
            commentVo.setChildren(aLong > 0);
            commentVo.setTime(DateUtils.timeUtile(model.getCreateDate()));

            //判断当前评论是否点赞
            boolean agree = isAgree(uid, String.valueOf(model.getId()));
            commentVo.setIsAgree(agree);
            commentVoList.add(commentVo);
        }


        return PageUtils.getPages((int) page, (int) limit, commentVoList);
    }

    @Override
    public IPage<CommentVo> getAllTwoCommentByOneId(long page, long limit, String id, String uid) {

        QueryWrapper<CommentEntity> twoQueryWrapper = new QueryWrapper<CommentEntity>().eq("pid", id).orderByDesc("create_date");
        List<CommentEntity> commentTwoList = baseDao.selectList(twoQueryWrapper);
        List<CommentVo> commentVoList = new ArrayList<>();
        CommentVo commentVo = null;
        for (CommentEntity model : commentTwoList) {
            commentVo = ConvertUtils.sourceToTarget(model, CommentVo.class);
            commentVo.setTime(DateUtils.timeUtile(model.getCreateDate()));

            //判断当前评论是否点赞
            boolean agree = isAgree(uid, String.valueOf(model.getId()));
            commentVo.setIsAgree(agree);
            commentVoList.add(commentVo);
        }

        return PageUtils.getPages((int) page, (int) limit, commentVoList);
    }

    @Override
    public List<CommentVo> getAllTwoComment(String id, String uid) {
        QueryWrapper<CommentEntity> twoQueryWrapper = new QueryWrapper<CommentEntity>().eq("pid", id).orderByDesc("create_date");
        List<CommentEntity> commentTwoList = baseDao.selectList(twoQueryWrapper);
        List<CommentVo> commentVoList = new ArrayList<>();
        CommentVo commentVo = null;
        for (CommentEntity model : commentTwoList) {
            commentVo = ConvertUtils.sourceToTarget(model, CommentVo.class);
            commentVo.setTime(DateUtils.timeUtile(model.getCreateDate()));

            //判断当前评论是否点赞
            boolean agree = isAgree(uid, String.valueOf(model.getId()));
            commentVo.setIsAgree(agree);
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
    public IPage<CommentVo> getAllReplyComment(long page, long limit, String uid) {

        List<CommentVo> replyCommentList = new ArrayList<>();
        //得到当前用户发布的所有作品
        List<ImgDetailsEntity> imgDetailList = imgDetailsDao.selectList(new QueryWrapper<ImgDetailsEntity>().eq("user_id", uid));

        for (ImgDetailsEntity model : imgDetailList) {
            //得到当前笔记的所有一级评论
            List<CommentEntity> commentList = baseDao.selectList(new QueryWrapper<CommentEntity>().and(e -> e.eq("mid", model.getId()).eq("pid", 0)));

            for (CommentEntity e : commentList) {
                if (StringUtils.equals(String.valueOf(e.getUid()), uid)) {
                    continue;
                }

                CommentVo commentVo = ConvertUtils.sourceToTarget(e, CommentVo.class);
                commentVo.setCover(model.getCover())
                        .setReplyContent(e.getContent())
                        .setCreateDate(e.getCreateDate())
                        .setTime(DateUtils.timeUtile(e.getCreateDate()));
                replyCommentList.add(commentVo);
            }

            //得到当前笔记的所有二级评论
            List<CommentEntity> twoCommentList = baseDao.selectList(new QueryWrapper<CommentEntity>().and(e -> e.eq("mid", model.getId()).ne("pid", 0)));

            for (CommentEntity comment : twoCommentList) {

                //如果二级评论是当前笔记的作者评论
                if (StringUtils.equals(String.valueOf(comment.getUid()), uid)) {
                    continue;
                }
                //得到原评论
                CommentEntity sourceComment = baseDao.selectById(comment.getReplyId());

                CommentVo commentVo = ConvertUtils.sourceToTarget(comment, CommentVo.class);
                commentVo.setCover(model.getCover())
                        .setReplyUid(sourceComment.getUid())
                        .setReplyContent(comment.getContent())
                        .setContent(sourceComment.getContent())
                        .setTime(DateUtils.timeUtile(comment.getCreateDate()));
                replyCommentList.add(commentVo);
            }

        }

        //得到当前用户的所有评论(有一级评论和二级评论)
        List<CommentEntity> commentList = baseDao.selectList(new QueryWrapper<CommentEntity>().eq("uid", uid));

        for (CommentEntity model : commentList) {

            List<CommentEntity> commentList2 = baseDao.selectList(new QueryWrapper<CommentEntity>().eq("reply_id", model.getId()));
            for (CommentEntity e : commentList2) {

                if (StringUtils.equals(String.valueOf(e.getUid()), uid)) {
                    continue;
                }

                CommentVo commentVo = ConvertUtils.sourceToTarget(e, CommentVo.class);
                commentVo.setCover(imgDetailsDao.selectById(model.getMid()).getCover())
                        .setReplyUid(model.getUid())
                        .setReplyContent(e.getContent())
                        .setContent(model.getContent())
                        .setTime(DateUtils.timeUtile(e.getCreateDate()));
                replyCommentList.add(commentVo);
            }

        }

        replyCommentList.sort((o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
        return PageUtils.getPages((int) page, (int) limit, replyCommentList);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommentVo addComment(CommentDTO commentDTO) {
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