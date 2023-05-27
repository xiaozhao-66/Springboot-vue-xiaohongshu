package com.xz.recommend.utils;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import java.math.BigInteger;
import java.util.*;

public class RecommendUtils {


    /**
     * 标题名称
     */
    private String topicName;
    /**
     * 分词向量
     */
    private BigInteger bigSimHash;
    /**
     * 初始桶大小
     */
    private Integer hashCount = 64;
    /**
     * 分词最小长度限制
     */
    private static final Integer WORD_MIN_LENGTH = 3;

    private static final BigInteger ILLEGAL_X = new BigInteger("-1");

    public RecommendUtils(String topicName, Integer hashCount) {

        this.topicName = topicName;
        this.bigSimHash = this.simHash();
        this.hashCount = hashCount;
    }

    /**
     * Description:[分词计算向量]
     *
     * @return BigInteger
     * @date 2020-04-01
     * @author huazai
     */
    private BigInteger simHash() {

        // 清除特殊字符
        this.topicName = this.clearSpecialCharacters(this.topicName);
        int[] hashArray = new int[this.hashCount];

        // 对内容进行分词处理
        List<Term> terms = StandardTokenizer.segment(this.topicName);

        // 配置词性权重
        Map<String, Integer> weightMap = new HashMap<>(16, 0.75F);
        weightMap.put("n", 1);
        // 设置停用词
        Map<String, String> stopMap = new HashMap<>(16, 0.75F);
        stopMap.put("w", "");
        // 设置超频词上线
        Integer overCount = 5;

        // 设置分词统计量
        Map<String, Integer> wordMap = new HashMap<>(16, 0.75F);

        for (Term term : terms) {
            // 获取分词字符串
            String word = term.word;
            // 获取分词词性
            String nature = term.nature.toString();

            // 过滤超频词
            if (wordMap.containsKey(word)) {

                Integer count = wordMap.get(word);
                if (count > overCount) {
                    continue;
                } else {
                    wordMap.put(word, count + 1);
                }
            } else {
                wordMap.put(word, 1);
            }

            // 过滤停用词
            if (stopMap.containsKey(nature)) {
                continue;
            }

            // 计算单个分词的Hash值
            BigInteger wordHash = this.getWordHash(word);

            for (int i = 0; i < this.hashCount; i++) {

                // 向量位移
                BigInteger bitMask = new BigInteger("1").shiftLeft(i);

                // 对每个分词hash后的列进行判断，例如：1000...1，则数组的第一位和末尾一位加1,中间的62位减一，也就是，逢1加1，逢0减1，一直到把所有的分词hash列全部判断完

                // 设置初始权重
                Integer weight = 1;
                if (weightMap.containsKey(nature)) {

                    weight = weightMap.get(nature);
                }
                // 计算所有分词的向量
                if (wordHash.and(bitMask).signum() != 0) {
                    hashArray[i] += weight;
                } else {
                    hashArray[i] -= weight;
                }

            }
        }

        // 生成指纹
        BigInteger fingerPrint = new BigInteger("0");
        for (int i = 0; i < this.hashCount; i++) {

            if (hashArray[i] >= 0) {
                fingerPrint = fingerPrint.add(new BigInteger("1").shiftLeft(i));
            }
        }

        return fingerPrint;
    }

    /**
     * Description:[计算单个分词的hash值]
     *
     * @return BigInteger
     * @date 2020-04-01
     * @author huazai
     */
    private BigInteger getWordHash(String word) {

        if (StringUtils.isEmpty(word)) {

            // 如果分词为null，则默认hash为0
            return new BigInteger("0");
        } else {

            // 分词补位，如果过短会导致Hash算法失败
            while (word.length() < RecommendUtils.WORD_MIN_LENGTH) {
                word = word + word.charAt(0);
            }

            // 分词位运算
            char[] wordArray = word.toCharArray();
            BigInteger x = BigInteger.valueOf(wordArray[0] << 7);
            BigInteger m = new BigInteger("1000003");

            // 初始桶pow运算
            BigInteger mask = new BigInteger("2").pow(this.hashCount).subtract(new BigInteger("1"));

            for (char item : wordArray) {
                BigInteger temp = BigInteger.valueOf(item);
                x = x.multiply(m).xor(temp).and(mask);
            }

            x = x.xor(new BigInteger(String.valueOf(word.length())));

            if (x.equals(ILLEGAL_X)) {

                x = new BigInteger("-2");
            }

            return x;
        }
    }

    /**
     * Description:[过滤特殊字符]
     *
     * @return BigInteger
     * @date 2020-04-01
     * @author huazai
     */
    private String clearSpecialCharacters(String topicName) {

        // 将内容转换为小写
        topicName = StringUtils.lowerCase(topicName);

        // 过来HTML标签
        topicName = Jsoup.clean(topicName, Whitelist.none());

        // 过滤特殊字符
        String[] strings = {" ", "\n", "\r", "\t", "\\r", "\\n", "\\t", "&nbsp;", "&amp;", "&lt;", "&gt;", "&quot;", "&qpos;"};
        for (String string : strings) {
            topicName = topicName.replaceAll(string, "");
        }

        return topicName;
    }

