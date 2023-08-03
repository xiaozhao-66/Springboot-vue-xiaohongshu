package io.renren.modules.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.platform.entity.MemberEntity;

import java.util.Map;

/**
 * 
 *
 * @author xiaozhao
 * @email 484235492@qq.com
 * @date 2023-07-19 15:24:23
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

