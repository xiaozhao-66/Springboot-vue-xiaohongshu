package com.xz.platform.service.impl;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.exception.RenException;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.DateUtils;
import com.xz.common.utils.PageUtils;
import com.xz.platform.common.constant.Constant;
import com.xz.platform.dao.*;
import com.xz.platform.dto.FollowDTO;
import com.xz.platform.entity.*;
import com.xz.platform.service.FollowService;
import com.xz.platform.vo.FollowTrendVo;
import com.xz.platform.vo.FollowVo;
import com.xz.platform.websocket.WebSocketServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class FollowServiceImpl extends CrudServiceImpl<FollowDao, FollowEntity, FollowDTO> implements FollowService {

    @Autowired
    ImgDetailsDao imgDetailsDao;

    @Autowired
    AlbumDao albumDao;

    @Autowired
    AlbumImgRelationDao albumImgRelationDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserRecordDao userRecordDao;

    @Autowired
    AgreeDao agreeDao;

    @Override
    public QueryWrapper<FollowEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<FollowEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<FollowTrendVo> getAllFollowTrends(long page, long limit, String userId) {

        List<FollowTrendVo> res = new ArrayList<>();

        List<String> ids = new ArrayList<>();
        ids.add(userId);
        //查询当前用户关注的所有用户
        List<FollowEntity> follows = baseDao.selectList(new QueryWrapper<FollowEntity>().eq("uid", userId));

        ids.addAll(follows.stream().map(map -> String.valueOf(map.getFid())).collect(Collectors.toList()));
        //批量查找

        Map<String, Object> map = new HashMap<>(4);
        map.put("idList", ids);
        //map.put("status",1);
        map.put("page", page);
        map.put("limit", limit);
        List<ImgDetailsEntity> imgDetailList = imgDetailsDao.selectBatch(map);
        FollowTrendVo followTrendVo = null;
        UserEntity userEntity = null;
        AlbumEntity albumEntity = null;
        for (ImgDetailsEntity model : imgDetailList) {
            userEntity = userDao.selectById(model.getUserId());
            AgreeEntity agreeEntity = agreeDao.selectOne(new QueryWrapper<AgreeEntity>().and(e -> e.eq("uid", userId).eq("agree_id", model.getId()).eq("type", 1)));
            List<String> imgList = JSON.parseArray(model.getImgsUrl(), String.class);

            followTrendVo = ConvertUtils.sourceToTarget(model, FollowTrendVo.class);
            followTrendVo.setMid(model.getId())
                    .setIsAgree(agreeEntity != null)
                    .setUserId(userEntity.getId())
                    .setUsername(userEntity.getUsername())
                    .setAvatar(userEntity.getAvatar())
                    .setImgsUrl(imgList)
                    .setTime(DateUtils.timeUtile(model.getUpdateDate()));

            List<AlbumImgRelationEntity> albumImgRelationList = albumImgRelationDao.selectList(new QueryWrapper<AlbumImgRelationEntity>().eq("mid", model.getId()));

            for (AlbumImgRelationEntity element : albumImgRelationList) {
                albumEntity = albumDao.selectById(element.getAid());
                if (albumEntity.getUid().equals(model.getUserId())) {
                    followTrendVo.setAlbumId(albumEntity.getId());
                    followTrendVo.setAlbumName(albumEntity.getName());
                    break;
                }
            }

            res.add(followTrendVo);

        }
        return res;
    }

    @Override
    public boolean isFollow(String uid, String fid) {

        FollowEntity followEntity = baseDao.selectOne(new QueryWrapper<FollowEntity>().and(e -> e.eq("uid", uid).eq("fid", fid)));
        return followEntity != null;
    }


    @Override
    public Page<FollowVo> getAllFanUser(long page, long limit, String uid) {

        List<FollowVo> list = new ArrayList<>();
        List<FollowEntity> fanList = baseDao.selectList(new QueryWrapper<FollowEntity>().eq("fid", uid).orderByDesc("create_date"));
        FollowVo followVo = null;
        UserEntity user = null;
        for (FollowEntity model : fanList) {

            user = userDao.selectById(model.getUid());
            boolean follow = isFollow(uid, String.valueOf(user.getId()));

            followVo = new FollowVo();

            followVo.setIsfollow(follow)
                    .setUid(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setTime(DateUtils.timeUtile(model.getCreateDate()));

            list.add(followVo);
        }

        return PageUtils.getPages((int) page, (int) limit, list);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void followUser(FollowDTO followDTO) {
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
        List<FollowEntity> followList = null;

        //0查找所有的粉丝
        if (type == 0) {
            followList = baseDao.selectList(new QueryWrapper<FollowEntity>().eq("fid", uid).orderByDesc("create_date"));
        } else {
            followList = baseDao.selectList(new QueryWrapper<FollowEntity>().eq("uid", uid).orderByDesc("create_date"));
        }

        FollowVo followVo = null;
        UserEntity user = null;
        UserRecordEntity userRecordEntity = null;
        for (FollowEntity model : followList) {
            followVo = new FollowVo();
            if (type == 0) {
                user = userDao.selectById(model.getUid());
            } else {
                user = userDao.selectById(model.getFid());
            }
            boolean follow = isFollow(uid, String.valueOf(user.getId()));
            userRecordEntity = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", user.getId()));

            followVo.setIsfollow(follow)
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
        UserRecordEntity currentUser = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", followDTO.getUid()));
        currentUser.setFollowCount(currentUser.getFollowCount() - 1);

        UserRecordEntity follower = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", followDTO.getFid()));
        follower.setFanCount(follower.getFanCount() - 1);
        userRecordDao.updateById(currentUser);
        userRecordDao.updateById(follower);
        baseDao.delete(new QueryWrapper<FollowEntity>().and(e -> e.eq("uid", followDTO.getUid()).eq("fid", followDTO.getFid())));

    }
}