package com.xz.recommend.Controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.xz.recommend.Dto.UserSet;
import com.xz.recommend.Service.RecommendServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/alipay")
public class RecommendController {

//    String string = "";
//    List<Goods> res;
//    @Resource
//    GoodsMapper goodsMapper;

//    @GetMapping("/recommend")
//    public Result<?> recommend(){
//        //输入用户总量
//        UserSet userSet = new UserSet();
//
//        userSet.put("swl")
//                .set("斗破苍穹漫画全套1-60册 全集60本", 50)
//                .set("哈利波特", 30)
//                .set("重生从手术室开始", 45)
//                .set("福尔摩斯探案集", 50)
//                .set("人间失格 罗生门 我是猫 月亮与六便士", 30)
//                .set("希望之线（东野圭吾新代表作，高分作品TOP5！）", 45)
//                .set("现货【全译本无删减】红与黑书原版全集", 50)
//                .create();
//
//        userSet.put("admin")
//                .set("百年孤独", 40)
//                .set("重生从手术室开始", 30)
//                .set("人间失格 罗生门 我是猫 月亮与六便士", 50)
//                .set("希望之线（东野圭吾新代表作，高分作品TOP5！）", 50)
//                .set("现货【全译本无删减】红与黑书原版全集", 30)
//                .set("斗罗大陆 （唐家三少第九部长篇小说）", 30)
//                .create();
//
//
//        userSet.put("tom")
//                .set("百年孤独", 20)
//                .set("斗破苍穹漫画全套1-60册 全集60本", 50)
//                .set("人间失格 罗生门 我是猫 月亮与六便士", 30)
//                .set("福尔摩斯探案集", 50)
//                .set("希望之线（东野圭吾新代表作，高分作品TOP5！）", 45)
//                .set("傲慢与偏见 （简·奥斯汀著长篇小说）", 50)
//                .create();
//
//        userSet.put("hello")
//                .set("百年孤独", 50)
//                .set("斗破苍穹漫画全套1-60册 全集60本", 30)
//                .set("人间失格 罗生门 我是猫 月亮与六便士", 40)
//                .set("匆匆那年", 40)
//                .set("傲慢与偏见 （简·奥斯汀著长篇小说）", 35)
//                .set("现货【全译本无删减】红与黑书原版全集", 35)
//                .set("斗罗大陆 （唐家三少第九部长篇小说）", 45)
//                .create();
//
//        userSet.put("jack")
//                .set("百年孤独", 20)
//                .set("斗破苍穹漫画全套1-60册 全集60本", 40)
//                .set("重生从手术室开始", 45)
//                .set("福尔摩斯探案集", 50)
//                .set("人间失格 罗生门 我是猫 月亮与六便士", 20)
//                .create();
//
//        userSet.put("jerry")
//                .set("重生从手术室开始", 50)
//                .set("盗梦空间", 50)
//                .set("人间失格 罗生门 我是猫 月亮与六便士", 30)
//                .set("傲慢与偏见 （简·奥斯汀著长篇小说）", 50)
//                .set("蚁人", 45)
//                .set("福尔摩斯探案集", 40)
//                .set("斗罗大陆 （唐家三少第九部长篇小说）", 35)
//                .create();
//
//        userSet.put("马保国")
//                .set("重生从手术室开始", 50)
//                .set("斗破苍穹漫画全套1-60册 全集60本", 40)
//                .set("人间失格 罗生门 我是猫 月亮与六便士", 10)
//                .set("Phoenix", 50)
//                .set("甄嬛传", 40)
//                .set("The Strokes", 50)
//                .create();
//
//        userSet.put("卢本伟牛逼")
//                .set("百年孤独", 40)
//                .set("人间失格 罗生门 我是猫 月亮与六便士", 45)
//                .set("匆匆那年", 45)
//                .set("甄嬛传", 25)
//                .set("The Strokes", 30)
//                .create();
//
//        RecommendServiceImpl recommend = new RecommendServiceImpl();
//
//        List<UserSet.Set> recommendations = recommend.recommend("swl", userSet);
//
//        System.out.println("-----------------------");
//
//        List<Goods> res = new ArrayList<Goods>() ;
//
//        for (UserSet.Set set : recommendations) {
//
//            string = string + set.username+" "+set.score + '\n';
////            Goods goods = goodsMapper.selectOne(Wrappers.<Goods>lambdaQuery().eq(Goods::getName, set.username));
//
////            res.add(goods);
////            System.out.println(set.username+" "+set.score );
//        }
//
//        System.out.println("Do it,just do it");
//
//        return new Result<>().ok(res);
    //}
}
