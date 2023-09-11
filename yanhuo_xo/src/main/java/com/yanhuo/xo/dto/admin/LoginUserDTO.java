package com.yanhuo.xo.dto.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;

    private String password;
}
