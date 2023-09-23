package com.yanhuo.xo.state;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentState implements Serializable {

    private Long cid;

    private Long count;

    private Long twoNums;
}
