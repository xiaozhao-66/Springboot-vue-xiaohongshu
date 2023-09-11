package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.platform.AgreeCollectDTO;
import com.yanhuo.xo.model.AgreeCollect;
import com.yanhuo.xo.vo.AgreeCollectVo;

import java.util.Map;

public interface AgreeCollectService extends IService<AgreeCollect> {

    /**
     * 点赞
     *
     * @param agreeCollectDTO
     */
    void agree(AgreeCollectDTO agreeCollectDTO);

    /**
     * 查看是否点赞
     *
     * @param agreeCollectDTO
     * @return
     */
    boolean isAgree(AgreeCollectDTO agreeCollectDTO);

    /**
     * 得到所有的赞和收藏
     *
     * @param page
     * @param limit
     * @param uid   当前用户id
     * @return
     */
    Page<AgreeCollectVo> getAllAgreeAndCollection(long page, long limit, String uid);

    /**
     * 取消点赞
     *
     * @param agreeCollectDTO
     */
    void cancelAgree(AgreeCollectDTO agreeCollectDTO);

    /**
     * 得到所有的收藏
     *
     * @param page
     * @param limit
     * @param uid
     * @param type  0代表收藏的图片，1代表收藏的专辑
     * @return
     */
    Page<AgreeCollectVo> getAllCollection(long page, long limit, String uid, Integer type);

    /**
     * 收藏
     *
     * @param agreeCollectDTO
     * @return
     */
    Map<String, String> collection(AgreeCollectDTO agreeCollectDTO);

    /**
     * 取消收藏
     *
     * @param agreeCollectDTO
     * @return
     */
    Map<String, String> cancelCollection(AgreeCollectDTO agreeCollectDTO);
}
