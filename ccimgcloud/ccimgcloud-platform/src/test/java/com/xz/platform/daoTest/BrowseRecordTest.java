package com.xz.platform.daoTest;

import com.xz.platform.dao.AgreeDao;
import com.xz.platform.dao.BrowseRecordDao;
import com.xz.platform.vo.AgreeVo;
import com.xz.platform.vo.BrowseRecordVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BrowseRecordTest {

    @Autowired
    BrowseRecordDao browseRecordDao;

    @Test
    public void test1(){
        List<BrowseRecordVo> list = browseRecordDao.getAllBrowseRecordByUser(1, 5, "1601126546037874692");
        for (BrowseRecordVo e:list
             ) {
            System.out.println(e);
        }
    }
}
