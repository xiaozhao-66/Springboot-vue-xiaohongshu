package com.yanhuo.platform.service.impl;

import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.yanhuo.common.constant.Constant;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.constant.platform.PlatformMqConstant;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.utils.*;
import com.yanhuo.platform.common.PlatformDataToCache;
import com.yanhuo.platform.dao.CommentDao;
import com.yanhuo.platform.service.CommentService;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.platform.websocket.WebSocketServer;
import com.yanhuo.xo.dto.platform.CommentDTO;
import com.yanhuo.xo.model.Comment;
import com.yanhuo.xo.model.ImgDetail;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.vo.CommentVo;
import com.yanhuo.xo.vo.ImgDetailVo;
import com.yanhuo.xo.vo.UserRecordVo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    ImgDetailService imgDetailService;

    @Autowired
    UserService userService;

    @Autowired
    SendMessageMq sendMessageMq;

    @Autowired
    PlatformDataToCache platformDataToCache;

    @Override
    public IPage<CommentVo> getAllComment(long page, long limit, String mid, String uid) {

        //先获取所有的一级评论
        Page<Comment> commentOnePage = this.page(new Page<>(page, limit), new QueryWrapper<Comment>().and(e -> e.eq("mid", mid).eq("pid", 0)).orderByDesc("count").orderByDesc("create_date"));

        List<Comment> commentOneList = commentOnePage.getRecords();

        List<Long> uidList = commentOneList.stream().map(Comment::getUid).collect(Collectors.toList());

        List<User> userList = userService.listByIds(uidList);

        Map<Long, User> userMap = new HashMap<>();

        userList.forEach(item -> userMap.put(item.getId(), item));

        long oneTotal = commentOnePage.getTotal();

        List<CommentVo> commentVoList = new ArrayList<>();
        CommentVo commentVo;
        for (Comment model : commentOneList) {
            commentVo = ConvertUtils.sourceToTarget(model, CommentVo.class);
            User user = userMap.get(model.getUid());
            commentVo.setUsername(user.getUsername());
            commentVo.setAvatar(user.getAvatar());


            Comment twoComment = this.getOne(new QueryWrapper<Comment>().last("limit 1").eq("pid", model.getId()).orderByDesc("create_date"));
            List<CommentVo> commentVo2List = new ArrayList<>();

            if (twoComment != null) {
                CommentVo commentVo2 = ConvertUtils.sourceToTarget(twoComment, CommentVo.class);
                User user2 = userService.getById(commentVo2.getUid());

                commentVo2.setUsername(user2.getUsername());
                commentVo2.setAvatar(user2.getAvatar());

                if (commentVo2.getReplyUid() != 0) {
                    User replyUser = userService.getById(commentVo2.getReplyUid());
                    commentVo2.setReplyName(replyUser.getUsername());
                }

                String agreeCommentKey2 = PlatformConstant.AGREE_COMMENT_KEY + twoComment.getId();
                //得到当前图片下有哪些用户点赞
                commentVo2.setIsAgree(redisUtils.sIsMember(agreeCommentKey2, uid));

                commentVo2List.add(commentVo2);
            }

            String agreeCommentKey = PlatformConstant.AGREE_COMMENT_KEY + model.getId();
            //得到当前图片下有哪些用户点赞
            commentVo.setIsAgree(redisUtils.sIsMember(agreeCommentKey, uid));

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

        Comment comment = ConvertUtils.sourceToTarget(commentDTO, Comment.class);
        this.save(comment);

        ImgDetail imgDetail = imgDetailService.getById(commentDTO.getMid());

        if (imgDetail.getCommentCount() <= 100) {
            imgDetail.setCommentCount(imgDetail.getCommentCount() + 1);
            sendMessageMq.sendMessage(PlatformMqConstant.IMG_DETAIL_STATE_EXCHANGE, PlatformMqConstant.IMG_DETAIL_STATE_KEY, imgDetail);
        } else {
            String imgDetailStateKey = PlatformConstant.IMG_DETAIL_STATE + commentDTO.getMid();
            platformDataToCache.imgDetailDataToCache(imgDetail, imgDetailStateKey, 2, 1);
        }

        String userRecordKey = PlatformConstant.USER_RECORD + imgDetail.getUserId();
        UserRecordVo userRecordVo;

        if (Boolean.TRUE.equals(redisUtils.hasKey(userRecordKey)) && !commentDTO.getUid().equals(imgDetail.getUserId())) {
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
            sendMessageMq.sendMessage(PlatformMqConstant.COMMON_STATE_EXCHANGE, PlatformMqConstant.COMMON_STATE_KEY, rootComment);

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
        CommentVo commentVo = ConvertUtils.sourceToTarget(comment, CommentVo.class);
        User user = userService.getById(comment.getUid());

        commentVo.setAvatar(user.getAvatar());
        commentVo.setUsername(user.getUsername());

        if (comment.getReplyUid() != 0) {
            User replyUser = userService.getById(comment.getReplyUid());
            commentVo.setReplyName(replyUser.getUsername());
        }

        return commentVo;
    }

    //不用的方法
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

    //不用的方法
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

            commentVo.setIsAgree(redisUtils.sIsMember(agreeCommentKey, uid));

            commentVoList.add(commentVo);
        }
        return commentVoList;
    }

    //TODO 需要优化
    @Override
    public List<CommentVo> getAllReplyComment(long page, long limit, String uid) {

        HashMap<Long, CommentVo> map = new HashMap<>();
        List<CommentVo> replyCommentList = new ArrayList<>();
        //得到当前用户发布的所有作品
        List<ImgDetail> imgDetailList = imgDetailService.list(new QueryWrapper<ImgDetail>().eq("user_id", uid));

        for (ImgDetail model : imgDetailList) {
            //得到当前笔记的所有一级评论
            List<Comment> commentList = this.list(new QueryWrapper<Comment>().and(e -> e.eq("mid", model.getId()).eq("pid", 0)));

            if (!commentList.isEmpty()) {
                Set<Long> oneUids = commentList.stream().map(Comment::getUid).collect(Collectors.toSet());
                List<User> oneUserList = userService.listByIds(oneUids);
                Map<Long, User> oneUserMap = new HashMap<>();

                oneUserList.forEach(item -> {
                    oneUserMap.put(item.getId(), item);
                });

                for (Comment e : commentList) {
                    if (StringUtils.equals(String.valueOf(e.getUid()), uid)) {
                        continue;
                    }
                    User user = oneUserMap.get(e.getUid());
                    CommentVo commentVo = ConvertUtils.sourceToTarget(e, CommentVo.class);
                    commentVo.setCover(model.getCover())
                            .setUsername(user.getUsername())
                            .setAvatar(user.getAvatar())
                            .setCreateDate(e.getCreateDate());

                    map.put(commentVo.getId(), commentVo);
                }
            }

            //得到当前笔记的所有二级评论
            List<Comment> twoCommentList = this.list(new QueryWrapper<Comment>().and(e -> e.eq("mid", model.getId()).ne("pid", 0)));

            if (!twoCommentList.isEmpty()) {
                Set<Long> twoUids = twoCommentList.stream().map(Comment::getUid).collect(Collectors.toSet());

                List<User> twoUserList = userService.listByIds(twoUids);
                Map<Long, User> twoUserMap = new HashMap<>();

                twoUserList.forEach(item -> {
                    twoUserMap.put(item.getId(), item);
                });

                for (Comment comment : twoCommentList) {

                    //如果二级评论是当前笔记的作者评论
                    if (StringUtils.equals(String.valueOf(comment.getUid()), uid)) {
                        continue;
                    }

                    User user = twoUserMap.get(comment.getUid());

                    //得到原评论
                    Comment sourceComment = this.getById(comment.getReplyId());
                    User sourceUser = userService.getById(sourceComment.getUid());

                    CommentVo commentVo = ConvertUtils.sourceToTarget(comment, CommentVo.class);
                    commentVo.setCover(model.getCover())
                            .setUsername(user.getUsername())
                            .setAvatar(user.getAvatar())
                            .setContent(comment.getContent())
                            .setReplyUid(sourceComment.getUid())
                            .setReplyName(sourceUser.getUsername())
                            .setReplyContent(sourceComment.getContent());

                    map.put(commentVo.getId(), commentVo);
                }
            }

        }

        //得到当前用户的所有评论(有一级评论和二级评论)
        List<Comment> commentList = this.list(new QueryWrapper<Comment>().eq("uid", uid));

        for (Comment model : commentList) {
            //得到所有的回复评论
            List<Comment> commentList2 = this.list(new QueryWrapper<Comment>().eq("reply_id", model.getId()));

            if (!commentList2.isEmpty()) {
                Set<Long> uids = commentList2.stream().map(Comment::getUid).collect(Collectors.toSet());

                List<User> userList = userService.listByIds(uids);
                Map<Long, User> userMap = new HashMap<>();
                userList.forEach(item -> {
                    userMap.put(item.getId(), item);
                });

                Set<Long> mids = commentList2.stream().map(Comment::getMid).collect(Collectors.toSet());

                List<ImgDetail> imgList = imgDetailService.listByIds(mids);

                Map<Long, ImgDetail> imgDetailMap = new HashMap<>();

                imgList.forEach(item -> {
                    imgDetailMap.put(item.getId(), item);
                });

                for (Comment e : commentList2) {

                    if (StringUtils.equals(String.valueOf(e.getUid()), uid) || map.containsKey(e.getId())) {
                        continue;
                    }

                    CommentVo commentVo = ConvertUtils.sourceToTarget(e, CommentVo.class);
                    User user = userMap.get(e.getUid());
                    ImgDetail imgDetail = imgDetailMap.get(e.getMid());

                    commentVo.setCover(imgDetail.getCover())
                            .setUsername(user.getUsername())
                            .setAvatar(user.getAvatar())
                            .setReplyContent(model.getContent())
                            .setContent(e.getContent());
                    map.put(commentVo.getId(), commentVo);
                }
            }

        }

        for (Long k : map.keySet()) {
            replyCommentList.add(map.get(k));
        }

        replyCommentList.sort((o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));


        return PageUtils.getPages((int) page, (int) limit, replyCommentList).getRecords();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delComment(String id) {

        String commentStatKey = PlatformConstant.COMMENT_STATE + id;
        redisUtils.delete(commentStatKey);

        Comment comment = this.getById(id);

        if (comment.getPid() == 0) {
            //要把下面的所有二级评论给删了
            List<Comment> twoCommentList = this.list(new QueryWrapper<Comment>().eq("pid", comment.getId()));
            List<Long> cids = twoCommentList.stream().map(Comment::getId).collect(Collectors.toList());
            List<String> agreeCommentKeys = twoCommentList.stream().map(e -> PlatformConstant.AGREE_COMMENT_KEY + e.getId()).collect(Collectors.toList());
            List<String> commentStatKeys = twoCommentList.stream().map(e -> PlatformConstant.COMMENT_STATE + e.getId()).collect(Collectors.toList());

            redisUtils.delete(agreeCommentKeys);
            redisUtils.delete(commentStatKeys);
            this.removeBatchByIds(cids);
        } else {
            Comment oneComment = this.getById(comment.getPid());
            oneComment.setTwoNums(oneComment.getTwoNums() - 1);
            sendMessageMq.sendMessage(PlatformMqConstant.COMMON_STATE_EXCHANGE, PlatformMqConstant.COMMON_STATE_KEY, oneComment);
        }
        this.removeById(id);
    }

    @Override
    public Map<String, Object> scrollComment(String id, String mid, String uid) {
        Map<String, Object> resMap = new HashMap<>();

        Comment comment = this.getById(id);
        Long pid = comment.getPid();

        int page1 = 1;
        int page2 = 1;
        int limit1 = 6;
        int limit2 = 4;

        long total = 0;

        boolean flag = false;

        List<CommentVo> comments = new ArrayList<>();

        if (pid == 0) {
            while (!flag) {
                IPage<CommentVo> allOneCommentPage = this.getAllComment(page1, limit1, mid, uid);
                List<CommentVo> commentVoList = allOneCommentPage.getRecords();
                List<Long> pids = commentVoList.stream().map(CommentVo::getId).collect(Collectors.toList());
                if (pids.contains(Long.valueOf(id))) {
                    flag = true;
                    total = allOneCommentPage.getTotal();
                } else {
                    page1++;
                }
                comments.addAll(commentVoList);
            }
        } else {
            boolean flag2 = false;

            while (!flag) {
                IPage<CommentVo> allOneCommentPage = this.getAllComment(page1, limit1, mid, uid);
                List<CommentVo> commentVoList = allOneCommentPage.getRecords();
                List<Long> pids = commentVoList.stream().map(CommentVo::getId).collect(Collectors.toList());
                if (pids.contains(pid)) {
                    for (CommentVo commentVo : commentVoList) {
                        if (Objects.equals(commentVo.getId(), pid)) {
                            List<CommentVo> comments2 = new ArrayList<>();
                            flag = true;
                            total = allOneCommentPage.getTotal();
                            while (!flag2) {
                                IPage<CommentVo> allTwoCommentPage = this.getAllTwoCommentByOneId(page2, limit2, String.valueOf(pid), uid);
                                List<CommentVo> commentVoList2 = allTwoCommentPage.getRecords();
                                List<Long> ids = commentVoList2.stream().map(CommentVo::getId).collect(Collectors.toList());
                                if (ids.contains(Long.valueOf(id))) {
                                    flag2 = true;
                                } else {
                                    page2++;
                                }
                                comments2.addAll(commentVoList2);
                            }
                            commentVo.setChildrenComments(comments2);
                        }
                    }
                } else {
                    page1++;
                }
                comments.addAll(commentVoList);
            }
        }

        resMap.put("records", comments);
        resMap.put("total", total);
        resMap.put("page1", page1);
        resMap.put("page2", page2);

        return resMap;
    }

    @NotNull
    private IPage<CommentVo> getCommentVoIPage(String uid, Page<CommentVo> resPage, Page<Comment> commentOnePage) {
        List<CommentVo> commentVoList = new ArrayList<>();
        CommentVo commentVo;

        List<Comment> commentList = commentOnePage.getRecords();

        List<Long> uidList = commentList.stream().map(Comment::getUid).collect(Collectors.toList());

        List<User> userList = userService.listByIds(uidList);

        Map<Long, User> userMap = new HashMap<>();

        userList.forEach(item -> userMap.put(item.getId(), item));

        for (Comment model : commentList) {
            commentVo = ConvertUtils.sourceToTarget(model, CommentVo.class);
            User user = userMap.get(model.getUid());
            commentVo.setUsername(user.getUsername());
            commentVo.setAvatar(user.getAvatar());

            if (commentVo.getReplyUid() != 0) {
                User replyUser = userService.getById(commentVo.getReplyUid());
                commentVo.setReplyName(replyUser.getUsername());
            }

            String agreeCommentKey = PlatformConstant.AGREE_COMMENT_KEY + model.getId();

            commentVo.setIsAgree(redisUtils.sIsMember(agreeCommentKey, uid));

            //判断当前评论是否点赞
            commentVoList.add(commentVo);
        }
        resPage.setRecords(commentVoList);
        resPage.setTotal(commentOnePage.getTotal());
        return resPage;
    }
}