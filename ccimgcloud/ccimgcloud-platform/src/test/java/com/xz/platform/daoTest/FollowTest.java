package com.xz.platform.daoTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.platform.dao.AgreeDao;
import com.xz.platform.dao.FollowDao;
import com.xz.platform.entity.FollowEntity;
import com.xz.platform.vo.AgreeVo;
import com.xz.platform.vo.FollowTrendVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class FollowTest {


    @Autowired
    FollowDao followDao;

    @Test
    public void test1(){

        List<String> ids = new ArrayList<>();
        List<FollowEntity> follows = followDao.selectList(new QueryWrapper<FollowEntity>().eq("uid", "1601126546037874692"));

        ids.addAll(follows.stream().map(map -> String.valueOf(map.getFid())).collect(Collectors.toList()));

        ids.add("1601126546037874692");
        List<FollowTrendVo> allFollowTrends = followDao.getAllFollowTrends(1, 50, "1601126546037874692");


        for (FollowTrendVo e: allFollowTrends
             ) {

        }
    }

}
