package com.yanhuo.platform.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanhuo.common.result.Result;
import com.yanhuo.common.validator.ValidatorUtils;
import com.yanhuo.common.validator.group.DefaultGroup;
import com.yanhuo.platform.service.CommentService;
import com.yanhuo.xo.dto.platform.CommentDTO;
import com.yanhuo.xo.vo.CommentVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/api/platform/comment")
@Api(tags="评论模块")
public class CommentController {

    @Autowired
    private CommentService commentService;


    /**
     * 得到当前图片下的所有一级评论
     *
     * @param page
     * @param limit
     * @param mid
     * @param uid
     * @return
     */
    @RequestMapping("getAllOneCommentByImgId/{page}/{limit}")
    public Result<?> getAllOneCommentByImgId(@PathVariable long page, @PathVariable long limit, String mid, String uid) {
        IPage<CommentVo> pageInfo = commentService.getAllOneCommentByImgId(page, limit, mid, uid);
        return Result.ok(pageInfo);
    }

    /**
     * 得到评论信息
     *
     * @param id
     * @return
     */
    @RequestMapping("getComment")
    public Result<?> getComment(String id) {
        return Result.ok(commentService.getById(id));
    }

    /**
     * 增加一条评论
     *
     * @param commentDTO
     * @return
     */
    @RequestMapping("addComment")
    public Result<?> addComment(@RequestBody CommentDTO commentDTO) {
        ValidatorUtils.validateEntity(commentDTO, DefaultGroup.class);
        CommentVo commentVo = commentService.addComment(commentDTO);
        return Result.ok(commentVo);
    }

    /**
     * 分页查询一级评论下的所有二级评论
     *
     * @param page
     * @param limit
     * @param id
     * @param uid
     * @return
     */
    @RequestMapping("getAllTwoCommentByOneId/{page}/{limit}")
    public Result<?> getAllTwoCommentByOneId(@PathVariable long page, @PathVariable long limit, String id, String uid) {
        IPage<CommentVo> pageInfo = commentService.getAllTwoCommentByOneId(page, limit, id, uid);
        return Result.ok(pageInfo);
    }

    /**
     * 得到当前一级评论下的所有二级评论
     *
     * @param id
     * @return
     */
    @RequestMapping("getAllTwoComment")
    public Result<?> getAllTwoComment(String id, String uid) {
        List<CommentVo> pageInfo = commentService.getAllTwoComment(id, uid);
        return Result.ok(pageInfo);
    }

    /**
     * 查看所有回复的评论
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @RequestMapping("getAllReplyComment/{page}/{limit}")
    public Result<?> getAllReplyComment(@PathVariable long page, @PathVariable long limit, String uid) {
        List<CommentVo> commentVoList = commentService.getAllReplyComment(page, limit, uid);
        return Result.ok(commentVoList);
    }

    /**
     * 得到所有的一级评论并携带一个二级评论
     *
     * @param page
     * @param limit
     * @param mid
     * @param uid
     * @return
     */
    @RequestMapping("getAllComment/{page}/{limit}")
    public Result<?> getAllComment(@PathVariable long page, @PathVariable long limit, String mid, String uid) {
        IPage<CommentVo> pageInfo = commentService.getAllComment(page, limit, mid, uid);
        return Result.ok(pageInfo);

    }


    /**
     * 跳转评论
     * @param id
     * @param mid
     * @param uid
     * @return
     */
    @RequestMapping("scrollComment")
    public Result<?> scrollComment(String id, String mid, String uid){
        Map<String,Object> resMap= commentService.scrollComment(id, mid, uid);
        return Result.ok(resMap);
    }

    @RequestMapping("delComment")
    public Result<?> delComment(String id) {
        commentService.delComment(id);
        return Result.ok(null);

    }

}