package com.xz.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xz.common.service.BaseService;
import com.xz.platform.dto.CommentDTO;
import com.xz.platform.entity.CommentEntity;
import com.xz.platform.vo.CommentVo;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface CommentService extends BaseService<CommentEntity> {


    IPage<CommentVo> getAllOneCommentByImgId(long page, long limit, String mid,String uid);

    IPage<CommentVo> getAllTwoCommentByOneId(long page, long limit, String id,String uid);

    List<CommentVo> getAllTwoComment(String id ,String uid);

    List<CommentVo> getAllReplyComment(long page, long limit, String uid);

    CommentVo addComment(CommentDTO commentDTO);

}