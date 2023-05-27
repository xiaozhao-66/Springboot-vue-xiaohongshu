package com.xz.platform.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.common.utils.ConvertUtils;
import com.xz.common.utils.PageUtils;
import com.xz.platform.common.constant.Constant;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.dao.BrowseRecordDao;
import com.xz.platform.dao.ImgDetailsDao;
import com.xz.platform.dao.UserDao;
import com.xz.platform.dto.BrowseRecordDTO;
import com.xz.platform.entity.BrowseRecordEntity;
import com.xz.platform.entity.ImgDetailsEntity;
import com.xz.platform.entity.UserEntity;
import com.xz.platform.service.BrowseRecordService;
import com.xz.platform.vo.BrowseRecordVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class BrowseRecordServiceImpl extends CrudServiceImpl<BrowseRecordDao, BrowseRecordEntity, BrowseRecordDTO> implements BrowseRecordService {


    @Autowired
    ImgDetailsDao imgDetailsDao;

    @Autowired
    UserDao userDao;

    @Autowired
    RedisUtils redisUtils;


    @Override
    public QueryWrapper<BrowseRecordEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<BrowseRecordEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBrowseRecord(BrowseRecordDTO browseRecordDTO) {
        BrowseRecordEntity browseRecord = baseDao.selectOne(new QueryWrapper<BrowseRecordEntity>().and(e -> e.eq("uid", browseRecordDTO.getUid())).eq("mid", browseRecordDTO.getMid()));

        if (browseRecord == null) {
            BrowseRecordEntity browseRecordEntity = ConvertUtils.sourceToTarget(browseRecordDTO, BrowseRecordEntity.class);
            this.baseDao.insert(browseRecordEntity);
        } else {
            browseRecord.setUpdateDate(new Date());
            this.baseDao.updateById(browseRecord);
        }


        String brCountKey = Constant.BR_COUNT + browseRecordDTO.getUid() + ":" + browseRecordDTO.getMid();

        ImgDetailsEntity imgDetail = imgDetailsDao.selectById(browseRecordDTO.getMid());

        if (Boolean.TRUE.equals(redisUtils.hasKey(brCountKey))) {

            long time = Long.parseLong(redisUtils.get(brCountKey));
            long curTime = DateUtil.currentSeconds();

            //当前用户60秒之内访问不会增加浏览次数
            if (curTime - time > 60) {
                imgDetail.setViewCount(imgDetail.getViewCount() + 1);
                imgDetailsDao.updateById(imgDetail);
                redisUtils.set(brCountKey, String.valueOf(curTime));
            }

        } else {
            imgDetail.setViewCount(1L);
            imgDetailsDao.updateById(imgDetail);
            redisUtils.set(brCountKey, String.valueOf(DateUtil.currentSeconds()));
        }

        String uid = String.valueOf(browseRecordDTO.getUid());
        String cid = String.valueOf(imgDetail.getCategoryId());

        //添加当前用户浏览记录的分类次数(二级分类)  后面用来做推荐
        String key = Constant.BR_KEY + uid + ":" + cid;

        if (redisUtils.get(key) == null) {
            //设置7天过期
            redisUtils.setEx(key, "1", 7L, TimeUnit.DAYS);
        } else {
            int count = Integer.parseInt(redisUtils.get(key));
            redisUtils.setEx(key, String.valueOf(count + 1), 7L, TimeUnit.DAYS);
        }

        //添加用户浏览记录
        String key2 = Constant.BR_IMG_KEY + uid;
        redisUtils.zAdd(key2, String.valueOf(browseRecordDTO.getMid()), System.currentTimeMillis());

    }

    @Override
    public Page<BrowseRecordVo> getAllBrowseRecordByUser(long page, long limit, String uid) {

        List<BrowseRecordEntity> browseRecordList = baseDao.selectList(new QueryWrapper<BrowseRecordEntity>().eq("uid", uid).orderByDesc("update_date"));

        List<BrowseRecordVo> res = new ArrayList<>();

        ImgDetailsEntity imgDetailsEntity = null;
        BrowseRecordVo browseRecordVo = null;
        UserEntity user = null;
        for (BrowseRecordEntity model : browseRecordList) {

            imgDetailsEntity = imgDetailsDao.selectById(model.getMid());

            if (imgDetailsEntity == null) {
                continue;
            }
            browseRecordVo = new BrowseRecordVo();

            user = userDao.selectById(imgDetailsEntity.getUserId());
            List<String> imgList = JSON.parseArray(imgDetailsEntity.getImgsUrl(), String.class);

            browseRecordVo.setId(model.getId())
                    .setMid(imgDetailsEntity.getId())
                    .setUserId(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar())
                    .setImgsUrl(imgList)
                    .setNums(imgDetailsEntity.getCount())
                    .setContent(imgDetailsEntity.getContent())
                    .setCover(imgDetailsEntity.getCover())
                    .setAgreeCount(imgDetailsEntity.getAgreeCount());
            res.add(browseRecordVo);
        }
        return PageUtils.getPages((int) page, (int) limit, res);
    }

    @Override
    public void delRecord(String[] ids) {
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }
}