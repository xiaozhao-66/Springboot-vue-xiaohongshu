package com.yanhuo.xo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class UserVo implements Serializable {

    /**
     *
     */
    private Long id;
    /**
     *
     */
    private Long userId;
    /**
     *
     */
    private String username;
    /**
     *
     */
    private String password;
    /**
     *
     */
    private String avatar;
    /**
     *
     */
    private Integer gender;
    /**
     *
     */
    private String phone;
    /**
     *
     */
    private String email;
    /**
     *
     */
    private String description;
    /**
     *
     */
    private Integer status;
    /**
     *
     */
    private String birthday;
    /**
     *
     */
    private String address;
    /**
     *
     */
    private String cover;
    /**
     *
     */
    private Long creator;
    /**
     *
     */
    private Date createDate;

    /**
     *
     */
    private Long trendCount;
    /**
     *
     */
    private Long followCount;
    /**
     *
     */
    private Long fanCount;
    /**
     *
     */
    private Long noreplyCount;
    /**
     *
     */
    private Long agreeCollectionCount;
}
