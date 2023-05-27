package com.xz.recommend.utils;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CW
 * @version 1.0
 * @date 2023/3/1 8:41
 * @desc ik分词工具类
 */
public class IKUtil {

    /**
     * 分词
     *
     * @param keyword 需要分词的文本
     * @return
     */
    public static List<String> splitKeyWord(String keyword) throws IOException {

        ArrayList<String> result = new ArrayList<>();
        // 创建一个reader对象
        StringReader reader = new StringReader(keyword);
        // 创建一个分词对象
        IKSegmenter ikSegmenter = new IKSegmenter(reader, false);
        Lexeme next = ikSegmenter.next();

        while (next != null) {
            // 获取分词的结果
            result.add(next.getLexemeText());
            next = ikSegmenter.next();
        }
        return result;
    }
//    public static void main(String[] args) throws IOException {
//        List<String> strings = splitKeyWord("蜡笔小新壁纸|动漫-蜡笔小新|蜡笔小新-动漫-头像");
//        System.out.println(strings);
//    }
}


