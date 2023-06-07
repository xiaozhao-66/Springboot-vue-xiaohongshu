package com.xz.platform.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class FollowTrendVo implements Serializable {

    private Long userId;

    private Long mid;

    private String username;

    private String avatar;

    private String time;

    private String content;

    private String imgsUrl;

    private Long albumId;

    private String albumName;

    private Long agreeCount;

    private Long commentCount;

    private Boolean isAgree;
}
