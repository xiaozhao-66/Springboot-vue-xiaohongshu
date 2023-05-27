package com.xz.search.entity;

import lombok.Data;

import java.util.List;

@Data
public class ImgDetailSearchEntity {

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
    private Integer nums;

    private Long agreeCount;

    private String time;
}
