package com.xz.platform.service;

import com.xz.common.service.BaseService;
import com.xz.platform.dto.AgreeDTO;
import com.xz.platform.entity.AgreeEntity;
import com.xz.platform.vo.AgreeVo;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface AgreeService extends BaseService<AgreeEntity> {

    /**
     * 查看是否点赞
     * @param agreeDTO
     * @return
     */
    boolean isAgree(AgreeDTO agreeDTO);

    /**
     * 点赞
     * @param agreeDTO
     */
    void agree(AgreeDTO agreeDTO);

    /**
     * 得到当前用户所有的赞和收藏
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    List<AgreeVo> getAllAgreeAndCollection(long page, long limit, String uid);

    /**
     * 取消点赞
     * @param agreeDTO
     * @return
     */
    void cancelAgree(AgreeDTO agreeDTO);
}