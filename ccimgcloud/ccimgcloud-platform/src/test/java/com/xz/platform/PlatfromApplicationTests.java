package com.xz.platform;

import cn.hutool.Hutool;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.xz.common.utils.RedisUtils;
import com.xz.platform.dao.ImgDetailsDao;
import com.xz.platform.dao.UserDao;
import com.xz.platform.entity.ImgDetailsEntity;
import com.xz.platform.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class PlatfromApplicationTests {

    @Autowired
    ImgDetailsDao imgDetailsDao;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    UserDao userDao;

    static String[] urls = {"https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/03/24/b60a291dc63f4d3992fd2b5c31c418b21679666021-origin-IMG_1784.PNG",
                           "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/03/24/4b6ae2a684404cd1a53944c3d47e54861679666410-origin-IMG_1924.JPG",
                           "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/03/24/0cd8d18a61a540369b00b07d67ab7b181679666467-origin-IMG_1977.PNG",
                           "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/03/24/bc67e129844842fbb3453ff202824f101679664800-origin-IMG_1755.JPG",
                           "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/03/24/4759835dd90e4b6db1ad6ea68601a54b1679643012-origin-IMG_1728.PNG",
                           "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/03/24/8553bab726654b259b362ff663e731e51679643058-origin-IMG_1737.PNG",
                           "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/03/24/2a7485fc21eb4463ac061a510ae8bd8c1679643679-origin-IMG_1743.PNG",
                           "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/03/24/1f012a98ce5a4c9d9af8faf9b84b51301679642976-origin-IMG_1726.PNG",
                           "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/03/24/00fbcbfbf3d744d2b590623dd1f69f3a1679666100-origin-IMG_1791.PNG",
                           "https://cc-video-oss.oss-accelerate.aliyuncs.com/2023/03/24/ef76bc4c5cfc4d9da3a8fa501350f7e81679643020-origin-IMG_1729.PNG",};


    @Test
    public  void test7(){

        String s = "sdswewd;csqcfee;cas;weffefg;fbtyhy;ererqw;g54343ed2;";

        StringBuilder stringBuilder = new StringBuilder(s);

        String ss = "sdwws";

        stringBuilder.insert(0,ss);
        stringBuilder.append(ss);
        stringBuilder.append(";");

        System.out.println(stringBuilder.toString());

    }

    //添加100个用户测试
    @Test
    public  void test3(){
        for (int i = 0; i < 100; i++) {
            UserEntity userEntity = new UserEntity();
            String username = RandomUtil.randomString(6);
            userEntity.setUsername(username);
            userEntity.setPassword("123456");
            int e = RandomUtil.randomInt(0, 10);
            userEntity.setCover(urls[e]);
            int e2 = RandomUtil.randomInt(0, 10);
            userEntity.setAvatar(urls[e2]);
            userEntity.setGender(1);
            userDao.insert(userEntity);

        }
    }

    @Test
    public  void  test4(){
        List<ImgDetailsEntity> imgDetailsEntityList = imgDetailsDao.selectList(null);
        for (ImgDetailsEntity model:imgDetailsEntityList) {
            String imgsUrl = model.getImgsUrl();
            List<String> strings = JSON.parseArray(imgsUrl, String.class);
            model.setCount(strings.size());
            imgDetailsDao.updateById(model);
        }
    }

    @Test
    void contextLoads() {

        List<Integer> xs =new ArrayList<>();
        xs.add(3);
        xs.add(5);
        xs.add(8);
        xs.add(11);
        xs.add(9);

        List<Integer> ys1 =new ArrayList<>();
        ys1.add(2);
        ys1.add(3);
        ys1.add(4);
        ys1.add(0);
        ys1.add(6);

        List<Integer> ys2 =new ArrayList<>();
        ys2.add(2);
        ys2.add(4);
        ys2.add(11);
        ys2.add(9);
        ys2.add(8);


        Double relate1 = getRelate(xs, ys1);
        System.out.println(relate1);
        Double relate2= getRelate(xs, ys2);
        System.out.println(relate2);
    }


    @Test
    public void test2(){

        List<String> ids = new ArrayList<>();

        ids.add("1067246875800000001");
        ids.add("1067246875800000002");

        Map<String,Object> map = new HashMap<>();
        map.put("idList",ids);
        map.put("page",2L);
        map.put("limit",3L);
        List<ImgDetailsEntity> imgDetailsEntityList = imgDetailsDao.selectBatch(map);
        System.out.println(imgDetailsEntityList.stream().map(e->e.getContent()).collect(Collectors.toList()));
    }

    @Test
    public void test5(){
        //需要进行分组的集合
        List<Integer> targetList = new ArrayList<>();
        targetList.add(1);
        targetList.add(2);
        targetList.add(3);
        targetList.add(4);
        targetList.add(5);
        targetList.add(6);
        //集合分组。2代表 每两个分成一组。已知集合size=6，两个一组即分为三组
        List<List<Integer>> partition = Lists.partition(targetList, 2);
        System.out.println("切分后的数组,index【0】:"+partition.get(0));
        System.out.println("切分后的数组,index【1】:"+partition.get(1));
        System.out.println("切分后的数组,index【2】:"+partition.get(2));

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
