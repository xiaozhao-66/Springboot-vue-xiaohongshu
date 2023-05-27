package com.xz.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.page.PageData;
import com.xz.common.service.impl.BaseServiceImpl;
import com.xz.common.service.impl.CrudServiceImpl;
import com.xz.manage.dao.UserDao;
import com.xz.manage.entity.UserEntity;
import com.xz.manage.service.UserService;
import com.xz.manage.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserDao, UserEntity> implements UserService {


    @Override
    public PageData<UserVo> getPage(long page, long limit, Map<String, Object> params) {


        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        if(StringUtils.isNotEmpty(key)){
            queryWrapper.like("username",key).or().like("user_id",key);
        }
        if(StringUtils.isNotEmpty(startDate)){
            queryWrapper.ge("create_date",startDate);
        }
        if(StringUtils.isNotEmpty(endDate)){
            queryWrapper.le("create_date",endDate);
        }
        queryWrapper.orderByAsc("create_date");


        IPage<UserEntity> pageInfo = baseDao.selectPage(new Page<>(page,limit), queryWrapper);

        return getPageData(pageInfo,UserVo.class);
    }
}