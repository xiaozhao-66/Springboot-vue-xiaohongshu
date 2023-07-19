package com.yanhuo.xo.dto.search;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchRecordDTO implements Serializable {

    private String uid;

    private String keyword;

//    private String highLightKeyword;
//
//    private Integer count;
//
//    //创建时间
//    private String time;
}
