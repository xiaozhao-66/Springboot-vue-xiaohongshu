package com.yanhuo.recommend.utils;

import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.extractor.impl.TextRankKeywordsExtractor;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class KeywordsExtractor {


    public static List<String> getKeywords(String content){
        SegmenterConfig config = new SegmenterConfig(true);
        config.setClearStopwords(true);
        config.setAppendCJKSyn(false);
        config.setKeepUnregWords(false);
        ADictionary dic = DictionaryFactory.createSingletonDictionary(config);

        try {
            ISegment seg = ISegment.COMPLEX.factory.create(config, dic);
            TextRankKeywordsExtractor extractor = new TextRankKeywordsExtractor(seg);
            extractor.setMaxIterateNum(100);
            extractor.setWindowSize(5);
            extractor.setKeywordsNum(15);

            return extractor.getKeywordsFromString(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

//    public static void main(String[] args)
//    {
//       String doc = "蜡笔小新壁纸头像动漫壁纸蜡笔小新";
//
//        List<String> keywords = getKeywords(doc);
//        System.out.println(keywords);
//
//    }
}
