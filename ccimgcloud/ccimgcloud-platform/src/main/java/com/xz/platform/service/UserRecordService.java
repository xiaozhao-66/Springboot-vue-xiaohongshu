package com.xz.platform.service;

import com.xz.common.service.CrudService;
import com.xz.platform.dto.UserRecordDTO;
import com.xz.platform.entity.UserRecordEntity;
import com.xz.platform.vo.UserRecordVo;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface UserRecordService extends CrudService<UserRecordEntity, UserRecordDTO> {

    UserRecordVo getUserRecord(String uid);

    void clearUserRecord(String uid, Integer type);
}