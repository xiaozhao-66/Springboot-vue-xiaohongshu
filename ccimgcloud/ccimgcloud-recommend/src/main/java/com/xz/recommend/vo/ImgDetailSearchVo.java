package com.xz.recommend.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 48423
 */
@Data
public class ImgDetailSearchVo implements Serializable {

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

    private Long otherUserId;


    private String albumName;

    //专辑的图片数量
    private Long imgCount;

    /**
     *
     */
    private String avatar;

    private String username;

    private List<String> imgsUrl;

    //当前imgsUrl的数量
    private Integer count;

    private Long agreeCount;

    private String time;
}
