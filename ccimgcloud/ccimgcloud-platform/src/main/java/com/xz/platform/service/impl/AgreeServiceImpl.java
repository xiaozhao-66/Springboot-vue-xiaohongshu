package com.xz.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.exception.RenException;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.DateUtils;
import com.xz.common.utils.PageUtils;
import com.xz.common.constant.cacheConstant.ImgDetailCacheNames;
import com.xz.platform.common.constant.Constant;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.dao.*;
import com.xz.platform.dto.AgreeDTO;
import com.xz.platform.entity.*;
import com.xz.platform.service.AgreeService;
import com.xz.platform.vo.AgreeVo;
import com.xz.platform.vo.ImgDetailInfoVo;
import com.xz.platform.websocket.WebSocketServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class AgreeServiceImpl extends CrudServiceImpl<AgreeDao, AgreeEntity, AgreeDTO> implements AgreeService {


    @Autowired
    ImgDetailsDao imgDetailsDao;

    @Autowired
    CommentDao commentDao;

    @Autowired
    AlbumImgRelationDao albumImgRelationDao;

    @Autowired
    AgreeDao agreeDao;

    @Autowired
    AlbumDao albumDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserRecordDao userRecordDao;

    @Autowired
    RedisUtils redisUtils;


    @Override
    public QueryWrapper<AgreeEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        QueryWrapper<AgreeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        return wrapper;
    }

    @Override
    public boolean isAgree(AgreeDTO agreeDTO) {
        AgreeEntity agreeEntity = baseDao.selectOne(new QueryWrapper<AgreeEntity>().and(e -> e.eq("uid", agreeDTO.getUid()).eq("agree_id", agreeDTO.getAgreeId()).eq("type", agreeDTO.getType())));
        return agreeEntity != null;
    }

    /**
     * 点赞评论或者是图片
     *
     * @param agreeDTO
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void agree(AgreeDTO agreeDTO) {

        String key = ImgDetailCacheNames.IMG_DETAIL+ agreeDTO.getAgreeId();

        if (agreeDTO.getType() == 1 && Boolean.TRUE.equals(redisUtils.hasKey(key))) {
            String strObject = redisUtils.get(key);
            ImgDetailInfoVo imgDetailInfoVo = JSON.parseObject(strObject, ImgDetailInfoVo.class);
            imgDetailInfoVo.setAgreeCount(imgDetailInfoVo.getAgreeCount() + 1);
            redisUtils.setEx(key, JSON.toJSONString(imgDetailInfoVo), 60, TimeUnit.SECONDS);
        }

        AgreeEntity agreeEntity = ConvertUtils.sourceToTarget(agreeDTO, AgreeEntity.class);

        if (agreeDTO.getType() == 1) {
            //点赞图片，点赞次数++
            ImgDetailsEntity imgEntity = imgDetailsDao.selectById(agreeDTO.getAgreeId());
            imgEntity.setAgreeCount(imgEntity.getAgreeCount() + 1);
            imgDetailsDao.updateById(imgEntity);
        } else {
            //点赞评论
            CommentEntity commentEntity = commentDao.selectById(agreeDTO.getAgreeId());
            commentEntity.setCount(commentEntity.getCount() + 1);
            commentDao.updateById(commentEntity);
        }

        baseDao.insert(agreeEntity);

        //更改用户记录表
        UserRecordEntity userRecordEntity = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", agreeDTO.getAgreeUid()));
        userRecordEntity.setAgreeCollectionCount(userRecordEntity.getAgreeCollectionCount() + 1);
        userRecordDao.updateById(userRecordEntity);

        //如果当前点赞的用户是本用户不需要通知
        if (!agreeDTO.getUid().equals(agreeDTO.getAgreeUid())) {
            try {
                WebSocketServer.sendMessageTo(JSON.toJSONString(userRecordEntity), String.valueOf(agreeDTO.getAgreeUid()));
            } catch (Exception e) {
                throw new RenException(Constant.MSG_ERROR);
            }
        }

    }

    /**
     * 得到所有的赞和收藏
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @Override
    public Page<AgreeVo> getAllAgreeAndCollection(long page, long limit, String uid) {

        List<AgreeEntity> agreeList = agreeDao.selectList(new QueryWrapper<AgreeEntity>().eq("agree_uid", uid));

        List<AgreeVo> agreeVoList = new ArrayList<>();

        UserEntity userEntity = null;
        CommentEntity commentEntity = null;
        ImgDetailsEntity imgDetailsEntity = null;
        AgreeVo agreeVo = null;

        //得到所有的图片赞和评论赞
        for (AgreeEntity model : agreeList) {

            if (String.valueOf(model.getUid()).equals(uid)) {
                continue;
            }

            agreeVo = new AgreeVo();
            userEntity = userDao.selectById(model.getUid());
            agreeVo.setAvatar(userEntity.getAvatar())
                    .setUsername(userEntity.getUsername())
                    .setUid(userEntity.getId())
                    .setCreateDate(model.getCreateDate())
                    .setTime(DateUtils.timeUtile(model.getCreateDate()));
            //点赞的是图片
            if (model.getType() == 1) {
                imgDetailsEntity = imgDetailsDao.selectById(model.getAgreeId());
                agreeVo.setType(1)
                        .setCover(imgDetailsEntity.getCover())
                        .setMid(imgDetailsEntity.getId());

            } else {
                commentEntity = commentDao.selectById(model.getAgreeId());
                imgDetailsEntity = imgDetailsDao.selectById(commentEntity.getMid());

                agreeVo.setType(0)
                        .setCover(imgDetailsEntity.getCover())
                        .setMid(imgDetailsEntity.getId())
                        .setContent(commentEntity.getContent());
            }

            agreeVoList.add(agreeVo);
        }


        //得到当前用户发布的图片
        List<ImgDetailsEntity> imgDetailsEntityList = imgDetailsDao.selectList(new QueryWrapper<ImgDetailsEntity>().eq("user_id", uid));
        AlbumEntity albumEntity = null;

        for (ImgDetailsEntity model : imgDetailsEntityList) {

            //当前图片被哪些专辑收藏
            List<AlbumImgRelationEntity> albumImgRelationList = albumImgRelationDao.selectList(new QueryWrapper<AlbumImgRelationEntity>().eq("mid", model.getId()));

            for (AlbumImgRelationEntity albumImgRelationElement : albumImgRelationList) {
                albumEntity = albumDao.selectById(albumImgRelationElement.getAid());

                //表示被他人给收藏
                if (!String.valueOf(albumEntity.getUid()).equals(uid)) {
                    userEntity = userDao.selectById(albumEntity.getUid());
                    agreeVo = new AgreeVo();
                    agreeVo.setType(2)
                            .setAvatar(userEntity.getAvatar())
                            .setUsername(userEntity.getUsername())
                            .setUid(userEntity.getId())
                            .setCreateDate(albumImgRelationElement.getCreateDate())
                            .setTime(DateUtils.timeUtile(albumImgRelationElement.getCreateDate()))
                            .setCover(model.getCover())
                            .setMid(model.getId());
                    agreeVoList.add(agreeVo);
                }
            }

        }

        agreeVoList.sort((o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));

        return PageUtils.getPages((int) page, (int) limit, agreeVoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAgree(AgreeDTO agreeDTO) {

        String key = ImgDetailCacheNames.IMG_DETAIL + agreeDTO.getAgreeId();

        if (agreeDTO.getType() == 1 && Boolean.TRUE.equals(redisUtils.hasKey(key))) {
            String strObject = redisUtils.get(key);
            ImgDetailInfoVo imgDetailInfoVo = JSON.parseObject(strObject, ImgDetailInfoVo.class);
            imgDetailInfoVo.setAgreeCount(imgDetailInfoVo.getAgreeCount() - 1);
            redisUtils.setEx(key, JSON.toJSONString(imgDetailInfoVo), 60, TimeUnit.SECONDS);
        }


        if (agreeDTO.getType() == 1) {
            //点赞图片，点赞次数--
            ImgDetailsEntity imgEntity = imgDetailsDao.selectById(agreeDTO.getAgreeId());
            imgEntity.setAgreeCount(imgEntity.getAgreeCount() - 1);
            imgDetailsDao.updateById(imgEntity);
        } else {
            //点赞评论
            CommentEntity commentEntity = commentDao.selectById(agreeDTO.getAgreeId());
            commentEntity.setCount(commentEntity.getCount() - 1);
            commentDao.updateById(commentEntity);
        }

        agreeDao.delete(new QueryWrapper<AgreeEntity>().and(e -> e.eq("uid", agreeDTO.getUid()).eq("agree_id", agreeDTO.getAgreeId()).eq("agree_uid", agreeDTO.getAgreeUid())));

        //更改用户记录表
        UserRecordEntity userRecordEntity = userRecordDao.selectOne(new QueryWrapper<UserRecordEntity>().eq("uid", agreeDTO.getAgreeUid()));
        userRecordEntity.setAgreeCollectionCount(userRecordEntity.getAgreeCollectionCount() - 1);
        userRecordDao.updateById(userRecordEntity);

    }
}