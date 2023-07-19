package com.yanhuo.platform.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanhuo.xo.model.Comment;
import com.yanhuo.xo.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@Mapper
public interface CommentDao extends BaseMapper<Comment> {

    /**
     * 得到所有回复消息
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    List<CommentVo> getAllReplyComment(long page, long limit, String uid);
}