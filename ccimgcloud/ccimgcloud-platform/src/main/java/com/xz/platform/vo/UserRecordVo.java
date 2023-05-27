package com.xz.platform.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class UserRecordVo implements Serializable {

    /**
     *
     */
    private Long id;
    /**
     *
     */
    private Long uid;
    /**
     *
     */
    private Long trendCount;
    /**
     *
     */
    private Long followCount;
    /**
     *
     */
    private Long fanCount;

    private Long addFollowCount;
    /**
     *
     */
    private Long noreplyCount;
    /**
     *
     */
    private Long agreeCollectionCount;
    /**
     *
     */
    private Long creator;
    /**
     *
     */
    private Long nochatCount;
    /**
     *
     */
    private Date createDate;
}
