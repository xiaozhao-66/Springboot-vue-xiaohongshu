package com.xz.manage.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
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
    private Integer status;

    /**
     *
     */
    private String address;

    /**
     *
     */
    private Date createDate;

    /**
     *
     */
    private Date updateDate;
}
