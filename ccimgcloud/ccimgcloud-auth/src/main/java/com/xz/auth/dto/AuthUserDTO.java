package com.xz.auth.dto;

import com.xz.common.validator.group.AddGroup;
import com.xz.common.validator.group.DefaultGroup;
import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Data
public class AuthUserDTO implements Serializable {

    private Long id;

    private String username;

    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$", message = "请输入8-20位由字母和数字组成的密码", groups = DefaultGroup.class)
    private String password;

    private String checkPassword;

    @Pattern(regexp = "^1[0-9]{10}$", message = "手机号格式有误", groups = DefaultGroup.class)
    private String phone;

    @Pattern(regexp = "^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$", message = "邮箱输入有误", groups = DefaultGroup.class)
    private String email;

    private String code;

    private String userId;

    private Integer type;
}
