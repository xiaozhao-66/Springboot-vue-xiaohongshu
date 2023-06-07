package com.xz.recommend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.xz.common.utils.RedisUtils;
import com.xz.recommend.dao.BrowseRecordDao;
import com.xz.recommend.dao.UserDao;
import com.xz.recommend.entity.BrowseRecordEntity;
import com.xz.recommend.service.ImgDetailsService;
import com.xz.recommend.vo.ImgDetailVo;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@SpringBootTest
class RecommendApplicationTests {

    @Test
    void test(){

    }


}
