package com.xz.platform.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class BrowseRecordVo implements Serializable {


    /**
     *
     */
    private Long id;

    private Long mid;
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

    private String avatar;

    private String username;

    private List<String> imgsUrl;

    //当前imgsUrl的数量
    private Integer nums;

    private Long agreeCount;
}
