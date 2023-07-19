package com.yanhuo.platform.service.impl;

import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.constant.Constant;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.JsonUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.platform.dao.CommentDao;
import com.yanhuo.platform.service.CommentService;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.platform.websocket.WebSocketServer;
import com.yanhuo.xo.dto.platform.CommentDTO;
import com.yanhuo.xo.model.Comment;
import com.yanhuo.xo.model.ImgDetail;
import com.yanhuo.xo.vo.CommentVo;
import com.yanhuo.xo.vo.ImgDetailVo;
import com.yanhuo.xo.vo.UserRecordVo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    ImgDetailService imgDetailService;

    @Override
    public IPage<CommentVo> getAllComment(long page, long limit, String mid, String uid) {

        //先获取所有的一级评论
        Page<Comment> commentOnePage = this.page(new Page<>(page, limit), new QueryWrapper<Comment>().and(e -> e.eq("mid", mid).eq("pid", 0)).orderByDesc("count").orderByDesc("create_date"));

        List<Comment> commentOneList = commentOnePage.getRecords();
        long oneTotal = commentOnePage.getTotal();

        List<CommentVo> commentVoList = new ArrayList<>();
        CommentVo commentVo;
        for (Comment model : commentOneList) {
            commentVo = ConvertUtils.sourceToTarget(model, CommentVo.class);
            Comment twoComment = this.getOne(new QueryWrapper<Comment>().last("limit 1").eq("pid", model.getId()).orderByDesc("create_date"));
            List<CommentVo> commentVo2List = new ArrayList<>();

            if(twoComment!=null){
                CommentVo commentVo2 = ConvertUtils.sourceToTarget(twoComment, CommentVo.class);
                String agreeCommentKey2 = PlatformConstant.AGREE_COMMENT_KEY + twoComment.getId();
                //得到当前图片下有哪些用户点赞
                List<Long> uids2;
                if (Boolean.TRUE.equals(redisUtils.hasKey(agreeCommentKey2))) {
                    uids2 = JSON.parseArray(redisUtils.get(agreeCommentKey2), Long.class);
                    commentVo2.setIsAgree(uids2.contains(Long.valueOf(uid)));
                }
                commentVo2List.add(commentVo2);
            }

            String agreeCommentKey = PlatformConstant.AGREE_COMMENT_KEY + model.getId();
            //得到当前图片下有哪些用户点赞
            List<Long> uids;
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeCommentKey))) {
                uids = JSON.parseArray(redisUtils.get(agreeCommentKey), Long.class);
                commentVo.setIsAgree(uids.contains(Long.valueOf(uid)));
            }

            commentVo.setChildrenComments(commentVo2List);
            //判断当前评论是否点赞
            commentVoList.add(commentVo);
        }

        Page<CommentVo> resPage = new Page<>();
        resPage.setRecords(commentVoList);
        resPage.setTotal(oneTotal);
        return resPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentVo addComment(CommentDTO commentDTO) {
        String imgInfoKey = PlatformConstant.IMG_INFO_KEY + commentDTO.getMid();
        if (Boolean.TRUE.equals(redisUtils.hasKey(imgInfoKey))) {
            ImgDetailVo imgDetailVo = JSON.parseObject(redisUtils.get(imgInfoKey), ImgDetailVo.class);
            imgDetailVo.setCommentCount(imgDetailVo.getCommentCount() + 1);
            redisUtils.setEx(imgInfoKey, JSON.toJSONString(imgDetailVo), 5, TimeUnit.DAYS);
        }

        Comment comment = ConvertUtils.sourceToTarget(commentDTO, Comment.class);
        this.save(comment);

        ImgDetail imgDetail = imgDetailService.getById(commentDTO.getMid());
        imgDetail.setCommentCount(imgDetail.getCommentCount() + 1);
        imgDetailService.updateById(imgDetail);

        String userRecordKey = PlatformConstant.USER_RECORD + imgDetail.getUserId();
        UserRecordVo userRecordVo;

        if (Boolean.TRUE.equals(redisUtils.hasKey(userRecordKey))&&!commentDTO.getUid().equals(imgDetail.getUserId())) {
            userRecordVo = JsonUtils.parseObject(redisUtils.get(userRecordKey), UserRecordVo.class);
            userRecordVo.setNoreplyCount(userRecordVo.getNoreplyCount() + 1);
            redisUtils.set(userRecordKey, JSON.toJSONString(userRecordVo));
            //如果当前点赞的用户是本用户不需要通知
            try {
                WebSocketServer.sendMessageTo(JSON.toJSONString(userRecordVo), String.valueOf(imgDetail.getUserId()));
            } catch (Exception e) {
                throw new YanHuoException(Constant.MSG_ERROR);
            }
        }

        //如果是二级评论
        if (commentDTO.getReplyId() != 0) {
            Comment rootComment = this.getById(commentDTO.getPid());
            rootComment.setTwoNums(rootComment.getTwoNums() + 1);
            this.updateById(rootComment);
            //通知回复的用户(不是当前用户才通知)
            Comment replyComment = this.getById(commentDTO.getReplyId()); //原评论
            String replyUserRecordKey = PlatformConstant.USER_RECORD + replyComment.getUid();

            if (Boolean.TRUE.equals(redisUtils.hasKey(replyUserRecordKey)) && !replyComment.getUid().equals(commentDTO.getUid())) {
                UserRecordVo replyUserRecordVo = JsonUtils.parseObject(redisUtils.get(replyUserRecordKey), UserRecordVo.class);
                replyUserRecordVo.setNoreplyCount(replyUserRecordVo.getNoreplyCount() + 1);
                redisUtils.set(replyUserRecordKey, JSON.toJSONString(replyUserRecordVo));

                try {
                    WebSocketServer.sendMessageTo(JSON.toJSONString(replyUserRecordVo), String.valueOf(replyComment.getUid()));
                } catch (Exception e) {
                    throw new YanHuoException(Constant.MSG_ERROR);
                }
            }
        }
        return ConvertUtils.sourceToTarget(comment, CommentVo.class);
    }

    @Override
    public IPage<CommentVo> getAllOneCommentByImgId(long page, long limit, String mid, String uid) {

        Page<CommentVo> resPage = new Page<>();
        //得到当前图片下的所有一级评论
        QueryWrapper<Comment> rootQueryWrapper = new QueryWrapper<Comment>().and(e -> e.eq("pid", "0").eq("mid", mid)).orderByDesc("count").orderByDesc("create_date");
        Page<Comment> commentOnePage = this.page(new Page<>(page, limit), rootQueryWrapper);

        if (commentOnePage.getTotal() == 0) {
            return resPage;
        }
        return getCommentVoIPage(uid, resPage, commentOnePage);
    }



    @Override
    public IPage<CommentVo> getAllTwoCommentByOneId(long page, long limit, String id, String uid) {
        QueryWrapper<Comment> twoQueryWrapper = new QueryWrapper<Comment>().eq("pid", id).orderByDesc("create_date");
        Page<CommentVo> resPage = new Page<>();
        Page<Comment> commentTwoPage = this.page(new Page<>(page, limit), twoQueryWrapper);
        return getCommentVoIPage(uid, resPage, commentTwoPage);
    }

    @Override
    public List<CommentVo> getAllTwoComment(String id, String uid) {
        QueryWrapper<Comment> twoQueryWrapper = new QueryWrapper<Comment>().eq("pid", id).orderByDesc("create_date");
        List<Comment> commentTwoList = this.list(twoQueryWrapper);
        List<CommentVo> commentVoList = new ArrayList<>();

        CommentVo commentVo;
        for (Comment model : commentTwoList) {
            commentVo = ConvertUtils.sourceToTarget(model, CommentVo.class);
            //判断当前评论是否点赞
            String agreeCommentKey = PlatformConstant.AGREE_COMMENT_KEY + model.getId();
            List<Long> uids;
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeCommentKey))) {
                uids = JSON.parseArray(redisUtils.get(agreeCommentKey), Long.class);
                commentVo.setIsAgree(uids.contains(Long.valueOf(uid)));
            }
            commentVoList.add(commentVo);
        }
        return commentVoList;
    }

    @Override
    public List<CommentVo> getAllReplyComment(long page, long limit, String uid) {
        return this.baseMapper.getAllReplyComment(page, limit, uid);
    }


    @NotNull
    private IPage<CommentVo> getCommentVoIPage(String uid, Page<CommentVo> resPage, Page<Comment> commentOnePage) {
        List<CommentVo> commentVoList = new ArrayList<>();
        CommentVo commentVo;

        for (Comment model : commentOnePage.getRecords()) {
            commentVo = ConvertUtils.sourceToTarget(model, CommentVo.class);
            String agreeCommentKey = PlatformConstant.AGREE_COMMENT_KEY + model.getId();
            //得到当前图片下有哪些用户点赞
            List<Long> uids;
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeCommentKey))) {
                uids = JSON.parseArray(redisUtils.get(agreeCommentKey), Long.class);
                commentVo.setIsAgree(uids.contains(Long.valueOf(uid)));
            }
            //判断当前评论是否点赞
            commentVoList.add(commentVo);
        }
        resPage.setRecords(commentVoList);
        resPage.setTotal(commentOnePage.getTotal());
        return resPage;
    }
}