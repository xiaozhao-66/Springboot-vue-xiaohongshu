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


    @Autowired
    BrowseRecordDao browseRecordDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ImgDetailsService imgDetailsService;

    @Autowired
    RedisUtils redisUtils;

    @Test
    public  void test1(){
        List<String> strings = browseRecordDao.selectNums("1601126546037874690", 2);
        System.out.println(strings);
    }

    @Test
    public  void test2(){
        List<String> strings = userDao.selectRand(1);
        System.out.println(strings);
    }

    @Test
    public void test3(){
        Page<ImgDetailVo> imgDetailVoPage = imgDetailsService.recommendToUser(1, 1, "1601126546037874693");
        System.out.println(imgDetailVoPage);
    }

    @Test
    public void test4(){
        HashMap<String,HashMap<String, Integer>> map = new HashMap<>();
        HashMap<String, Integer> map2 = new HashMap<>();
        map.put("11",map2);
        HashMap<String, Integer> stringIntegerHashMap = map.get("11");
        System.out.println(stringIntegerHashMap.isEmpty());
    }
    /**
     * 用户0浏览记录（1，3）
     *            （2，5）
     *            （3，18)\
     用户1浏览记录（2，3）
     *            （4，10）
     *            （6，18）
     *            （7，5）
     *            （8，11）
     用户2浏览记录（1，3）
     *            （2，5）
     *            （3，18）
     *            （4，5）
     *            （5，6）
     用户3浏览记录（1，3）
     *            （2，5）
     *            （3，18）
     *            （4，5）
     *            （5，6）
     用户4浏览记录（1，3）
     *            （2，5）
     *            （3，18）
     *            （4，5）
     *            （5，6）
     用户5浏览记录（1，3）
     *            （2，5）
     *            （3，18）
     *            （4，5）
     *            （5，6）
     *
     *
     *            [(x1-x2)+(y1-y2)]^2
     */
    @Test
    void contextLoads() {

          List<Integer> xs =new ArrayList<>();
          xs.add(3);
          xs.add(5);
          xs.add(8);
          xs.add(11);
          xs.add(9);

        List<Integer> ys =new ArrayList<>();
        xs.add(0);
        xs.add(0);
        xs.add(0);
        xs.add(0);
        xs.add(0);


        Double relate = getRelate(xs, ys);
        System.out.println(relate);

    }


    @Test
    public void test5(){
        redisUtils.zAdd("mm","1",System.currentTimeMillis());
        redisUtils.zAdd("mm","2",System.currentTimeMillis());
        redisUtils.zAdd("mm","3",System.currentTimeMillis());
    }


    @Test
    public void test6(){
        imgDetailsService.recommendToUser2(1,1,"1601126546037874693");
    }

    @Test
    public void test7(){
        ArrayList<Integer> list  = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        List<List<Integer>> partition = Lists.partition(list, 10);

        System.out.println(partition.get(0));

    }



    /**
     * 方法描述: 皮尔森（pearson）相关系数计算
     *
     * @param xs
     * @param ys
     * @Return {@link Double}
     * @throws
     * @author tarzan
     * @date 2020年07月31日 17:03:20
     */
    public static Double getRelate(List<Integer> xs, List<Integer>  ys){
        int n=xs.size();
        double Ex= xs.stream().mapToDouble(x->x).sum();
        double Ey=ys.stream().mapToDouble(y->y).sum();
        double Ex2=xs.stream().mapToDouble(x->Math.pow(x,2)).sum();
        double Ey2=ys.stream().mapToDouble(y->Math.pow(y,2)).sum();
        double Exy= IntStream.range(0,n).mapToDouble(i->xs.get(i)*ys.get(i)).sum();
        double numerator=Exy-Ex*Ey/n;
        double denominator=Math.sqrt((Ex2-Math.pow(Ex,2)/n)*(Ey2-Math.pow(Ey,2)/n));
        if (denominator==0) return 0.0;
        return numerator/denominator;
    }

}