    /**
     * Description:[获取标题内容的相似度]
     *
     * @return Double
     * @date 2020-04-01
     * @author huazai
     */
    public Double getSimilar(RecommendUtils simHashUtil) {

        // 获取海明距离
        Double hammingDistance = (double) this.getHammingDistance(simHashUtil);

        // 求得海明距离百分比
        Double scale = (1 - hammingDistance / this.hashCount) * 100;

        Double formatScale = Double.parseDouble(String.format("%.2f", scale));

        return formatScale;
    }

    /**
     * Description:[获取标题内容的海明距离]
     *
     * @return Double
     * @date 2020-04-01
     * @author huazai
     */
    private int getHammingDistance(RecommendUtils simHashUtil) {

        // 求差集
        BigInteger subtract = new BigInteger("1").shiftLeft(this.hashCount).subtract(new BigInteger("1"));

        // 求异或
        BigInteger xor = this.bigSimHash.xor(simHashUtil.bigSimHash).and(subtract);

        int total = 0;
        while (xor.signum() != 0) {
            total += 1;
            xor = xor.and(xor.subtract(new BigInteger("1")));
        }

        return total;
    }


    public static double[] getArray(List<String> s1, Map<String, Integer> map) {
        int length = s1.size();
        double[] res = new double[length];
        for (int i = 0; i < length; i++) {
            if (map.containsKey(s1.get(i))) {
                res[i] = map.get(s1.get(i));
            }
        }

        return res;

    }


    public static void main(String[] args) {
        String s1 = "计算机，代码，前端，大理，旅游，旅行，广州，拍照，恋爱，异地恋";
        String s2 = "程序员，编程";
        String s3 = "女朋友";
        String s4 = "北京";


        //修改内容权重占0.6 标签权重占0.3 分类权重占0.1

        HashMap<String,String> map1 = new HashMap<>();
        map1.put("content","蜡笔小新壁纸");
        map1.put("tags","蜡笔小新动漫-头像");
        map1.put("category","动漫-头像");


        RecommendUtils content1 = new RecommendUtils(map1.get("content"), 64);
        RecommendUtils tags1 = new RecommendUtils(map1.get("tags"), 64);
        RecommendUtils category1 = new RecommendUtils(map1.get("category"), 64);


        HashMap<String,String> map2 = new HashMap<>();
        map2.put("content","蜡笔小新可爱");
        map2.put("tags","动漫");
        map2.put("category","壁纸-头像");

        RecommendUtils content2 = new RecommendUtils(map2.get("content"), 64);
        RecommendUtils tags2 = new RecommendUtils(map2.get("tags"), 64);
        RecommendUtils category2 = new RecommendUtils(map2.get("category"), 64);

        HashMap<String,String> map3 = new HashMap<>();
        map3.put("content","最爱蜡笔小新,壁纸分享");
        map3.put("tags","主题-动漫");
        map3.put("category","蜡笔小新-壁纸");

        RecommendUtils content3 = new RecommendUtils(map3.get("content"), 64);
        RecommendUtils tags3 = new RecommendUtils(map3.get("tags"), 64);
        RecommendUtils category3 = new RecommendUtils(map3.get("category"), 64);

        HashMap<String,String> map4 = new HashMap<>();
        map4.put("content","动漫");
        map4.put("tags","蜡笔小新-头像");
        map4.put("category","壁纸-壁纸");

        RecommendUtils content4 = new RecommendUtils(map4.get("content"), 64);
        RecommendUtils tags4 = new RecommendUtils(map4.get("tags"), 64);
        RecommendUtils category4 = new RecommendUtils(map4.get("category"), 64);


        Double content1Similar = content1.getSimilar(content2);
        Double tags1Similar = tags1.getSimilar(tags2);
        Double category1Similar = category1.getSimilar(category2);

        Double res1 = content1Similar*0.6+tags1Similar*0.3+category1Similar*0.1;
        System.out.println(res1);

        Double content2Similar = content1.getSimilar(content3);
        Double tags2Similar = tags1.getSimilar(tags3);
        Double category2Similar = category1.getSimilar(category3);

        Double res2 = content2Similar*0.6+tags2Similar*0.3+category2Similar*0.1;
        System.out.println(res2);

        Double content3Similar = content1.getSimilar(content4);
        Double tags3Similar = tags1.getSimilar(tags4);
        Double category3Similar = category1.getSimilar(category4);

        Double res3 = content3Similar*0.6+tags3Similar*0.3+category3Similar*0.1;
        System.out.println(res3);

        RecommendUtils mySimHash_1 = new RecommendUtils(s1, 64);
        RecommendUtils mySimHash_2 = new RecommendUtils(s2, 64);
        RecommendUtils mySimHash_3 = new RecommendUtils(s3, 64);
        RecommendUtils mySimHash_4 = new RecommendUtils(s4, 64);
        Double similar1 = mySimHash_1.getSimilar(mySimHash_2);
        Double similar2 = mySimHash_1.getSimilar(mySimHash_3);
        Double similar3 = mySimHash_1.getSimilar(mySimHash_4);
        System.out.println(similar1+"-"+similar2+"-"+similar3);

 }

}
