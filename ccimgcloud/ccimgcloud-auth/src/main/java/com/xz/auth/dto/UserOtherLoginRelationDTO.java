package com.xz.auth.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserOtherLoginRelationDTO implements Serializable {

    private Long id;

    private Long uid;

    private String otherUserId;

    private String otherUsername;

    private String otherAvatar;

    private String otherToken;
}
