package com.xz.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.entity.BaseEntity;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.DateUtils;
import com.xz.common.utils.PageUtils;
import com.xz.platform.dao.*;
import com.xz.platform.dto.UserDTO;
import com.xz.platform.entity.*;
import com.xz.platform.service.UserService;
import com.xz.platform.vo.FollowVo;
import com.xz.platform.vo.TrendVo;
import com.xz.platform.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
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
public class UserServiceImpl extends CrudServiceImpl<UserDao, UserEntity, UserDTO> implements UserService {

    @Autowired
    ImgDetailsDao imgDetailsDao;

    @Autowired
    AlbumDao albumDao;

    @Autowired
    AlbumImgRelationDao albumImgRelationDao;

    @Autowired
    UserRecordDao userRecordDao;

    @Autowired
    UserDao userDao;

    @Autowired
    FollowDao followDao;

    @Override
    public QueryWrapper<UserEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public Page<TrendVo> getTrendByUser(long page, long limit, String userId) {

        List<TrendVo> res = new ArrayList<>();

        List<ImgDetailsEntity> imgDetailList = imgDetailsDao.selectList(new QueryWrapper<ImgDetailsEntity>().eq("user_id", userId).orderByDesc("update_date"));
        TrendVo trendVo = null;
        List<String> imgList = null;
        List<AlbumEntity> albumList = null;
        List<AlbumImgRelationEntity> albumImgRelationList = null;
        //分页
        for (ImgDetailsEntity model : imgDetailList) {

            imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            trendVo = ConvertUtils.sourceToTarget(model, TrendVo.class);
            trendVo.setImgsUrl(imgList)
                    .setMid(model.getId())
                    .setTime(DateUtils.timeUtile(model.getUpdateDate()));

            //得到专辑
            albumList = albumDao.selectList(new QueryWrapper<AlbumEntity>().eq("uid", userId));

            for (AlbumEntity element : albumList) {
                albumImgRelationList = albumImgRelationDao.selectList(new QueryWrapper<AlbumImgRelationEntity>().eq("aid", element.getId()));
                for (AlbumImgRelationEntity e : albumImgRelationList) {
                    if (StringUtils.equals(String.valueOf(e.getMid()), String.valueOf(model.getId()))) {
                        trendVo.setAlbumId(element.getId());
                        trendVo.setAlbumName(element.getName());
                        break;
                    }
                }
            }

            res.add(trendVo);
        }

        return PageUtils.getPages((int) page, (int) limit, res);
    }

    @Override
    public UserVo getUserInfo(String uid) {
        UserEntity userEntity = baseDao.selectById(uid);
        UserRecordEntity userRecordEntity = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", uid));
        UserVo userVo = ConvertUtils.sourceToTarget(userEntity, UserVo.class);
        userVo.setTrendCount(userRecordEntity.getTrendCount())
                .setFollowCount(userRecordEntity.getFollowCount())
                .setFanCount(userRecordEntity.getFanCount())
                .setNoreplyCount(userRecordEntity.getNoreplyCount())
                .setAgreeCollectionCount(userRecordEntity.getAgreeCollectionCount());
        return userVo;
    }

    //查找当前用户并查看是否关注
    @Override
    public Page<FollowVo> searchUser(long page, long limit, String keyword, String uid) {

        List<FollowVo> list = new ArrayList<>();

        //得到所有搜索的用户
        List<UserEntity> userList = baseDao.selectList(new QueryWrapper<UserEntity>().like("user_id", keyword).or().like("username", keyword));


        //得到当前用户所有关注的用户
        List<FollowEntity> followList = followDao.selectList(new QueryWrapper<FollowEntity>().eq("uid", uid).orderByDesc("create_date"));

        //所有关注者
        List<Long> ids = followList.stream().map(FollowEntity::getFid).collect(Collectors.toList());

        UserRecordEntity userRecordEntity = null;

        FollowVo followVo = null;
        for (UserEntity user : userList) {

            followVo = new FollowVo();

            userRecordEntity = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", user.getId()));

            followVo.setIsfollow(ids.contains(user.getId()))
                    .setUid(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setFanCount(userRecordEntity.getFanCount())
                    .setUserId(user.getUserId());

            list.add(followVo);
        }


        return PageUtils.getPages((int) page, (int) limit, list);

    }

    @Override
    public UserEntity updateUser(UserEntity userEntity) {
        userDao.updateById(userEntity);
        return userEntity;
    }

    @Override
    public UserVo searchUserByUsername(String username) {
        UserEntity userEntity = baseDao.selectOne(new QueryWrapper<UserEntity>().eq("username", username));
        if(userEntity==null){
            return null;
        }
        UserVo userVo = ConvertUtils.sourceToTarget(userEntity, UserVo.class);
        UserRecordEntity userRecordEntity = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", userEntity.getId()));
        userVo.setFanCount(userRecordEntity.getFanCount());
        return userVo;
    }
}