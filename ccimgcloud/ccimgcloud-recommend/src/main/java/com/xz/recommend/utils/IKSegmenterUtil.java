package com.xz.recommend.utils;



import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.cfg.Configuration;

/**
 * 测试 IK Analyzer 分词架构中的独立使用分词方法 IK Segmenter
 * 需要加载 IKAnalyzer2012_u6.jar
 * @author zsoft
 */
public class IKSegmenterUtil {

    private static Configuration m_wordCut_cfg;


    public static List<String> parse(String content, boolean useSmart) {
        ArrayList<String> result = new ArrayList<>();
        StringReader sr = new StringReader(content);
        // 参数2为是否使用智能分词
        // true：使用智能分词
        // false：使用最细粒度分词
        IKSegmenter ikSegmenter = new IKSegmenter(sr, useSmart);
        Lexeme word = null;

        try {
            while ((word = ikSegmenter.next()) != null) {
                result.add(word.getLexemeText());
            }
        } catch (Exception e) {
            throw new RuntimeException("分词错误");
        }

        return result;
    }

//    public static void main(String[] args) {
//        m_wordCut_cfg = DefaultConfig.getInstance();
//        System.out.println(m_wordCut_cfg.getQuantifierDicionary());
//
//        String text = "蜡笔小新壁纸|动漫-蜡笔小新|蜡笔小新-动漫-头像点赞";
//
//        try {
//
//            List<String> parse = IKSegmenterUtil.parse(text, true);
//            System.out.println(parse);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}

