package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuo.xo.dto.platform.CommentDTO;
import com.yanhuo.xo.model.Comment;
import com.yanhuo.xo.vo.CommentVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
public interface CommentService extends IService<Comment> {


    /**
     * 得到所有的一级评论并携带一个二级评论
     *
     * @param page
     * @param limit
     * @param mid
     * @param uid
     * @return
     */
    IPage<CommentVo> getAllComment(long page, long limit, String mid, String uid);

    /**
     * 增加一条评论
     *
     * @param commentDTO
     * @return
     */
    CommentVo addComment(CommentDTO commentDTO);

    /**
     * 得到当前图片下的所有一级评论
     *
     * @param page
     * @param limit
     * @param mid
     * @param uid
     * @return
     */
    IPage<CommentVo> getAllOneCommentByImgId(long page, long limit, String mid, String uid);

    /**
     * 得到所有的二级评论根据一级评论id
     *
     * @param page
     * @param limit
     * @param id
     * @param uid
     * @return
     */
    IPage<CommentVo> getAllTwoCommentByOneId(long page, long limit, String id, String uid);

    /**
     * 得到所有的二级评论
     *
     * @param id
     * @param uid
     * @return
     */
    List<CommentVo> getAllTwoComment(String id, String uid);

    /**
     * 查看所有回复的评论
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    List<CommentVo> getAllReplyComment(long page, long limit, String uid);

    /**
     * 删除一条评论
     * @param id
     */
    void delComment(String id);

    Map<String, Object> scrollComment(String id, String mid, String uid);
}