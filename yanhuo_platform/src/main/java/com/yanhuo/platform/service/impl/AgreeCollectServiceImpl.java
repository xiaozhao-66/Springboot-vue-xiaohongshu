package com.yanhuo.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.constant.Constant;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.JsonUtils;
import com.yanhuo.common.utils.RedisUtils;
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

    private void agreeByType(AgreeCollectDTO agreeCollectDTO, String agreeKey) {
        if (Boolean.TRUE.equals(redisUtils.hasKey(agreeKey))) {
            //存储所有用户点赞信息
            List<Long> uids = JSON.parseArray(redisUtils.get(agreeKey), Long.class);
            uids.add(agreeCollectDTO.getUid());
            redisUtils.set(agreeKey, JSON.toJSONString(uids));
        } else {
            List<Long> uids = new ArrayList<>();
            uids.add(agreeCollectDTO.getUid());
            redisUtils.set(agreeKey, JSON.toJSONString(uids));
        }
    }


    /**
     * 通知用户点赞和收藏记录
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
            ImgDetail imgDetail = imgDetailService.getById(agreeCollect.getAgreeCollectId());
            imgDetail.setAgreeCount(imgDetail.getAgreeCount() + 1);
            imgDetailService.updateById(imgDetail);
        } else if (agreeCollectDTO.getType() == 0) {
            //点赞评论
            Comment comment = commentService.getById(agreeCollect.getAgreeCollectId());
            comment.setCount(comment.getCount() + 1);
            commentService.updateById(comment);
        }
        agreeCollectNotice(agreeCollectDTO, agreeCollect);
    }


    @Override
    public boolean isAgree(AgreeCollectDTO agreeCollectDTO) {
        if (agreeCollectDTO.getType() == 1) {
            String agreeImgKey = PlatformConstant.AGREE_IMG_KEY + agreeCollectDTO.getAgreeCollectId();
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeImgKey))) {
                List<Long> uids = JSON.parseArray(redisUtils.get(agreeImgKey), Long.class);
                return uids.contains(agreeCollectDTO.getUid());
            }
        } else if (agreeCollectDTO.getType() == 0) {
            String agreeCommentKey = PlatformConstant.AGREE_COMMENT_KEY + agreeCollectDTO.getAgreeCollectId();
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeCommentKey))) {
                List<Long> uids = JSON.parseArray(redisUtils.get(agreeCommentKey), Long.class);
                return uids.contains(agreeCollectDTO.getUid());
            }
        }
        return false;
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
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeImgKey))) {
                //存储所有用户点赞信息
                List<Long> uids = JSON.parseArray(redisUtils.get(agreeImgKey), Long.class);
                uids.remove(agreeCollectDTO.getUid());
                redisUtils.set(agreeImgKey, JSON.toJSONString(uids));
            }
        } else if (agreeCollectDTO.getType() == 0) {
            String agreeCommentKey = PlatformConstant.AGREE_COMMENT_KEY + agreeCollectDTO.getAgreeCollectId();
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeCommentKey))) {
                List<Long> uids = JSON.parseArray(redisUtils.get(agreeCommentKey), Long.class);
                uids.remove(agreeCollectDTO.getUid());
                redisUtils.set(agreeCommentKey, JSON.toJSONString(uids));
            }
        }

        this.remove(new QueryWrapper<AgreeCollect>().and(e -> e.eq("uid", agreeCollectDTO.getUid()).eq("agree_collect_id", agreeCollectDTO.getAgreeCollectId()).eq("agree_collect_uid", agreeCollectDTO.getAgreeCollectUid())));

        if (agreeCollectDTO.getType() == 1) {
            ImgDetail imgEntity = imgDetailService.getById(agreeCollectDTO.getAgreeCollectId());
            imgEntity.setAgreeCount(imgEntity.getAgreeCount() - 1);
            imgDetailService.updateById(imgEntity);
        } else if (agreeCollectDTO.getType() == 0) {
            //点赞评论
            Comment comment = commentService.getById(agreeCollectDTO.getAgreeCollectId());
            comment.setCount(comment.getCount() - 1);
            commentService.updateById(comment);
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
            List<Long> uids = agreeCollectList.stream().map(AgreeCollect::getUid).collect(Collectors.toList());

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
                user = userMap.get(item.getUid());

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
        album.setCollectionCount(album.getCollectionCount() + 1);
        albumService.updateById(album);

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
            String albumImgRelationKey = PlatformConstant.ALBUM_IMG_RELATION_KEY + agreeCollectDTO.getAgreeCollectId();
            if (Boolean.TRUE.equals(redisUtils.hasKey(albumImgRelationKey))) {
                //存储所有用户点赞信息
                List<Long> uids = JSON.parseArray(redisUtils.get(albumImgRelationKey), Long.class);
                uids.remove(agreeCollectDTO.getUid());
                redisUtils.set(albumImgRelationKey, JSON.toJSONString(uids));
            }

            AgreeCollect collectionEntity = this.getOne(new QueryWrapper<AgreeCollect>().and(e -> e.eq("uid", agreeCollectDTO.getUid()).eq("agree_collect_id", agreeCollectDTO.getAgreeCollectId()).eq("type", agreeCollectDTO.getType())));
            if (collectionEntity == null) {
                res.put(PlatformConstant.MESSAGE, PlatformConstant.COLLECTION_ERROR);
                return res;
            }

            ImgDetail imgDetail = imgDetailService.getById(agreeCollectDTO.getAgreeCollectId());
            imgDetail.setCollectionCount(imgDetail.getCollectionCount() - 1);
            imgDetailService.updateById(imgDetail);

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
        }

        AgreeCollect agreeCollect = this.getOne(new QueryWrapper<AgreeCollect>().and(e -> e.eq("uid", agreeCollectDTO.getUid()).eq("agree_collect_id", agreeCollectDTO.getAgreeCollectId())).eq("type", agreeCollectDTO.getType()));
        if (agreeCollect == null) {
            res.put(PlatformConstant.MESSAGE, PlatformConstant.COLLECTION_USER_FAIL);
            return res;
        }

        this.remove(new QueryWrapper<AgreeCollect>().and(e -> e.eq("uid", agreeCollectDTO.getUid()).eq("agree_collect_id", agreeCollectDTO.getAgreeCollectId()).eq("type", agreeCollectDTO.getType())));
        res.put(PlatformConstant.MESSAGE, PlatformConstant.COLLECTION_CANCEL);
        return res;
    }
}
