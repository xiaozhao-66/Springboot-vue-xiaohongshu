package com.xz.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.service.CrudService;
import com.xz.platform.dto.BrowseRecordDTO;
import com.xz.platform.entity.BrowseRecordEntity;
import com.xz.platform.vo.BrowseRecordVo;
import com.xz.platform.vo.ImgDetailVo;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface BrowseRecordService extends CrudService<BrowseRecordEntity, BrowseRecordDTO> {

    void addBrowseRecord(BrowseRecordDTO browseRecordDTO);

    Page<BrowseRecordVo> getAllBrowseRecordByUser(long page, long limit, String uid);

    void delRecord(String[] ids);
}