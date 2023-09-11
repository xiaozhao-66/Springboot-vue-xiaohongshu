package com.yanhuo.xo.dto.search;

import com.yanhuo.common.validator.group.DefaultGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 48423
 */
@Data
public class SearchRecordDTO implements Serializable {

    @NotNull(message = "uid不能为空", groups = DefaultGroup.class)
    private String uid;

    @NotBlank(message = "关键词不能为空", groups = DefaultGroup.class )
    private String keyword;

}
