package com.xz.platform.daoTest;

import com.xz.platform.dao.AgreeDao;
import com.xz.platform.vo.AgreeVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AgreeTest {

    @Autowired
    AgreeDao agreeDao;

    @Test
    public void test1(){
        List<AgreeVo> allAgreeAndCollection = agreeDao.getAllAgreeAndCollection(1, 5, "1601126546037874692");
        System.out.println(allAgreeAndCollection);
    }
}
