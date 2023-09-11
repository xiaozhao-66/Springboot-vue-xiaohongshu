package com.yanhuo.platform.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.common.utils.ConvertUtils;
import com.yanhuo.platform.dao.TagDao;
import com.yanhuo.platform.service.ImgDetailService;
import com.yanhuo.platform.service.TagImgRelationService;
import com.yanhuo.platform.service.TagService;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.dto.platform.TagDTO;
import com.yanhuo.xo.model.ImgDetail;
import com.yanhuo.xo.model.Tag;
import com.yanhuo.xo.model.TagImgRelation;
import com.yanhuo.xo.model.User;
import com.yanhuo.xo.vo.ImgDetailVo;
import com.yanhuo.xo.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements TagService {

    @Autowired
    TagImgRelationService tagImgRelationService;

    @Autowired
    ImgDetailService imgDetailService;

    @Autowired
    UserService userService;

    @Override
    public List<TagVo> getAllTag() {
        List<Tag> tagList = baseMapper.selectTagsNums(22);
        return ConvertUtils.sourceToTarget(tagList, TagVo.class);
    }


    @Override
    public void saveTag(TagDTO tagDTO) {
        Tag tag = this.getOne(new QueryWrapper<Tag>().eq("name", tagDTO.getName()));
        if (tag != null) {
            tag.setCount(tag.getCount() + 1);
            this.updateById(tag);
        } else {
            Tag tagEntity = ConvertUtils.sourceToTarget(tagDTO, Tag.class);
            tagEntity.setCount(1L);
            this.save(tagEntity);
        }
    }

    @Override
    public long saveTagByName(String name) {
        Tag tag = this.getOne(new QueryWrapper<Tag>().eq("name", name));
        if (tag != null) {
            tag.setCount(tag.getCount() + 1);
            this.updateById(tag);
            return tag.getId();
        } else {
            Tag tagEntity = new Tag();
            tagEntity.setName(name);
            tagEntity.setCount(1L);
            this.save(tagEntity);
            return tagEntity.getId();
        }
    }

    @Override
    public Page<ImgDetailVo> getImgListByTag(long page, long limit, String id, Integer type) {
        Page<ImgDetailVo> pages = new Page<>();
        Page<ImgDetail> imgDetailPage;
        List<TagImgRelation> tagImgRelationList = tagImgRelationService.list(new QueryWrapper<TagImgRelation>().eq("tid", id));
        List<Long> mids = tagImgRelationList.stream().map(TagImgRelation::getMid).collect(Collectors.toList());

        if (type == 0) {
            //获取最新
            imgDetailPage = imgDetailService.page(new Page<>(page, limit), new QueryWrapper<ImgDetail>().in("id", mids).orderByDesc("create_date"));
        } else {
            imgDetailPage = imgDetailService.page(new Page<>(page, limit), new QueryWrapper<ImgDetail>().in("id", mids).orderByDesc("agree_count"));
        }

        List<ImgDetail> imgDetailList = imgDetailPage.getRecords();
        Collection<Long> uids = new HashSet<>();
        imgDetailList.forEach(item -> {
            uids.add(item.getUserId());
        });

        List<User> userList = userService.listByIds(uids);
        HashMap<Long, User> userMap = new HashMap<>();
        userList.forEach(item -> {
            userMap.put(item.getId(), item);
        });

        List<ImgDetailVo> res = new ArrayList<>();
        ImgDetailVo imgDetailVo;
        User user;
        for (ImgDetail model : imgDetailPage.getRecords()) {
            imgDetailVo = ConvertUtils.sourceToTarget(model, ImgDetailVo.class);
            user = userMap.get(model.getUserId());
            imgDetailVo.setUserId(user.getId())
                    .setUsername(user.getUsername())
                    .setAvatar(user.getAvatar());
            res.add(imgDetailVo);
        }
        pages.setRecords(res);
        pages.setTotal(imgDetailPage.getTotal());

        return pages;
    }
}