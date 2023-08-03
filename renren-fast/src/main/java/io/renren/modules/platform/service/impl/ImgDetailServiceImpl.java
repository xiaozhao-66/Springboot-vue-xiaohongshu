package io.renren.modules.platform.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.platform.dao.ImgDetailDao;
import io.renren.modules.platform.entity.ImgDetailEntity;
import io.renren.modules.platform.service.ImgDetailService;


@Service("imgDetailService")
public class ImgDetailServiceImpl extends ServiceImpl<ImgDetailDao, ImgDetailEntity> implements ImgDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ImgDetailEntity> page = this.page(
                new Query<ImgDetailEntity>().getPage(params),
                new QueryWrapper<ImgDetailEntity>()
        );

        return new PageUtils(page);
    }

}