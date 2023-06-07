package com.xz.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.service.BaseService;
import com.xz.platform.dto.BrowseRecordDTO;
import com.xz.platform.entity.BrowseRecordEntity;
import com.xz.platform.vo.BrowseRecordVo;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface BrowseRecordService extends BaseService<BrowseRecordEntity> {

    void addBrowseRecord(BrowseRecordDTO browseRecordDTO);

    List<BrowseRecordVo> getAllBrowseRecordByUser(long page, long limit, String uid);

    void delRecord(String[] ids);
}