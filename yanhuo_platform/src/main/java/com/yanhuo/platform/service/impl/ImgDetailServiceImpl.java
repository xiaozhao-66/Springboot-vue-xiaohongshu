package com.yanhuo.platform.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.constant.Constant;
import com.yanhuo.common.constant.platform.PlatformConstant;
import com.yanhuo.common.exception.YanHuoException;
import com.yanhuo.common.result.ResultCodeEnum;
import com.yanhuo.common.utils.*;
import com.yanhuo.platform.client.EsClient;
import com.yanhuo.platform.client.OSSClient;
import com.yanhuo.platform.dao.ImgDetailDao;
import com.yanhuo.platform.service.*;
import com.yanhuo.xo.dto.platform.AlbumImgRelationDTO;
import com.yanhuo.xo.dto.platform.BrowseRecordDTO;
import com.yanhuo.xo.dto.platform.ImgDetailDTO;
import com.yanhuo.xo.model.*;
import com.yanhuo.xo.vo.ImgDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Service
public class ImgDetailServiceImpl extends ServiceImpl<ImgDetailDao, ImgDetail> implements ImgDetailService {


    @Autowired
    UserService userService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    CategoryService categoryService;

    @Autowired
    TagImgRelationService tagImgRelationService;

    @Autowired
    TagService tagService;

    @Autowired
    AlbumService albumService;

    @Autowired
    AlbumImgRelationService albumImgRelationService;

    @Autowired
    AgreeCollectService agreeCollectService;

    @Autowired
    EsClient esClient;

    @Autowired
    OSSClient ossClient;


    @Override
    public Page<ImgDetailVo> getPage(long page, long limit) {

        Page<ImgDetail> imgDetailPage = this.page(new Page<>(page, limit), new QueryWrapper<ImgDetail>().eq("status", 1).orderByDesc("update_date"));

        Page<ImgDetailVo> imgDetailVoPage = new Page<>();

        List<ImgDetail> imgDetailList = imgDetailPage.getRecords();
        long total = imgDetailPage.getTotal();
        List<Long> uids = imgDetailList.stream().map(ImgDetail::getUserId).collect(Collectors.toList());

        Map<Long, User> userMap = new HashMap<>();
        List<User> userList = userService.listByIds(uids);
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        List<ImgDetailVo> imgDetailVoList = new ArrayList<>();

        ImgDetailVo imgDetailVo;
        User user;
        for (ImgDetail model : imgDetailPage.getRecords()) {
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            List<String> imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            if (!imgList.isEmpty()) {
                imgDetailVo.setImgsUrl(imgList);
            }
            user = userMap.get(model.getUserId());
            imgDetailVo.setUserId(user.getId());
            imgDetailVo.setUsername(user.getUsername());
            imgDetailVo.setAvatar(user.getAvatar());
            imgDetailVoList.add(imgDetailVo);
        }
        imgDetailVoPage.setRecords(imgDetailVoList);
        imgDetailVoPage.setTotal(total);
        return imgDetailVoPage;
    }

    @Override
    public Page<ImgDetailVo> getAllImgByAlbum(long page, long limit, String albumId, Integer type) {

        Page<AlbumImgRelation> albumImgRelationPage = albumImgRelationService.page(new Page<>(page, limit), new QueryWrapper<AlbumImgRelation>().eq("aid", albumId).orderByDesc("create_date"));

        long total = albumImgRelationPage.getTotal();
        List<AlbumImgRelation> albumImgRelationList = albumImgRelationPage.getRecords();

        List<Long> mids = albumImgRelationList.stream().map(AlbumImgRelation::getMid).collect(Collectors.toList());

        List<ImgDetail> imgDetailList;
        if (type == 0) {
            imgDetailList = this.list(new QueryWrapper<ImgDetail>().in("id", mids).eq("status", 1));
        } else {
            imgDetailList = this.listByIds(mids);
        }

        List<Long> uids = imgDetailList.stream().map(ImgDetail::getUserId).collect(Collectors.toList());
        List<User> userList = userService.listByIds(uids);
        HashMap<Long, ImgDetail> imgDetailMap = new HashMap<>();
        HashMap<Long, User> userMap = new HashMap<>();
        imgDetailList.forEach(item -> {
            imgDetailMap.put(item.getId(), item);
        });
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        Page<ImgDetailVo> resPage = new Page<>();

        List<ImgDetailVo> imgDetailVoList = new ArrayList<>();
        ImgDetailVo imgDetailVo;
        ImgDetail imgDetail;
        for (AlbumImgRelation model : albumImgRelationList) {
            imgDetail = imgDetailMap.get(model.getMid());
            User user = userMap.get(imgDetail.getUserId());
            imgDetailVo = ConvertUtils.sourceToTarget(imgDetail, ImgDetailVo.class);
            imgDetailVo.setUsername(user.getUsername())
                    .setAvatar(user.getAvatar());
            imgDetailVoList.add(imgDetailVo);
        }

        resPage.setRecords(imgDetailVoList);
        resPage.setTotal(total);
        return resPage;
    }

