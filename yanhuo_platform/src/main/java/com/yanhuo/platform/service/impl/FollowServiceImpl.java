package com.yanhuo.platform.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.constant.Constant;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.constant.platform.PlatformMqConstant;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.utils.JsonUtils;
import com.yanhuo.common.utils.RedisUtils;
import com.yanhuo.common.utils.SendMessageMq;
import com.yanhuo.platform.dao.FollowDao;
import com.yanhuo.platform.service.FollowService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.platform.websocket.WebSocketServer;
import com.yanhuo.xo.dto.platform.FollowDTO;
import com.yanhuo.xo.model.Follow;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.vo.FollowVo;
import com.yanhuo.xo.vo.TrendVo;
import com.yanhuo.xo.vo.UserRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowDao, Follow> implements FollowService {

    @Autowired
    UserService userService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    SendMessageMq sendMessageMq;

    //TODO 直接在数据库中关联查询，暂无其他好的方法
    @Override
    public List<TrendVo> getAllFollowTrends(long page, long limit, String uid) {
        return baseMapper.getAllFollowTrends(page, limit, uid);
    }

    @Override
    public Page<FollowVo> getAllFriend(long page, long limit, String uid, Integer type) {
        Page<FollowVo> followVoPage = new Page<>();
        Page<Follow> followPage;
        List<Long> uids;

        //0查找所有的粉丝
        if (type == 0) {
            followPage = this.page(new Page<>(page, limit), new QueryWrapper<Follow>().eq("fid", uid).orderByDesc("create_date"));
        } else {
            followPage = this.page(new Page<>(page, limit), new QueryWrapper<Follow>().eq("uid", uid).orderByDesc("create_date"));
        }

        List<Follow> followList = followPage.getRecords();
        long total = followPage.getTotal();

        if (type == 0) {
            uids = followList.stream().map(Follow::getUid).collect(Collectors.toList());
        } else {
            uids = followList.stream().map(Follow::getFid).collect(Collectors.toList());
        }

        List<User> userList = userService.listByIds(uids);
        HashMap<Long, User> userMap = new HashMap<>();
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        //得到当前用户所有的关注
        List<Follow> followCurrentUserList = this.list(new QueryWrapper<Follow>().eq("uid", uid));
        List<Long> followIds = followCurrentUserList.stream().map(Follow::getFid).collect(Collectors.toList());

        FollowVo followVo;
        User user;
        List<FollowVo> followVoList = new ArrayList<>();
        for (Follow model : followList) {
            followVo = new FollowVo();
            //查找粉丝
            if (type == 0) {
                user = userMap.get(model.getUid());
            } else {
                user = userMap.get(model.getFid());
            }

            followVo.setIsfollow(followIds.contains(model.getUid()))
                    .setUid(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setFanCount(user.getFanCount())
                    .setUserId(user.getUserId())
                    .setTime(model.getCreateDate());
            followVoList.add(followVo);
        }

        followVoPage.setRecords(followVoList);
        followPage.setTotal(total);
        return followVoPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void followUser(FollowDTO followDTO) {
        if (followDTO.getUid().equals(followDTO.getFid())) {
            return;
        }

        Follow follow = new Follow();
        follow.setFid(followDTO.getFid());
        follow.setUid(followDTO.getUid());

        this.save(follow);


        User currentUser = userService.getById(followDTO.getUid());
        currentUser.setFollowCount(currentUser.getFollowCount() + 1);
        sendMessageMq.sendMessage(PlatformMqConstant.USER_STATE_EXCHANGE, PlatformMqConstant.USER_STATE_KEY, currentUser);

        User followUser = userService.getById(followDTO.getFid());
        followUser.setFanCount(followUser.getFanCount() + 1);
        sendMessageMq.sendMessage(PlatformMqConstant.USER_STATE_EXCHANGE, PlatformMqConstant.USER_STATE_KEY, followUser);


        //更改用户记录表
        String followerKey = PlatformConstant.USER_RECORD + followDTO.getFid();
        UserRecordVo follower;
        if (Boolean.TRUE.equals(redisUtils.hasKey(followerKey))) {
            follower = JsonUtils.parseObject(redisUtils.get(followerKey), UserRecordVo.class);
            follower.setAddFollowCount(follower.getAddFollowCount() + 1);
            redisUtils.set(followerKey, JSON.toJSONString(follower));

            try {
                WebSocketServer.sendMessageTo(JSON.toJSONString(follower), String.valueOf(followDTO.getFid()));
            } catch (Exception e) {
                throw new YanHuoException(Constant.MSG_ERROR);
            }
        }
    }

    @Override
    public boolean isFollow(String uid, String fid) {
        long count = this.count(new QueryWrapper<Follow>().and(e -> e.eq("uid", uid).eq("fid", fid)));
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearFollow(FollowDTO followDTO) {

        if (followDTO.getUid().equals(followDTO.getFid())) {
            return;
        }
        User currentUser = userService.getById(followDTO.getUid());
        currentUser.setFollowCount(currentUser.getFollowCount() - 1);
        sendMessageMq.sendMessage(PlatformMqConstant.USER_STATE_EXCHANGE, PlatformMqConstant.USER_STATE_KEY, currentUser);


        User follower = userService.getById(followDTO.getFid());
        follower.setFanCount(follower.getFanCount() - 1);
        sendMessageMq.sendMessage(PlatformMqConstant.USER_STATE_EXCHANGE, PlatformMqConstant.USER_STATE_KEY, follower);


        this.remove(new QueryWrapper<Follow>().and(e -> e.eq("uid", followDTO.getUid()).eq("fid", followDTO.getFid())));
    }
}