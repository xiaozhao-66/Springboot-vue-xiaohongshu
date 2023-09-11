package com.yanhuo.xo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanhuo.xo.model.Album;
import com.yanhuo.xo.model.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class ImgDetailVo implements Serializable {
    /**
     *
     */
    private Long id;
    /**
     *
     */
    private String content;
    /**
     *
     */
    private String cover;
    /**
     *
     */
    private Long userId;

    private Long categoryId;
    //
    private String categoryName;
    //
    private Long categoryPid;
    //
    private String categoryPName;

    private Long otherUserId;

    private Album album;


    private String albumName;

    //专辑的图片数量
    private Long imgCount;

    private Integer status;

    /**
     *
     */
    private String avatar;

    private String username;

    private List<String> imgsUrl;

    //当前imgsUrl的数量
    private Integer count;

    private List<Tag> tagList;

    private Long viewCount;

    private Long agreeCount;
    //
    private Long collectionCount;

    private Long commentCount;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;
}
