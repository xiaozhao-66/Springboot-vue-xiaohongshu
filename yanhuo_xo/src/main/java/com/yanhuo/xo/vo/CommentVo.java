package com.yanhuo.xo.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 48423
 */
@Data
@Accessors(chain = true)
public class CommentVo implements Serializable {
    /**
     *
     */
    private Long id;
    /**
     *
     */
    private Long mid;

    private String cover;
    /**
     *
     */
    private Long uid;


    private String username;

    private String replyName;

    private String replyContent;


    private String avatar;
    /**
     *
     */
    private Long pid;
    /**
     *
     */

    private Long replyUid;


    private Long replyId;
    /**
     *
     */
    private Integer level;
    /**
     *
     */
    private Integer sort;
    /**
     *
     */
    private String content;

    private Long count;


    //重做评论模块
    private List<CommentVo> childrenComments;

    private Date createDate;

    //是否点赞
    private Boolean isAgree;

    private Long twoNums;
}
