package com.yanhuo.platform.common;

import com.yanhuo.common.utils.JsonUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.xo.model.Album;
import com.yanhuo.xo.model.Comment;
import com.yanhuo.xo.model.ImgDetail;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.state.AlbumState;
import com.yanhuo.xo.state.CommentState;
import com.yanhuo.xo.state.ImgDetailState;
import com.yanhuo.xo.state.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 48423
 */
@Component
public class PlatformDataToCache {

    @Autowired
    RedisUtils redisUtils;

    public void albumDataToCache(Album album, String key, int value) {
        AlbumState albumState;
        if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
            albumState = JsonUtils.parseObject(redisUtils.get(key), AlbumState.class);
            albumState.setCollectionCount(albumState.getCollectionCount() + value);
        } else {
            albumState = new AlbumState();
            albumState.setAid(album.getId());
            albumState.setCollectionCount(album.getCollectionCount() + value);
        }
        redisUtils.set(key, JsonUtils.toJsonString(albumState));
    }

    public void userDataToCache(User user, String key, int type, int value) {
        UserState userState;
        if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
            userState = JsonUtils.parseObject(redisUtils.get(key), UserState.class);
            if (type == 0) {
                userState.setTrendCount(userState.getTrendCount() + value);
            } else if (type == 1) {
                userState.setFollowCount(userState.getFollowCount() + value);
            } else {
                userState.setFanCount(userState.getFanCount() + value);
            }
        } else {

            if (type == 0) {
                user.setTrendCount(user.getTrendCount() + value);
            } else if (type == 1) {
                user.setFollowCount(user.getFollowCount() + value);
            } else {
                user.setFanCount(user.getFanCount() + value);
            }
            userState = new UserState();
            userState.setUid(user.getId());
            userState.setTrendCount(user.getTrendCount());
            userState.setFollowCount(user.getFollowCount());
            userState.setFanCount(user.getFanCount());
        }
        redisUtils.set(key, JsonUtils.toJsonString(userState));
    }

    public void commentDataToCache(Comment comment, String key, int type, int value) {
        CommentState commentState;
        if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
            commentState = JsonUtils.parseObject(redisUtils.get(key), CommentState.class);
            if (type == 0) {
                commentState.setCount(commentState.getCount() + value);
            } else if (type == 1) {
                commentState.setTwoNums(commentState.getTwoNums() + value);
            }
        } else {
            if (type == 0) {
                comment.setCount(comment.getCount() + value);
            } else if (type == 1) {
                comment.setTwoNums(comment.getTwoNums() + value);
            }

            commentState = new CommentState();
            commentState.setCid(comment.getId());
            commentState.setCount(comment.getCount());
            commentState.setTwoNums(comment.getTwoNums());

        }
        redisUtils.set(key, JsonUtils.toJsonString(commentState));
    }

    public void imgDetailDataToCache(ImgDetail imgDetail, String key, int type, int value) {
        ImgDetailState imgDetailState;
        if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
            imgDetailState = JsonUtils.parseObject(redisUtils.get(key), ImgDetailState.class);
            if (type == 0) {
                imgDetailState.setAgreeCount(imgDetailState.getAgreeCount() + value);
            } else if (type == 1) {
                imgDetailState.setCollectionCount(imgDetailState.getCollectionCount() + value);
            } else if (type == 2) {
                imgDetailState.setCommentCount(imgDetailState.getCommentCount() + value);
            } else {
                imgDetailState.setViewCount(imgDetailState.getViewCount() + value);
            }
        } else {
            imgDetailState = new ImgDetailState();

            if (type == 0) {
                imgDetail.setAgreeCount(imgDetail.getAgreeCount() + value);
            } else if (type == 1) {
                imgDetail.setCollectionCount(imgDetail.getCollectionCount() + value);
            } else if (type == 2) {
                imgDetail.setCommentCount(imgDetail.getCommentCount() + value);
            } else {
                imgDetail.setViewCount(imgDetail.getViewCount() + value);
            }

            imgDetailState.setMid(imgDetail.getId());
            imgDetailState.setAgreeCount(imgDetail.getAgreeCount());
            imgDetailState.setCommentCount(imgDetail.getCommentCount());
            imgDetailState.setCollectionCount(imgDetail.getCollectionCount());
            imgDetailState.setViewCount(imgDetail.getViewCount());
        }
        redisUtils.set(key, JsonUtils.toJsonString(imgDetailState));
    }
}
