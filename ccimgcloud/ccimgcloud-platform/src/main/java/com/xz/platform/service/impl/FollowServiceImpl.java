package com.xz.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.exception.RenException;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.utils.DateUtils;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.common.constant.Constant;
import com.xz.platform.dao.*;
import com.xz.platform.dto.FollowDTO;
import com.xz.platform.entity.*;
import com.xz.platform.service.FollowService;
import com.xz.platform.vo.FollowTrendVo;
import com.xz.platform.vo.FollowVo;;
import com.xz.platform.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class FollowServiceImpl extends BaseServiceImpl<FollowDao, FollowEntity> implements FollowService {


    @Autowired
    UserDao userDao;

    @Autowired
    UserRecordDao userRecordDao;

    @Autowired
    RedisUtils redisUtils;


    /**
     * TODO 优化缓存机制
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @Override
    public List<FollowTrendVo> getAllFollowTrends(long page, long limit, String uid) {

        return baseDao.getAllFollowTrends(page, limit, uid);

//            旧方法(垃圾代码)
//            List<FollowTrendVo> res = new ArrayList<>();
//
//            List<String> ids = new ArrayList<>();
//            ids.add(userId);
//
//            //查询当前用户关注的所有用户
//            List<FollowEntity> follows = baseDao.selectList(new QueryWrapper<FollowEntity>().eq("uid", userId));
//
//            ids.addAll(follows.stream().map(map -> String.valueOf(map.getFid())).collect(Collectors.toList()));
//            //批量查找
//
//            Map<String, Object> map = new HashMap<>(4);
//            map.put("idList", ids);
//            map.put("page", page);
//            map.put("limit", limit);
//
//            List<ImgDetailsEntity> imgDetailList = imgDetailsDao.selectBatch(map);
//
//
//            FollowTrendVo followTrendVo = null;
//            UserEntity userEntity = null;
//            AlbumEntity albumEntity = null;
//
//
//            for (ImgDetailsEntity model : imgDetailList) {
//
//                userEntity = userDao.selectById(model.getUserId());
//                AgreeEntity agreeEntity = agreeDao.selectOne(new QueryWrapper<AgreeEntity>().and(e -> e.eq("uid", userId).eq("agree_id", model.getId()).eq("type", 1)));
//
//                List<String> imgList = JSON.parseArray(model.getImgsUrl(), String.class);
//
//
//                followTrendVo = ConvertUtils.sourceToTarget(model, FollowTrendVo.class);
//                followTrendVo.setMid(model.getId())
//                        .setIsAgree(agreeEntity != null)
//                        .setUserId(userEntity.getId())
//                        .setUsername(userEntity.getUsername())
//                        .setAvatar(userEntity.getAvatar())
//                        .setImgsUrl(imgList)
//                        .setTime(DateUtils.timeUtile(model.getUpdateDate()));
//
//
//
//                List<AlbumImgRelationEntity> albumImgRelationList = albumImgRelationDao.selectList(new QueryWrapper<AlbumImgRelationEntity>().eq("mid", model.getId()));
//
//                for (AlbumImgRelationEntity element : albumImgRelationList) {
//                    albumEntity = albumDao.selectById(element.getAid());
//                    if (albumEntity.getUid().equals(model.getUserId())) {
//                        followTrendVo.setAlbumId(albumEntity.getId());
//                        followTrendVo.setAlbumName(albumEntity.getName());
//                        break;
//                    }
//                }
//
//                res.add(followTrendVo);
//            }
//
// 设置缓存失效过期时间为5分钟


    }

    @Override
    public boolean isFollow(String uid, String fid) {

        Long count = baseDao.selectCount(new QueryWrapper<FollowEntity>().and(e -> e.eq("uid", uid).eq("fid", fid)));
        return count > 0;
    }


    @Override
    public Page<FollowVo> getAllFanUser(long page, long limit, String uid) {
        Page<FollowVo> res = new Page<>();

        //得到所有粉丝
        Page<FollowEntity> fanPage = baseDao.selectPage(new Page<>(page, limit), new QueryWrapper<FollowEntity>().eq("fid", uid).orderByDesc("create_date"));

        List<FollowEntity> fanList = fanPage.getRecords();

        //所有粉丝的id
        List<Long> uids = fanList.stream().map(FollowEntity::getUid).collect(Collectors.toList());
        HashMap<Long, UserEntity> userMap = new HashMap<>();

        List<UserEntity> userList = userDao.selectBatchIds(uids);
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        //得到当前用户关注的所有用户
        List<FollowEntity> followList = baseDao.selectList(new QueryWrapper<FollowEntity>().eq("uid", uid));
        List<Long> fids = followList.stream().map(FollowEntity::getFid).collect(Collectors.toList());

        FollowVo followVo;
        UserEntity user;

        List<FollowVo> list = new ArrayList<>();

        for (FollowEntity model : fanList) {
            user = userMap.get(model.getUid());

            followVo = new FollowVo();

            followVo.setIsfollow(fids.contains(model.getUid()))
                    .setUid(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setTime(DateUtils.timeUtile(model.getCreateDate()));

            list.add(followVo);
        }

        res.setRecords(list);
        res.setTotal(fanPage.getTotal());

        return res;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void followUser(FollowDTO followDTO) {

        if (followDTO.getUid().equals(followDTO.getFid())) {
            return;
        }

        //TODO 使用redis缓存优化
//        String followKey = "follow:"+followDTO.getUid();
//        if(Boolean.TRUE.equals(redisUtils.hasKey(followKey))){
//            //存储所有用户点赞信息
//            List<Long> uids = JSON.parseArray(redisUtils.get(followKey), Long.class);
//            uids.add(followDTO.getFid());
//            redisUtils.set(followKey,JSON.toJSONString(uids));
//        }else{
//            List<Long> uids = new ArrayList<>();
//            uids.add(followDTO.getFid());
//            redisUtils.set(followKey,JSON.toJSONString(uids));
//        }


        FollowEntity followEntity = new FollowEntity();
        followEntity.setFid(followDTO.getFid());
        followEntity.setUid(followDTO.getUid());

        baseDao.insert(followEntity);

        UserRecordEntity currentUser = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", followDTO.getUid()));
        currentUser.setFollowCount(currentUser.getFollowCount() + 1);
        userRecordDao.updateById(currentUser);

        UserRecordEntity follower = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", followDTO.getFid()));
        follower.setFanCount(follower.getFanCount() + 1);
        follower.setAddFollowCount(follower.getAddFollowCount() + 1);
        userRecordDao.updateById(follower);


        try {
            WebSocketServer.sendMessageTo(JSON.toJSONString(follower), String.valueOf(followDTO.getFid()));
        } catch (Exception e) {
            throw new RenException(Constant.MSG_ERROR);
        }
    }

    @Override
    public List<FollowVo> getAllFriend(String uid, Integer type) {

        List<FollowVo> list = new ArrayList<>();
        List<FollowEntity> followList;
        List<Long> uids;

        //0查找所有的粉丝
        if (type == 0) {
            followList = baseDao.selectList(new QueryWrapper<FollowEntity>().eq("fid", uid).orderByDesc("create_date"));
            //所有粉丝的id
            uids = followList.stream().map(FollowEntity::getUid).collect(Collectors.toList());

        } else {
            followList = baseDao.selectList(new QueryWrapper<FollowEntity>().eq("uid", uid).orderByDesc("create_date"));
            uids = followList.stream().map(FollowEntity::getFid).collect(Collectors.toList());
        }

        List<UserEntity> userList = userDao.selectBatchIds(uids);

        List<UserRecordEntity> userRecordList = userRecordDao.selectBatchByUid(uids);

        HashMap<Long, UserEntity> userMap = new HashMap<>();
        HashMap<Long, UserRecordEntity> userRecordMap = new HashMap<>();
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });
        userRecordList.forEach(item -> {
            userRecordMap.put(item.getUid(), item);
        });

        //得到当前用户所有的关注
        List<FollowEntity> followCurrentUserList = baseDao.selectList(new QueryWrapper<FollowEntity>().eq("uid", uid));

        List<Long> followIds = followCurrentUserList.stream().map(FollowEntity::getFid).collect(Collectors.toList());

        FollowVo followVo;
        UserEntity user;
        UserRecordEntity userRecordEntity;
        for (FollowEntity model : followList) {
            followVo = new FollowVo();
            //查找粉丝
            if (type == 0) {
                user = userMap.get(model.getUid());
            } else {
                user = userMap.get(model.getFid());
            }

            userRecordEntity = userRecordMap.get(user.getId());


            followVo.setIsfollow(followIds.contains(model.getUid()))
                    .setUid(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setFanCount(userRecordEntity.getFanCount())
                    .setUserId(user.getUserId())
                    .setTime(DateUtils.timeUtile(model.getCreateDate()));
            list.add(followVo);
        }


        return list;
    }

    @Override
    public void clearFollow(FollowDTO followDTO) {

        if (followDTO.getUid().equals(followDTO.getFid())) {
            return;
        }

        UserRecordEntity currentUser = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", followDTO.getUid()));
        currentUser.setFollowCount(currentUser.getFollowCount() - 1);

        UserRecordEntity follower = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", followDTO.getFid()));
        follower.setFanCount(follower.getFanCount() - 1);
        userRecordDao.updateById(currentUser);
        userRecordDao.updateById(follower);
        baseDao.delete(new QueryWrapper<FollowEntity>().and(e -> e.eq("uid", followDTO.getUid()).eq("fid", followDTO.getFid())));

    }
}