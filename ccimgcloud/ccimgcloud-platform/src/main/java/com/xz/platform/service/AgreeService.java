package com.xz.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xz.common.service.CrudService;
import com.xz.platform.dto.AgreeDTO;
import com.xz.platform.entity.AgreeEntity;
import com.xz.platform.vo.AgreeVo;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface AgreeService extends CrudService<AgreeEntity, AgreeDTO> {

    boolean isAgree(AgreeDTO agreeDTO);

    void agree(AgreeDTO agreeDTO);

    Page<AgreeVo> getAllAgreeAndCollection(long page, long limit, String uid);

    void cancelAgree(AgreeDTO agreeDTO);
}