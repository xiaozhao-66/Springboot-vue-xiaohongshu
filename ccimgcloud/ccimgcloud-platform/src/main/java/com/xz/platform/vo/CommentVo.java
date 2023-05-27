package com.xz.platform.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    /**
     *
     */
    private Boolean children;

    //重做评论模块
    private List<CommentVo> childrenComments;


    private Date createDate;

    private String time;

    //是否点赞
    private Boolean isAgree;
}
