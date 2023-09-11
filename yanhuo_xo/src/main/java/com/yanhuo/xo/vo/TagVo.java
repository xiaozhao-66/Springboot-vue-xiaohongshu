package com.yanhuo.xo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
public class TagVo implements Serializable {


    private Long count;

    private String name;
}
