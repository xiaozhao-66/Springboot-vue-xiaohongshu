package com.yanhuo.platform.task;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.JsonUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.platform.service.AlbumService;
import com.yanhuo.platform.service.CommentService;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.model.Album;
import com.yanhuo.xo.model.Comment;
import com.yanhuo.xo.model.ImgDetail;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.state.AlbumState;
import com.yanhuo.xo.state.CommentState;
import com.yanhuo.xo.state.ImgDetailState;
import com.yanhuo.xo.state.UserState;
import com.yanhuo.xo.vo.ImgDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class PlatformTask {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    ImgDetailService imgDetailService;

    @Autowired
    CommentService commentService;

    @Autowired
    AlbumService albumService;

    @Autowired
    UserService userService;

    @Async
    @Scheduled(fixedDelay = 1000 * 60)  //间隔1分
    public void updateImgDetail() {

        Set<String> listKey = redisUtils.getListKey(PlatformConstant.IMG_DETAIL_STATE);
        if (!listKey.isEmpty()) {
            for (String k : listKey) {
                String mid = k.split(":")[1];
                ImgDetail imgDetail = imgDetailService.getById(mid);
                String imgDetailStateKey = PlatformConstant.IMG_DETAIL_STATE + mid;
                ImgDetailState imgDetailState = JsonUtils.parseObject(redisUtils.get(imgDetailStateKey), ImgDetailState.class);
                imgDetail.setViewCount(imgDetailState.getViewCount());
                imgDetail.setAgreeCount(imgDetailState.getAgreeCount());
                imgDetail.setCommentCount(imgDetailState.getCommentCount());
                imgDetail.setCollectionCount(imgDetailState.getCollectionCount());
                imgDetailService.updateById(imgDetail);
            }
        }
    }

    @Async
    @Scheduled(fixedDelay = 1000 * 60)  //间隔1分
    public void updateComment() {

        Set<String> listKey = redisUtils.getListKey(PlatformConstant.COMMENT_STATE);
        if (!listKey.isEmpty()) {
            for (String k : listKey) {
                String cid = k.split(":")[1];
                Comment comment = commentService.getById(cid);
                String commonStateKey = PlatformConstant.COMMENT_STATE + cid;
                CommentState commentState = JsonUtils.parseObject(redisUtils.get(commonStateKey), CommentState.class);
                comment.setTwoNums(commentState.getTwoNums());
                comment.setCount(commentState.getCount());
                commentService.updateById(comment);
            }
        }
    }

    @Async
    @Scheduled(fixedDelay = 1000 * 60)  //间隔1分
    public void updateUser() {

        Set<String> listKey = redisUtils.getListKey(PlatformConstant.USER_STATE);
        if (!listKey.isEmpty()) {
            for (String k : listKey) {
                String uid = k.split(":")[1];
                User user = userService.getById(uid);
                String userStateKey = PlatformConstant.USER_STATE + uid;
                UserState userState = JsonUtils.parseObject(redisUtils.get(userStateKey), UserState.class);
                user.setTrendCount(userState.getTrendCount());
                user.setFollowCount(userState.getFollowCount());
                user.setFanCount(userState.getFanCount());

                userService.updateById(user);
            }
        }
    }

    @Async
    @Scheduled(fixedDelay = 1000 * 60)  //间隔1分
    public void updateAlbum() {

        Set<String> listKey = redisUtils.getListKey(PlatformConstant.ALBUM_STATE);
        if (!listKey.isEmpty()) {
            for (String k : listKey) {
                String aid = k.split(":")[1];
                Album album = albumService.getById(aid);
                String albumStateKey = PlatformConstant.ALBUM_STATE + aid;
                AlbumState albumState = JsonUtils.parseObject(redisUtils.get(albumStateKey), AlbumState.class);
                album.setCollectionCount(albumState.getCollectionCount());
                albumService.updateById(album);
            }
        }
    }


    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 10)  //10分钟刷新一次热榜
    public void refreshHot() {

        List<ImgDetail> imgDetailList = imgDetailService.list(new QueryWrapper<ImgDetail>().orderByDesc("agree_count").ge("agree_count", 1));
        List<ImgDetailVo> list = new ArrayList<>();

        List<Long> uids = imgDetailList.stream().map(ImgDetail::getUserId).collect(Collectors.toList());

        List<User> userList = userService.listByIds(uids);

        HashMap<Long, User> userMap = new HashMap<>();
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        ImgDetailVo imgDetailVo;
        List<String> imgList;
        User userEntity;
        for (ImgDetail model : imgDetailList) {
            imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            userEntity = userMap.get(model.getUserId());
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            imgDetailVo.setImgsUrl(imgList)
                    .setUsername(userEntity.getUsername())
                    .setAvatar(userEntity.getAvatar());
            list.add(imgDetailVo);
        }

        redisUtils.set(PlatformConstant.HOT, JSON.toJSONString(list));
    }
}
