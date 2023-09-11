package com.yanhuo.platform.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuo.platform.dao.TagImgRelationDao;
import com.yanhuo.platform.service.TagImgRelationService;
import com.yanhuo.xo.model.TagImgRelation;
import org.springframework.stereotype.Service;

@Service
public class TagImgRelationServiceImpl extends ServiceImpl<TagImgRelationDao, TagImgRelation> implements TagImgRelationService {
}
