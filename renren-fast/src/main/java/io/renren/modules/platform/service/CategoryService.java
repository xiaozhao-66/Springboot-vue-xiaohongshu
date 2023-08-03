package io.renren.modules.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.platform.entity.CategoryEntity;

import java.util.Map;

/**
 * 
 *
 * @author xiaozhao
 * @email 484235492@qq.com
 * @date 2023-07-19 19:42:35
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

