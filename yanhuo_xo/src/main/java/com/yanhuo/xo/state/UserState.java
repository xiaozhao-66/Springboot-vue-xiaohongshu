package com.yanhuo.xo.state;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserState implements Serializable {

    private Long uid;

    private Long trendCount;

    private Long followCount;

    private Long fanCount;
}
