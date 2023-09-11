package com.yanhuo.xo.model;

import com.yanhuo.common.entity.BaseEntity;
import lombok.Data;

/**
 * @author 48423
 */
@Data
public class AdminUser extends BaseEntity {

    private String username;

    private String password;
}