    @Override
    public ImgDetailVo getImgDetail(String id) {
        //视频浏览记录
        String viewRecordKey = PlatformConstant.IMG_VIEW_RECORD + id;

        if (Boolean.TRUE.equals(redisUtils.hasKey(viewRecordKey))) {
            int count = Integer.parseInt(redisUtils.get(viewRecordKey));
            redisUtils.set(viewRecordKey, String.valueOf(count + 1));
        } else {
            redisUtils.set(viewRecordKey, "1");
        }

        String imgInfoKey = PlatformConstant.IMG_INFO_KEY + id;
        String agreeImgKey = PlatformConstant.AGREE_IMG_KEY + id;
        String albumImgRelationKey = PlatformConstant.ALBUM_IMG_RELATION_KEY + id;

        if (Boolean.TRUE.equals(redisUtils.hasKey(imgInfoKey))) {
            ImgDetailVo imgDetailInfoVo = JSON.parseObject(redisUtils.get(imgInfoKey), ImgDetailVo.class);
            int viewCount = Integer.parseInt(redisUtils.get(viewRecordKey));
            int agreeCount = 0;
            int collectCount = 0;
            if (Boolean.TRUE.equals(redisUtils.hasKey(agreeImgKey))) {
                agreeCount = JSON.parseArray(redisUtils.get(agreeImgKey), Long.class).size();
            }
            if (Boolean.TRUE.equals(redisUtils.hasKey(albumImgRelationKey))) {
                collectCount = JSON.parseArray(redisUtils.get(albumImgRelationKey), Long.class).size();
            }

            imgDetailInfoVo.setViewCount((long) viewCount);
            imgDetailInfoVo.setAgreeCount((long) agreeCount);
            imgDetailInfoVo.setCollectionCount((long) collectCount);
            return imgDetailInfoVo;
        }

        ImgDetail imgDetail = this.getById(id);

        List<String> imgList = JSON.parseArray(imgDetail.getImgsUrl(), String.class);
        User user = userService.getById(imgDetail.getUserId());

        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(imgDetail.getCategoryId());
        categoryIds.add(imgDetail.getCategoryPid());
        List<Category> categoryList = categoryService.listByIds(categoryIds);
        HashMap<Integer, Category> categoryMap = new HashMap<>();

        categoryList.forEach(item -> {
            if (item.getPid() == 0) {
                categoryMap.put(0, item);
            } else {
                categoryMap.put(1, item);
            }
        });

        ImgDetailVo imgDetailVo = ConvertUtils.sourceToTarget(imgDetail, ImgDetailVo.class);

        imgDetailVo.setImgsUrl(imgList)
                .setCategoryId(categoryMap.get(1).getId())
                .setCategoryName(categoryMap.get(1).getName())
                .setCategoryPid(categoryMap.get(0).getId())
                .setCategoryPName(categoryMap.get(0).getName())
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setAvatar(user.getAvatar())
                .setTime(imgDetail.getUpdateDate());

        //得到当前图像的所有标签
        List<TagImgRelation> tagImgRelationList = tagImgRelationService.list(new QueryWrapper<TagImgRelation>().eq("mid", id));
        List<Long> tids = tagImgRelationList.stream().map(TagImgRelation::getTid).collect(Collectors.toList());

        if (!tids.isEmpty()) {
            List<Tag> tagList = tagService.listByIds(tids);
            imgDetailVo.setTagList(tagList);
        }

        //得到专辑
        List<Album> albumList = albumService.list(new QueryWrapper<Album>().eq("uid", user.getId()));
        //图片绑定多个专辑
        List<AlbumImgRelation> albumImgRelationList = albumImgRelationService.list(new QueryWrapper<AlbumImgRelation>().eq("mid", id));
        HashMap<Long, Album> albumMap = new HashMap<>();
        albumList.forEach(item -> {
            albumMap.put(item.getId(), item);
        });

        for (AlbumImgRelation item : albumImgRelationList) {
            if (albumMap.containsKey(item.getAid())) {
                Album album = albumMap.get(item.getAid());
                imgDetailVo.setAlbum(album);
                break;
            }
        }
        redisUtils.setEx(imgInfoKey, JSON.toJSONString(imgDetailVo), 5, TimeUnit.DAYS);
        return imgDetailVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long publish(ImgDetailDTO imgDetailsDTO) {
        ImgDetail imgDetail = ConvertUtils.sourceToTarget(imgDetailsDTO, ImgDetail.class);
        imgDetail.setCollectionCount(0L);
        imgDetail.setCommentCount(0L);
        imgDetail.setAgreeCount(0L);
        imgDetail.setViewCount(0L);
        this.save(imgDetail);

        AlbumImgRelation albumImgRelation = new AlbumImgRelation();
        albumImgRelation.setAid(imgDetailsDTO.getAlbumId());
        albumImgRelation.setMid(imgDetail.getId());
        albumImgRelation.setSort(0);
        albumImgRelationService.save(albumImgRelation);

        Album album = albumService.getById(imgDetailsDTO.getAlbumId());
        album.setImgCount(imgDetailsDTO.getCount() + album.getImgCount());
        albumService.updateById(album);


        List<TagImgRelation> tagImgRelationList = new ArrayList<>();
        for (Tag tag : imgDetailsDTO.getTags()) {
            long id = tagService.saveTagByName(tag.getName());
            TagImgRelation tagImgRelationEntity = new TagImgRelation();
            tagImgRelationEntity.setMid(imgDetail.getId());
            tagImgRelationEntity.setTid(id);
            tagImgRelationList.add(tagImgRelationEntity);
        }

        tagImgRelationService.saveBatch(tagImgRelationList);

        User user = userService.getById(imgDetail.getUserId());
        user.setTrendCount(user.getTrendCount() + 1);
        userService.updateById(user);

        ImgDetailVo imgDetailVo = ConvertUtils.sourceToTarget(imgDetail, ImgDetailVo.class);
        imgDetailVo.setUsername(user.getUsername())
                .setAvatar(user.getAvatar())
                .setOtherUserId(user.getUserId())
                .setTime(imgDetail.getUpdateDate());

        redisUtils.hPut(PlatformConstant.IMG_DETAIL_LIST_KEY, String.valueOf(imgDetail.getId()), JSON.toJSONString(imgDetail));

        try {
            esClient.addData(imgDetailVo);
        } catch (Exception e) {
            throw new YanHuoException(Constant.ES_ERROR);
        }

        return imgDetail.getId();
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateStatus(ImgDetailDTO imgDetailDTO) {
//
//        ImgDetail imgDetail = this.getById(imgDetailDTO.getId());
//
//        imgDetail.setCover(imgDetailDTO.getCover());
//        imgDetail.setImgsUrl(imgDetailDTO.getImgsUrl());
//        imgDetail.setStatus(imgDetailDTO.getStatus());
//
//        this.updateById(imgDetail);
//
//        ImgDetailVo imgDetailVo = ConvertUtils.sourceToTarget(imgDetail, ImgDetailVo.class);
//
//        User user = userService.getById(imgDetail.getUserId());
//        imgDetailVo.setUsername(user.getUsername())
//                .setAvatar(user.getAvatar())
//                .setOtherUserId(user.getUserId())
//                .setTime(imgDetail.getUpdateDate());
//
//        if (imgDetailDTO.getType() == 0) {
//
//            redisUtils.hPut(PlatformConstant.IMG_DETAIL_LIST_KEY, String.valueOf(imgDetail.getId()), JSON.toJSONString(imgDetail));
//
//            try {
//                esClient.addData(imgDetailVo);
//            } catch (Exception e) {
//                throw new YanHuoException(Constant.ES_ERROR);
//            }
//        } else {
//
//            String imgInfoKey = PlatformConstant.IMG_INFO_KEY + imgDetail.getId();
//            redisUtils.delete(imgInfoKey);
//
//            redisUtils.hDelete(PlatformConstant.IMG_DETAIL_LIST_KEY, String.valueOf(imgDetail.getId()));
//            redisUtils.hPut(PlatformConstant.IMG_DETAIL_LIST_KEY, String.valueOf(imgDetail.getId()), JSON.toJSONString(imgDetail));
//
//            try {
//                esClient.update(imgDetailVo);
//            } catch (Exception e) {
//                throw new YanHuoException(Constant.ES_ERROR);
//            }
//        }
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateImgDetail(ImgDetailDTO imgDetailDTO) {
        ImgDetail imgDetail = this.getById(imgDetailDTO.getId());
        imgDetail.setCategoryId(imgDetailDTO.getCategoryId());
        imgDetail.setCategoryPid(imgDetailDTO.getCategoryPid());
        imgDetail.setContent(imgDetailDTO.getContent());
        imgDetail.setCount(imgDetailDTO.getCount());
        imgDetail.setStatus(imgDetailDTO.getStatus());
        imgDetail.setCover(imgDetailDTO.getCover());
        imgDetail.setImgsUrl(imgDetailDTO.getImgsUrl());

        this.updateById(imgDetail);

        //删除原有的绑定关系
        if (imgDetailDTO.getAlbum() != null) {
            Album album = albumService.getById(imgDetailDTO.getAlbum().getId());
            album.setImgCount(album.getImgCount() - imgDetailDTO.getCount());
            albumService.updateById(album);
            AlbumImgRelationDTO albumImgRelationDTO = new AlbumImgRelationDTO();
            albumImgRelationDTO.setAid(imgDetailDTO.getAlbum().getId());
            albumImgRelationDTO.setMid(imgDetail.getId());
            albumImgRelationService.deleteAlbumImgRelation(albumImgRelationDTO);
        }

        Album album = albumService.getById(imgDetailDTO.getAlbumId());
        album.setImgCount(imgDetailDTO.getCount() + album.getImgCount());
        albumService.updateById(album);

        //插入新的绑定关系
        AlbumImgRelation albumImgRelation = new AlbumImgRelation();
        albumImgRelation.setAid(imgDetailDTO.getAlbumId());
        albumImgRelation.setMid(imgDetail.getId());
        albumImgRelation.setSort(0);
        albumImgRelationService.save(albumImgRelation);

        //修改标签
        tagImgRelationService.remove(new QueryWrapper<TagImgRelation>().eq("mid", imgDetail.getId()));
        List<TagImgRelation> tagImgRelationList = new ArrayList<>();

        //当前图片绑定了哪些标签
        for (Tag tag : imgDetailDTO.getTags()) {
            long id = tagService.saveTagByName(tag.getName());
            TagImgRelation tagImgRelationEntity = new TagImgRelation();
            tagImgRelationEntity.setMid(imgDetail.getId());
            tagImgRelationEntity.setTid(id);
            tagImgRelationList.add(tagImgRelationEntity);
        }
        tagImgRelationService.saveBatch(tagImgRelationList);

        ImgDetailVo imgDetailVo = ConvertUtils.sourceToTarget(imgDetail, ImgDetailVo.class);
        User user = userService.getById(imgDetail.getUserId());
        imgDetailVo.setUsername(user.getUsername())
                .setAvatar(user.getAvatar())
                .setOtherUserId(user.getUserId())
                .setTime(imgDetail.getUpdateDate());

        String imgInfoKey = PlatformConstant.IMG_INFO_KEY + imgDetail.getId();
        redisUtils.delete(imgInfoKey);

        redisUtils.hDelete(PlatformConstant.IMG_DETAIL_LIST_KEY, String.valueOf(imgDetail.getId()));
        redisUtils.hPut(PlatformConstant.IMG_DETAIL_LIST_KEY, String.valueOf(imgDetail.getId()), JSON.toJSONString(imgDetail));

        String recommendKey = PlatformConstant.RECOMMEND + imgDetail.getId();

        if (Boolean.TRUE.equals(redisUtils.hasKey(recommendKey))) {
            redisUtils.delete(recommendKey);
        }

        try {
            esClient.update(imgDetailVo);
        } catch (Exception e) {
            throw new YanHuoException(Constant.ES_ERROR);
        }
        return imgDetail.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteImgs(List<String> idList, String uid) {
        if (idList.isEmpty()) {
            return;
        }
        //删除图片
        List<ImgDetail> imgDetailsEntityList = this.listByIds(idList);
        List<AlbumImgRelation> albumImgRelationList;

        //删除图片收藏
        agreeCollectService.remove(new QueryWrapper<AgreeCollect>().eq("type", 3).in("agree_collect_id", idList));

        //---
        for (ImgDetail model : imgDetailsEntityList) {
            //图片绑定专辑
            albumImgRelationList = albumImgRelationService.list(new QueryWrapper<AlbumImgRelation>().eq("mid", model.getId()));

            //所有绑定的专辑
            List<Long> albumIds = albumImgRelationList.stream().map(AlbumImgRelation::getAid).collect(Collectors.toList());
            List<Album> albumList = albumService.listByIds(albumIds);
            HashMap<Long, Album> albumMap = new HashMap<>();
            albumList.forEach(item -> {
                albumMap.put(item.getId(), item);
            });

            if (String.valueOf(model.getUserId()).equals(uid)) {

                for (AlbumImgRelation albumImgRelationModel : albumImgRelationList) {
                    Album albumEntity = albumMap.get(albumImgRelationModel.getAid());

                    if (albumEntity.getImgCount() >= model.getCount()) {
                        albumEntity.setImgCount(albumEntity.getImgCount() - model.getCount());
                        albumService.updateById(albumEntity);
                    }

                    albumImgRelationService.remove(new QueryWrapper<AlbumImgRelation>().and(e -> e.eq("mid", model.getId()).eq("aid", albumEntity.getId())));
                    //删除图片与标签的绑定关系
                    tagImgRelationService.remove(new QueryWrapper<TagImgRelation>().eq("mid", model.getId()));
                    String viewRecordKey = PlatformConstant.IMG_VIEW_RECORD + model.getId();
                    if (Boolean.TRUE.equals(redisUtils.hasKey(viewRecordKey))) {
                        redisUtils.delete(viewRecordKey);
                    }

                    //删除推荐系统中缓存的数据
                    String recommendKey = PlatformConstant.RECOMMEND + model.getId();
                    if (Boolean.TRUE.equals(redisUtils.hasKey(recommendKey))) {
                        redisUtils.delete(recommendKey);
                    }

                    //删除hmap中的图片信息
                    redisUtils.hDelete(PlatformConstant.IMG_DETAIL_LIST_KEY, String.valueOf(model));

                    if (model.getStatus() == 1) {
                        //删除es中的数据
                        try {
                            esClient.delData(String.valueOf(model.getId()));
                        } catch (Exception e) {
                            throw new YanHuoException(ResultCodeEnum.FAIL);
                        }
                    }

                    //删除oss对象存储中的图片
                    List<String> fileNames = JSON.parseArray(model.getImgsUrl(), String.class);

                    for (String item : fileNames) {
                        if (WebUtils.isHttpUrl(item)) {
                            ossClient.deleteFile(item,2);
                        }
                    }
                    this.removeById(model);
                }
            } else {
                //找到当前图片所属专辑
                for (AlbumImgRelation albumImgRelationModel : albumImgRelationList) {
                    Album albumEntity = albumMap.get(albumImgRelationModel.getAid());
                    if (String.valueOf(albumEntity.getUid()).equals(uid)) {
                        albumImgRelationService.remove(new QueryWrapper<AlbumImgRelation>().and(e -> e.eq("mid", model.getId()).eq("aid", albumImgRelationModel.getAid())));
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBulkData() {
        List<ImgDetail> imgDetailList = this.list(null);
        List<ImgDetailVo> res = new ArrayList<>();
        ImgDetailVo imgDetailVo;
        User user;
        List<String> imgList;

        List<Long> uids = imgDetailList.stream().map(ImgDetail::getUserId).collect(Collectors.toList());
        List<User> userList = userService.listByIds(uids);
        HashMap<Long, User> userMap = new HashMap<>();
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        for (ImgDetail model : imgDetailList) {
            imgList = JSON.parseArray(model.getImgsUrl(), String.class);
            user = userMap.get(model.getUserId());
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            imgDetailVo.setImgsUrl(imgList)
                    .setUserId(user.getId())
                    .setOtherUserId(user.getUserId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setTime(model.getUpdateDate());
            res.add(imgDetailVo);
        }
        try {
            esClient.addBulkData(res);
        } catch (Exception e) {
            throw new YanHuoException(Constant.ES_ERROR);
        }
    }

    @Override
    public Page<ImgDetailVo> getHot(long page, long limit) {
        Page<ImgDetailVo> resPage = new Page<>();

        String key = PlatformConstant.HOT;
        if (Boolean.TRUE.equals(redisUtils.hasKey(key))) {
            String objStr = redisUtils.get(key);
            List<ImgDetailVo> imgDetailVos = JSON.parseArray(objStr, ImgDetailVo.class);
            return PageUtils.getPages((int) page, (int) limit, imgDetailVos);
        }

        Page<ImgDetail> imgDetailsEntityPage = this.page(new Page<>(page, limit), new QueryWrapper<ImgDetail>().orderByDesc("agree_count").ge("agree_count", 1));
        List<ImgDetailVo> list = new ArrayList<>();
        List<ImgDetail> imgDetailList = imgDetailsEntityPage.getRecords();
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
        resPage.setRecords(list);
        resPage.setTotal(imgDetailsEntityPage.getTotal());

        return resPage;
    }

    @Override
    public List<ImgDetailVo> getAllBrowseRecordByUser(long page, long limit, String uid) {

        List<ImgDetailVo> imgDetailVoList = new ArrayList<>();
        String userBrowseRecordKey = PlatformConstant.BR_IMG_KEY + uid;
        List<String> mids;
        if (Boolean.TRUE.equals(redisUtils.hasKey(userBrowseRecordKey))) {
            mids = redisUtils.lRange(userBrowseRecordKey, (page - 1) * limit, page * limit);
            List<ImgDetail> imgDetailList = this.listByIds(mids);

            Map<Long, User> userMap = new HashMap<>();
            List<Long> uids = imgDetailList.stream().map(ImgDetail::getUserId).collect(Collectors.toList());
            List<User> userList = userService.listByIds(uids);
            userList.forEach(item -> {
                userMap.put(item.getId(), item);
            });

            Map<Long, ImgDetail> imgDetailMap = new HashMap<>();
            imgDetailList.forEach(item -> {
                imgDetailMap.put(item.getId(), item);
            });

            ImgDetailVo imgDetailVo;
            User user;
            for (String id : mids) {
                ImgDetail imgDetail = imgDetailMap.get(Long.valueOf(id));
                imgDetailVo = ConvertUtils.sourceToTarget(imgDetail, ImgDetailVo.class);
                user = userMap.get(imgDetail.getUserId());
                imgDetailVo.setUsername(user.getUsername())
                        .setAvatar(user.getAvatar());
                imgDetailVoList.add(imgDetailVo);
            }
        }
        return imgDetailVoList;
    }

    @Override
    public void addBrowseRecord(BrowseRecordDTO browseRecordDTO) {
        String userBrowseRecordKey = PlatformConstant.BR_IMG_KEY + browseRecordDTO.getUid();
        if (Boolean.TRUE.equals(redisUtils.hasKey(userBrowseRecordKey))) {
            redisUtils.lRemove(userBrowseRecordKey, 0, String.valueOf(browseRecordDTO.getMid()));
        }
        redisUtils.lLeftPush(userBrowseRecordKey, String.valueOf(browseRecordDTO.getMid()));
    }

    @Override
    public void delRecord(String uid, List<String> idList) {
        String userBrowseRecordKey = PlatformConstant.BR_IMG_KEY + uid;
        if (Boolean.TRUE.equals(redisUtils.hasKey(userBrowseRecordKey))) {
            for (String mid : idList) {
                redisUtils.lRemove(userBrowseRecordKey, 0, mid);
            }
        }
    }

    @Override
    public void addBulkRedisData() {
        List<ImgDetail> imgDetailList = this.list(null);
        List<Long> uids = imgDetailList.stream().map(ImgDetail::getUserId).collect(Collectors.toList());
        List<User> userList = userService.listByIds(uids);
        HashMap<Long,User> userMap = new HashMap<>();
        userList.forEach(item->{
            userMap.put(item.getId(),item);
        });
        String key = PlatformConstant.IMG_DETAIL_LIST_KEY;
        for (ImgDetail model: imgDetailList) {
            redisUtils.hPut(key,String.valueOf(model.getId()), JSON.toJSONString(model));
        }
    }
}