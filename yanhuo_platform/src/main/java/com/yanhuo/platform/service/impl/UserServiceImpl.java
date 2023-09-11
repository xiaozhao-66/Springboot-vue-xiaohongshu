package com.yanhuo.platform.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.common.utils.JsonUtils;
import com.yanhuo.common.utils.PageUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.platform.dao.UserDao;
import com.yanhuo.platform.service.*;
import com.yanhuo.xo.dto.search.SearchRecordDTO;
import com.yanhuo.xo.model.*;
import com.yanhuo.xo.vo.FollowVo;
import com.yanhuo.xo.vo.TrendVo;
import com.yanhuo.xo.vo.UserRecordVo;
import com.yanhuo.xo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    FollowService followService;

    @Autowired
    ImgDetailService imgDetailService;

    @Autowired
    AlbumImgRelationService albumImgRelationService;

    @Autowired
    AlbumService albumService;

    @Override
    public Page<TrendVo> getTrendByUser(long page, long limit, String userId, Integer type) {
        Page<ImgDetail> imgDetailPage;
        if (type == 0) {
            imgDetailPage = imgDetailService.page(new Page<>(page, limit), new QueryWrapper<ImgDetail>().and(e -> e.eq("user_id", userId).eq("status", 1)).orderByDesc("update_date"));
        } else {
            imgDetailPage = imgDetailService.page(new Page<>(page, limit), new QueryWrapper<ImgDetail>().eq("user_id", userId).orderByDesc("update_date"));
        }

        List<TrendVo> trendVoList = new ArrayList<>();
        List<ImgDetail> imgDetailList = imgDetailPage.getRecords();
        long total = imgDetailPage.getTotal();

        List<Long> mids = imgDetailList.stream().map(ImgDetail::getId).collect(Collectors.toList());
        List<AlbumImgRelation> albumImgRelationList = albumImgRelationService.list(new QueryWrapper<AlbumImgRelation>().in("mid", mids));
        List<Long> aids = albumImgRelationList.stream().map(AlbumImgRelation::getAid).collect(Collectors.toList());
        Map<Long, AlbumImgRelation> albumImgRelationMap = new HashMap<>();
        List<Album> albumList = albumService.listByIds(aids);
        Map<Long, Album> albumMap = new HashMap<>();
        albumImgRelationList.forEach(item -> {
            albumImgRelationMap.put(item.getMid(), item);
        });
        albumList.forEach(item -> {
            albumMap.put(item.getId(), item);
        });

        TrendVo trendVo;
        for (ImgDetail model : imgDetailList) {
            AlbumImgRelation albumImgRelation = albumImgRelationMap.get(model.getId());
            Album album = albumMap.get(albumImgRelation.getAid());
            trendVo = ConvertUtils.sourceToTarget(model, TrendVo.class);
            trendVo.setMid(model.getId())
                    .setStatus(model.getStatus())
                    .setTime(model.getUpdateDate())
                    .setAlbumId(album.getId())
                    .setAlbumName(album.getName());
            trendVoList.add(trendVo);
        }

        Page<TrendVo> trendVoPage = new Page<>();
        trendVoPage.setRecords(trendVoList);
        trendVoPage.setTotal(total);
        return trendVoPage;
    }


    @Override
    public Page<FollowVo> searchUser(long page, long limit, String keyword, String uid) {
        List<FollowVo> list = new ArrayList<>();

        //得到所有搜索的用户
        List<User> userList = this.list(new QueryWrapper<User>().like("user_id", keyword).or().like("username", keyword));
        //得到当前用户所有关注的用户
        List<Follow> followList = followService.list(new QueryWrapper<Follow>().eq("uid", uid).orderByDesc("create_date"));
        //所有关注者
        List<Long> ids = followList.stream().map(Follow::getFid).collect(Collectors.toList());

        FollowVo followVo;
        for (User user : userList) {
            followVo = new FollowVo();
            followVo.setIsfollow(ids.contains(user.getId()))
                    .setUid(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setFanCount(user.getFanCount())
                    .setUserId(user.getUserId());
            list.add(followVo);
        }
        return PageUtils.getPages((int) page, (int) limit, list);
    }

    @Override
    public User updateUser(User user) {
        this.updateById(user);
        return user;
    }

    @Override
    public List<UserVo> searchUserByUsername(String username) {
        List<User> userList = this.list(new QueryWrapper<User>().eq("username", username));
        if (userList.isEmpty()) {
            return new ArrayList<>();
        }
        return ConvertUtils.sourceToTarget(userList, UserVo.class);
    }

    @Override
    public UserRecordVo getUserRecord(String uid) {
        String userRecordKey = PlatformConstant.USER_RECORD + uid;
        UserRecordVo userRecordVo = new UserRecordVo();
        if (Boolean.TRUE.equals(redisUtils.hasKey(userRecordKey))) {
            userRecordVo = JsonUtils.parseObject(redisUtils.get(userRecordKey), UserRecordVo.class);
        }
        return userRecordVo;
    }

    @Override
    public void clearUserRecord(String uid, Integer type) {
        String userRecordKey = PlatformConstant.USER_RECORD + uid;
        UserRecordVo userRecordVo;
        if (Boolean.TRUE.equals(redisUtils.hasKey(userRecordKey))) {
            userRecordVo = JsonUtils.parseObject(redisUtils.get(userRecordKey), UserRecordVo.class);
            if (type == 1) {
                userRecordVo.setAgreeCollectionCount(0L);
            } else if (type == 2) {
                userRecordVo.setAddFollowCount(0L);
            } else {
                userRecordVo.setNoreplyCount(0L);
            }
            redisUtils.set(userRecordKey, JsonUtils.toJsonString(userRecordVo));
        }
    }

    @Override
    public List<String> getAllSearchRecord(String uid) {
        String userRecordKey = PlatformConstant.USER_SEARCH_RECORD + uid;
        return redisUtils.lRange(userRecordKey, 0, 20);
    }

    @Override
    public void addSearchRecord(SearchRecordDTO searchRecordDTO) {
        String userSearchRecordKey = PlatformConstant.USER_SEARCH_RECORD + searchRecordDTO.getUid();
        if (Boolean.TRUE.equals(redisUtils.hasKey(userSearchRecordKey))) {
            redisUtils.lRemove(userSearchRecordKey, 0, searchRecordDTO.getKeyword());
        }
        redisUtils.lLeftPush(userSearchRecordKey, searchRecordDTO.getKeyword());
    }

    @Override
    public void deleteSearchRecord(List<String> words, String uid) {
        String userSearchRecordKey = PlatformConstant.USER_SEARCH_RECORD + uid;
        if (Boolean.TRUE.equals(redisUtils.hasKey(userSearchRecordKey))) {
            for (String word : words) {
                redisUtils.lRemove(userSearchRecordKey, 0, word);
            }
        }
    }

    @Override
    public void addBulkUserRecord() {
        List<User> list = this.list();
        for (User user : list) {
            String userRecordKey = PlatformConstant.USER_RECORD + user.getId();
            UserRecordVo userRecordVo = new UserRecordVo();
            userRecordVo.setAgreeCollectionCount(0L);
            userRecordVo.setAddFollowCount(0L);
            userRecordVo.setNoreplyCount(0L);
            redisUtils.set(userRecordKey, JsonUtils.toJsonString(userRecordVo));
        }
    }
}