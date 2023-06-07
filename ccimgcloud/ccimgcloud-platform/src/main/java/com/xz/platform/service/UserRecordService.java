package com.xz.platform.service;

import com.xz.common.service.BaseService;
import com.xz.platform.entity.UserRecordEntity;
import com.xz.platform.vo.UserRecordVo;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface UserRecordService extends BaseService<UserRecordEntity> {

    UserRecordVo getUserRecord(String uid);

    void clearUserRecord(String uid, Integer type);
}