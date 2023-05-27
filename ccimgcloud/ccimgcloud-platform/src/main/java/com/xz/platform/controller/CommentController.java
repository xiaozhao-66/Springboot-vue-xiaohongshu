package com.xz.platform.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xz.common.utils.Result;
import com.xz.common.validator.ValidatorUtils;
import com.xz.common.validator.group.DefaultGroup;
import com.xz.platform.dto.CommentDTO;
import com.xz.platform.entity.CommentEntity;
import com.xz.platform.service.CommentService;
import com.xz.platform.vo.CommentVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 
 *
 * @author xiaozhao sunlightcs@gmail.com
 * @since 1.0.0 2023-03-16
 */
@RestController
@RequestMapping("/comment")
@Api(tags="评论模块")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取所有的评论
     *
     * @param page
     * @param limit
     * @param mid
     * @param uid
     * @return
     */
    @RequestMapping("getAllOneCommentByImgId/{page}/{limit}")
    public Result<?> getAllOneCommentByImgId(@PathVariable long page, @PathVariable long limit, String mid, String uid) {
        Result<IPage<CommentVo>> result = new Result<>();
        IPage<CommentVo> pageInfo = commentService.getAllOneCommentByImgId(page, limit, mid, uid);
        return result.ok(pageInfo);
    }

    /**
     * 获取所有的二级评论
     *
     * @param page
     * @param limit
     * @param id
     * @param uid
     * @return
     */
    @RequestMapping("getAllTwoCommentByOneId/{page}/{limit}")
    public Result<?> getAllTwoCommentByOneId(@PathVariable long page, @PathVariable long limit, String id, String uid) {
        Result<IPage<CommentVo>> result = new Result<>();
        IPage<CommentVo> pageInfo = commentService.getAllTwoCommentByOneId(page, limit, id, uid);
        return result.ok(pageInfo);
    }

    /**
     * 得到当前一级评论下的所有二级评论
     *
     * @param id
     * @return
     */
    @RequestMapping("getAllTwoComment")
    public Result<?> getAllTwoComment(String id, String uid) {
        Result<List<CommentVo>> result = new Result<>();
        List<CommentVo> pageInfo = commentService.getAllTwoComment(id, uid);
        return result.ok(pageInfo);
    }

    /**
     * 得到评论信息
     *
     * @param id
     * @return
     */
    @RequestMapping("getComment")
    public Result<?> getComment(String id) {
        return new Result<>().ok(commentService.selectById(id));
    }

    /**
     * 增加评论
     *
     * @param commentDTO
     * @return
     */
    @RequestMapping("addComment")
    public Result<?> addComment(@RequestBody CommentDTO commentDTO) {
        ValidatorUtils.validateEntity(commentDTO, DefaultGroup.class);
        CommentVo commentVo = commentService.addComment(commentDTO);
        return new Result<>().ok(commentVo);
    }

    /**
     * 得到所有回复的评论
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @RequestMapping("getAllReplyComment/{page}/{limit}")
    public Result<?> getAllReplyComment(@PathVariable long page, @PathVariable long limit, String uid) {
        Result<IPage<CommentVo>> result = new Result<>();
        IPage<CommentVo> pageInfo = commentService.getAllReplyComment(page, limit, uid);
        return result.ok(pageInfo);
    }
}