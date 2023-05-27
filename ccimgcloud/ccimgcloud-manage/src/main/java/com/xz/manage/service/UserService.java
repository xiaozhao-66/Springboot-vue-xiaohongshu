package com.xz.manage.service;


import com.xz.common.page.PageData;
import com.xz.common.service.BaseService;
import com.xz.manage.entity.UserEntity;
import com.xz.manage.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-13
 */
public interface UserService extends BaseService<UserEntity> {

    PageData<UserVo> getPage(long page, long limit, Map<String, Object> params);
}