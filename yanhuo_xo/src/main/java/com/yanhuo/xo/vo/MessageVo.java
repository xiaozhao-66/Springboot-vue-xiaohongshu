package com.yanhuo.xo.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MessageVo implements Serializable {

    private String sendId;

    private String acceptId;

    private String username;

    private String avatar;

    private Integer count;

    private String content;

    private String date;

    private String  time;
}
