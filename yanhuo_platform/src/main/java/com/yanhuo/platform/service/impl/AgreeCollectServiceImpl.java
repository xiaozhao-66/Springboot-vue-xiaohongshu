package com.yanhuo.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.constant.Constant;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.constant.platform.PlatformMqConstant;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.JsonUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.common.utils.SendMessageMq;
import com.yanhuo.platform.common.PlatformDataToCache;
import com.yanhuo.platform.dao.AgreeCollectDao;
import com.yanhuo.platform.service.*;
import com.yanhuo.platform.websocket.WebSocketServer;
import com.yanhuo.xo.dto.platform.AgreeCollectDTO;
import com.yanhuo.xo.model.*;
import com.yanhuo.xo.vo.AgreeCollectVo;
import com.yanhuo.xo.vo.CommentVo;
import com.yanhuo.xo.vo.UserRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgreeCollectServiceImpl extends ServiceImpl<AgreeCollectDao, AgreeCollect> implements AgreeCollectService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    ImgDetailService imgDetailService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    AlbumService albumService;

    @Autowired
    AlbumImgRelationService albumImgRelationService;

    @Autowired
    SendMessageMq sendMessageMq;

    @Autowired
    PlatformDataToCache platformDataToCache;

    private void agreeByType(AgreeCollectDTO agreeCollectDTO, String agreeKey) {
        boolean isMember = redisUtils.sIsMember(agreeKey, String.valueOf(agreeCollectDTO.getUid()));
        if (isMember) {
            redisUtils.sRemove(agreeKey, String.valueOf(agreeCollectDTO.getUid()));
        } else {
            redisUtils.sAdd(agreeKey, String.valueOf(agreeCollectDTO.getUid()));
        }
    }


    /**
     * 通知用户点赞和收藏记录
     *
     * @param agreeCollectDTO
     * @param agreeCollect
     */
    private void agreeCollectNotice(AgreeCollectDTO agreeCollectDTO, AgreeCollect agreeCollect) {
        String userRecordKey = PlatformConstant.USER_RECORD + agreeCollectDTO.getAgreeCollectUid();
        UserRecordVo userRecordVo;
        if (Boolean.TRUE.equals(redisUtils.hasKey(userRecordKey)) && !agreeCollectDTO.getUid().equals(agreeCollect.getAgreeCollectUid())) {
            //如果当前点赞的用户是本用户不需要通知
            userRecordVo = JsonUtils.parseObject(redisUtils.get(userRecordKey), UserRecordVo.class);
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
    @Transactional(rollbackFor = Exception.class)
    public void agree(AgreeCollectDTO agreeCollectDTO) {
        //如果点赞的是图片
        if (agreeCollectDTO.getType() == 1) {
            String agreeImgKey = PlatformConstant.AGREE_IMG_KEY + agreeCollectDTO.getAgreeCollectId();
            agreeByType(agreeCollectDTO, agreeImgKey);
        } else if (agreeCollectDTO.getType() == 0) {
            String agreeCommentKey = PlatformConstant.AGREE_COMMENT_KEY + agreeCollectDTO.getAgreeCollectId();
            agreeByType(agreeCollectDTO, agreeCommentKey);
        }

        AgreeCollect agreeCollect = ConvertUtils.sourceToTarget(agreeCollectDTO, AgreeCollect.class);
        this.save(agreeCollect);

        if (agreeCollectDTO.getType() == 1) {
            ImgDetail imgDetail = imgDetailService.getById(agreeCollectDTO.getAgreeCollectId());
            if (imgDetail.getAgreeCount() <= 100) {
                imgDetail.setAgreeCount(imgDetail.getAgreeCount() + 1);
                sendMessageMq.sendMessage("imgDetailState.direct", "imgDetailStateKey.update", imgDetail);
            } else {
                String imgDetailStateKey = PlatformConstant.IMG_DETAIL_STATE + agreeCollectDTO.getAgreeCollectId();
                platformDataToCache.imgDetailDataToCache(imgDetail, imgDetailStateKey, 0, 1);
            }
        } else if (agreeCollectDTO.getType() == 0) {
            //点赞评论
            Comment comment = commentService.getById(agreeCollectDTO.getAgreeCollectId());
            if (comment.getCount() <= 100) {
                comment.setCount(comment.getCount() + 1);
                sendMessageMq.sendMessage("commentState.direct", "commentStateKey.update", comment);
            } else {
                String commentStateKey = PlatformConstant.COMMENT_STATE + agreeCollectDTO.getAgreeCollectId();
                platformDataToCache.commentDataToCache(comment, commentStateKey, 0, 1);
            }
        }
        agreeCollectNotice(agreeCollectDTO, agreeCollect);
    }


    @Override
    public boolean isAgree(AgreeCollectDTO agreeCollectDTO) {
        if (agreeCollectDTO.getType() == 1) {
            String agreeImgKey = PlatformConstant.AGREE_IMG_KEY + agreeCollectDTO.getAgreeCollectId();
            return redisUtils.sIsMember(agreeImgKey, String.valueOf(agreeCollectDTO.getUid()));
        } else {
            String agreeCommentKey = PlatformConstant.AGREE_COMMENT_KEY + agreeCollectDTO.getAgreeCollectId();
            return redisUtils.sIsMember(agreeCommentKey, String.valueOf(agreeCollectDTO.getUid()));
        }

    }

    @Override
    public Page<AgreeCollectVo> getAllAgreeAndCollection(long page, long limit, String uid) {
        Page<AgreeCollectVo> resPage = new Page<>();

        Page<AgreeCollect> agreeCollectPage = this.page(new Page<>(page, limit), new QueryWrapper<AgreeCollect>().and(e -> e.eq("agree_collect_uid", uid).ne("uid", uid)).orderByDesc("create_date"));
        List<AgreeCollect> agreeCollectList = agreeCollectPage.getRecords();
        long total = agreeCollectPage.getTotal();

        List<Long> uids = agreeCollectList.stream().map(AgreeCollect::getUid).collect(Collectors.toList());
        HashMap<Long, User> userMap = new HashMap<>();
        List<User> userList = userService.listByIds(uids);
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        //先遍历点赞和收藏图片的部分
        List<Long> mids = agreeCollectList.stream().filter(e -> e.getType() == 1 || e.getType() == 2).map(AgreeCollect::getAgreeCollectId).collect(Collectors.toList());
        HashMap<Long, ImgDetail> imgDetailMap = new HashMap<>();
        List<ImgDetail> imgDetailList;
        if (!mids.isEmpty()) {
            imgDetailList = imgDetailService.listByIds(mids);
            imgDetailList.forEach(item -> {
                imgDetailMap.put(item.getId(), item);
            });
        }

        //得到评论
        List<Long> cids = agreeCollectList.stream().filter(e -> e.getType() == 0).map(AgreeCollect::getAgreeCollectId).collect(Collectors.toList());
        Map<Long, CommentVo> commentMap = new HashMap<>();
        List<Comment> commentList;

        if (!cids.isEmpty()) {
            commentList = commentService.listByIds(cids);
            List<Long> cmidList = commentList.stream().map(Comment::getMid).collect(Collectors.toList());
            List<ImgDetail> imgDetailList1 = imgDetailService.listByIds(cmidList);
            HashMap<Long, ImgDetail> imgDetailMap1 = new HashMap<>();
            imgDetailList1.forEach(item -> {
                imgDetailMap1.put(item.getId(), item);
            });
            commentList.forEach(item -> {
                CommentVo commentVo = ConvertUtils.sourceToTarget(item, CommentVo.class);
                ImgDetail imgDetail = imgDetailMap1.get(item.getMid());
                commentVo.setCover(imgDetail.getCover());
                commentMap.put(item.getId(), commentVo);
            });
        }

        //得到专辑
        List<Long> aids = agreeCollectList.stream().filter(e -> e.getType() == 3).map(AgreeCollect::getAgreeCollectId).collect(Collectors.toList());
        HashMap<Long, Album> albunMap = new HashMap<>();
        List<Album> albumList;
        if (!aids.isEmpty()) {
            albumList = albumService.listByIds(aids);
            albumList.forEach(item -> {
                albunMap.put(item.getId(), item);
            });
        }

        List<AgreeCollectVo> agreeCollectVoList = new ArrayList<>();
        AgreeCollectVo agreeCollectVo;
        ImgDetail imgDetail;
        User user;
        for (AgreeCollect item : agreeCollectList) {
            agreeCollectVo = new AgreeCollectVo();
            user = userMap.get(item.getUid());
            if (item.getType() == 0) {
                //评论
                CommentVo commentVo = commentMap.get(item.getAgreeCollectId());
                agreeCollectVo.setMid(commentVo.getMid())
                        .setCover(commentVo.getCover())
                        .setUid(user.getId())
                        .setAvatar(user.getAvatar())
                        .setUsername(user.getUsername())
                        .setType(item.getType())
                        .setCreateDate(item.getCreateDate())
                        //评论的内容
                        .setContent(commentVo.getContent());

            } else if (item.getType() == 3) {

                Album album = albunMap.get(item.getAgreeCollectId());
                agreeCollectVo.setAid(album.getId())
                        .setCover(album.getCover())
                        .setAvatar(user.getAvatar())
                        .setUid(user.getId())
                        .setUsername(user.getUsername())
                        .setType(item.getType())
                        .setCreateDate(item.getCreateDate())
                        .setContent(album.getName());

            } else {
                imgDetail = imgDetailMap.get(item.getAgreeCollectId());
                agreeCollectVo.setMid(imgDetail.getId())
                        .setCover(imgDetail.getCover())
                        .setUid(user.getId())
                        .setAvatar(user.getAvatar())
                        .setUsername(user.getUsername())
                        .setType(item.getType())
                        .setCreateDate(item.getCreateDate())
                        .setContent(imgDetail.getContent());
            }
            agreeCollectVoList.add(agreeCollectVo);
        }

        resPage.setRecords(agreeCollectVoList);
        resPage.setTotal(total);
        return resPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAgree(AgreeCollectDTO agreeCollectDTO) {
        //如果点赞的是图片
        if (agreeCollectDTO.getType() == 1) {
            String agreeImgKey = PlatformConstant.AGREE_IMG_KEY + agreeCollectDTO.getAgreeCollectId();
            agreeByType(agreeCollectDTO, agreeImgKey);
        } else if (agreeCollectDTO.getType() == 0) {
            String agreeCommentKey = PlatformConstant.AGREE_COMMENT_KEY + agreeCollectDTO.getAgreeCollectId();
            agreeByType(agreeCollectDTO, agreeCommentKey);
        }

        this.remove(new QueryWrapper<AgreeCollect>().and(e -> e.eq("uid", agreeCollectDTO.getUid()).eq("agree_collect_id", agreeCollectDTO.getAgreeCollectId()).eq("agree_collect_uid", agreeCollectDTO.getAgreeCollectUid()).eq("type", agreeCollectDTO.getType())));

        if (agreeCollectDTO.getType() == 1) {
            ImgDetail imgDetail = imgDetailService.getById(agreeCollectDTO.getAgreeCollectId());
            if (imgDetail.getAgreeCount() <= 100) {
                imgDetail.setAgreeCount(imgDetail.getAgreeCount() - 1);
                sendMessageMq.sendMessage(PlatformMqConstant.IMG_DETAIL_STATE_EXCHANGE, PlatformMqConstant.IMG_DETAIL_STATE_KEY, imgDetail);
            } else {
                String imgDetailStateKey = PlatformConstant.IMG_DETAIL_STATE + agreeCollectDTO.getAgreeCollectId();
                platformDataToCache.imgDetailDataToCache(imgDetail, imgDetailStateKey, 0, -1);
            }
        } else if (agreeCollectDTO.getType() == 0) {
            //点赞评论
            Comment comment = commentService.getById(agreeCollectDTO.getAgreeCollectId());
            if (comment.getCount() < 100) {
                comment.setCount(comment.getCount() - 1);
                sendMessageMq.sendMessage(PlatformMqConstant.COMMON_STATE_EXCHANGE, PlatformMqConstant.COMMON_STATE_KEY, comment);
            } else {
                String commentStateKey = PlatformConstant.COMMENT_STATE + agreeCollectDTO.getAgreeCollectId();
                platformDataToCache.commentDataToCache(comment, commentStateKey, 0, -1);
            }
        }

        //更改用户记录表
        String userRecordKey = PlatformConstant.USER_RECORD + agreeCollectDTO.getAgreeCollectUid();
        UserRecordVo userRecordVo;
        if (Boolean.TRUE.equals(redisUtils.hasKey(userRecordKey))) {
            userRecordVo = JsonUtils.parseObject(redisUtils.get(userRecordKey), UserRecordVo.class);
            userRecordVo.setAgreeCollectionCount(userRecordVo.getAgreeCollectionCount() - 1);
            redisUtils.set(userRecordKey, JSON.toJSONString(userRecordVo));
        }
    }

    @Override
    public Page<AgreeCollectVo> getAllCollection(long page, long limit, String uid, Integer type) {
        Page<AgreeCollectVo> resPage = new Page<>();
        Page<AgreeCollect> agreeCollectPage = this.page(new Page<>(page, limit), new QueryWrapper<AgreeCollect>().and(e -> e.eq("uid", uid).eq("type", type)).orderByDesc("create_date"));

        List<AgreeCollect> agreeCollectList = agreeCollectPage.getRecords();

        if (agreeCollectList.isEmpty()) {
            return resPage;
        }
        long total = agreeCollectPage.getTotal();
        List<Long> ids = agreeCollectList.stream().map(AgreeCollect::getAgreeCollectId).collect(Collectors.toList());
        List<AgreeCollectVo> agreeCollectVoList = new ArrayList<>();

        if (type == 2) {
            //查找所有收藏的图片
            List<Long> uids = agreeCollectList.stream().map(AgreeCollect::getAgreeCollectUid).collect(Collectors.toList());

            HashMap<Long, User> userMap = new HashMap<>();
            HashMap<Long, ImgDetail> imgDetailMap = new HashMap<>();
            List<ImgDetail> imgDetailList = imgDetailService.listByIds(ids);
            List<User> userList = userService.listByIds(uids);

            imgDetailList.forEach(item -> {
                imgDetailMap.put(item.getId(), item);
            });
            userList.forEach(item -> {
                userMap.put(item.getId(), item);
            });

            AgreeCollectVo agreeCollectVo;
            ImgDetail imgDetail;
            User user;
            for (AgreeCollect item : agreeCollectList) {
                agreeCollectVo = new AgreeCollectVo();
                imgDetail = imgDetailMap.get(item.getAgreeCollectId());
                user = userMap.get(item.getAgreeCollectUid());

                agreeCollectVo.setMid(imgDetail.getId())
                        .setCount(imgDetail.getCount())
                        .setCover(imgDetail.getCover())
                        .setUid(imgDetail.getUserId())
                        .setAvatar(user.getAvatar())
                        .setUsername(user.getUsername())
                        .setContent(imgDetail.getContent())
                        .setType(1)
                        .setCreateDate(item.getCreateDate());

                agreeCollectVoList.add(agreeCollectVo);
            }
        } else if (type == 3) {
            //所有收藏的专辑
            List<Album> albumList = albumService.listByIds(ids);
            AgreeCollectVo agreeCollectVo;
            for (Album album : albumList) {
                agreeCollectVo = ConvertUtils.sourceToTarget(album, AgreeCollectVo.class);
                agreeCollectVo.setAid(album.getId());
                agreeCollectVoList.add(agreeCollectVo);
            }
            Collections.reverse(agreeCollectList);
        }

        resPage.setRecords(agreeCollectVoList);
        resPage.setTotal(total);
        return resPage;
    }

    @Override
    public Map<String, String> collection(AgreeCollectDTO agreeCollectDTO) {
        Map<String, String> res = new HashMap<>();
        AgreeCollect isAgreeCollect = this.getOne(new QueryWrapper<AgreeCollect>().and(e -> e.eq("uid", agreeCollectDTO.getUid()).eq("agree_collect_id", agreeCollectDTO.getAgreeCollectId()).eq("type", agreeCollectDTO.getType())));
        if (isAgreeCollect != null) {
            res.put(PlatformConstant.MESSAGE, PlatformConstant.COLLECTION_USER_SUCCESS);
            return res;
        }
        AgreeCollect agreeCollect = ConvertUtils.sourceToTarget(agreeCollectDTO, AgreeCollect.class);
        this.save(agreeCollect);

        Album album = albumService.getById(agreeCollectDTO.getAgreeCollectId());

        if (album.getCollectionCount() <= 100) {
            album.setCollectionCount(album.getCollectionCount() + 1);
            sendMessageMq.sendMessage(PlatformMqConstant.ALBUM_STATE_EXCHANGE, PlatformMqConstant.ALBUM_STATE_KEY, album);
        } else {
            String albumStateKey = PlatformConstant.ALBUM_STATE + agreeCollectDTO.getAgreeCollectId();
            platformDataToCache.albumDataToCache(album, albumStateKey, 1);
        }

        //更改用户记录表
        agreeCollectNotice(agreeCollectDTO, agreeCollect);

        res.put(PlatformConstant.MESSAGE, PlatformConstant.COLLECTION_SUCCESS);
        return res;
    }

    @Override
    public Map<String, String> cancelCollection(AgreeCollectDTO agreeCollectDTO) {
        Map<String, String> res = new HashMap<>();

        if (agreeCollectDTO.getType() == 2) {
            //取消收藏图片
            String imgDetailListKey = PlatformConstant.IMG_DETAIL_LIST_KEY;
            if (Boolean.TRUE.equals(redisUtils.hExists(imgDetailListKey, String.valueOf(agreeCollectDTO.getAgreeCollectId())))) {
                //存储所有用户点赞信息
                ImgDetail imgDetail = JsonUtils.parseObject((String) redisUtils.hGet(imgDetailListKey, String.valueOf(agreeCollectDTO.getAgreeCollectId())), ImgDetail.class);
                imgDetail.setCollectionCount(imgDetail.getCollectionCount() - 1);
                redisUtils.hPut(PlatformConstant.IMG_DETAIL_LIST_KEY, String.valueOf(agreeCollectDTO.getAgreeCollectId()), JsonUtils.toJsonString(imgDetail));
            }

            String albumImgRelationKey = PlatformConstant.ALBUM_IMG_RELATION_KEY + agreeCollectDTO.getAgreeCollectId();
            boolean isMember = albumImgRelationService.isCollectImgToAlbum(String.valueOf(agreeCollectDTO.getUid()), String.valueOf(agreeCollectDTO.getAgreeCollectId()));
            if (isMember) {
                redisUtils.sRemove(albumImgRelationKey, String.valueOf(agreeCollectDTO.getUid()));
            }

            AgreeCollect collectionEntity = this.getOne(new QueryWrapper<AgreeCollect>().and(e -> e.eq("uid", agreeCollectDTO.getUid()).eq("agree_collect_id", agreeCollectDTO.getAgreeCollectId()).eq("type", agreeCollectDTO.getType())));
            if (collectionEntity == null) {
                res.put(PlatformConstant.MESSAGE, PlatformConstant.COLLECTION_ERROR);
                return res;
            }

            ImgDetail imgDetail = imgDetailService.getById(agreeCollectDTO.getAgreeCollectId());
            if (imgDetail.getCollectionCount() <= 100) {
                imgDetail.setCollectionCount(imgDetail.getCollectionCount() - 1);
                sendMessageMq.sendMessage(PlatformMqConstant.IMG_DETAIL_STATE_EXCHANGE, PlatformMqConstant.IMG_DETAIL_STATE_KEY, imgDetail);
            } else {
                String imgDetailStateKey = PlatformConstant.IMG_DETAIL_STATE + agreeCollectDTO.getAgreeCollectId();
                platformDataToCache.imgDetailDataToCache(imgDetail, imgDetailStateKey, 1, -1);
            }


            List<AlbumImgRelation> albumImgRelationList = albumImgRelationService.list(new QueryWrapper<AlbumImgRelation>().eq("mid", agreeCollectDTO.getAgreeCollectId()));

            List<Long> albumIds = new ArrayList<>();
            albumImgRelationList.forEach(item -> {
                albumIds.add(item.getAid());
            });

            List<Album> albumList = albumService.listByIds(albumIds);

            Map<Long, Album> albumMap = new HashMap<>();

            albumList.forEach(item -> {
                albumMap.put(item.getId(), item);
            });

            for (AlbumImgRelation albumImgRelation : albumImgRelationList) {
                Album album = albumMap.get(albumImgRelation.getAid());
                //找到当前用户专辑下绑定的图片
                if (album.getUid().equals(agreeCollectDTO.getUid())) {
                    albumImgRelationService.remove(new QueryWrapper<AlbumImgRelation>().and(e -> e.eq("aid", albumImgRelation.getAid()).eq("mid", agreeCollectDTO.getAgreeCollectId())));
                    break;
                }
            }
        } else if (agreeCollectDTO.getType() == 3) {
            AgreeCollect agreeCollect = this.getOne(new QueryWrapper<AgreeCollect>().and(e -> e.eq("uid", agreeCollectDTO.getUid()).eq("agree_collect_id", agreeCollectDTO.getAgreeCollectId())).eq("type", agreeCollectDTO.getType()));
            if (agreeCollect == null) {
                res.put(PlatformConstant.MESSAGE, PlatformConstant.COLLECTION_USER_FAIL);
                return res;
            }

            Album album = albumService.getById(agreeCollectDTO.getAgreeCollectId());

            if (album.getCollectionCount() <= 100) {
                album.setCollectionCount(album.getCollectionCount() - 1);
                sendMessageMq.sendMessage(PlatformMqConstant.ALBUM_STATE_EXCHANGE, PlatformMqConstant.ALBUM_STATE_KEY, album);
            } else {
                String albumStateKey = PlatformConstant.ALBUM_STATE + agreeCollectDTO.getAgreeCollectId();
                platformDataToCache.albumDataToCache(album, albumStateKey, -1);
            }
        }

        this.remove(new QueryWrapper<AgreeCollect>().and(e -> e.eq("uid", agreeCollectDTO.getUid()).eq("agree_collect_id", agreeCollectDTO.getAgreeCollectId()).eq("type", agreeCollectDTO.getType())));
        res.put(PlatformConstant.MESSAGE, PlatformConstant.COLLECTION_CANCEL);
        return res;
    }
}
