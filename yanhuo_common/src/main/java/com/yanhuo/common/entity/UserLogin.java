package com.yanhuo.common.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserLogin implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    /**
     * 唯一用户的id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;

    private String password;

    private String avatar;

    /**
     * 电话
     */
    private String phone;
    /**
     * email
     */
    private String email;


}
