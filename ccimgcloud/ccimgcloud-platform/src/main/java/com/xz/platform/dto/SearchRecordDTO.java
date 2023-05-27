package com.xz.platform.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * @author 48423
 */
@Data
public class SearchRecordDTO implements Serializable {


    private Long uid;

    private String keyword;
}
