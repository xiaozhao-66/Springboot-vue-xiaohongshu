package com.yanhuo.common.recommend;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 新的推荐方式
 * @author 48423
 */
public class RecommendUtils2 {

    private static final Logger logger = LoggerFactory.getLogger(RecommendUtils2.class);

    public static float[] getEmbeddings(String str) throws MalformedModelException, ModelNotFoundException, IOException, TranslateException {

        SentenceEncoder sentenceEncoder = new SentenceEncoder();
        try (ZooModel<String, float[]> model = ModelZoo.loadModel(sentenceEncoder.criteria());
             Predictor<String, float[]> predictor = model.newPredictor()) {
            float[] embeddings = predictor.predict(str);
            logger.info("Sentence1 embeddings: {}", Arrays.toString(embeddings));
            return embeddings;
        }
    }

    public static float[] getEmbeddings(List<String> keywords) throws MalformedModelException, ModelNotFoundException, IOException, TranslateException {

        StringBuilder sb = new StringBuilder();
        for (String key : keywords) {
            sb.append(key);
            sb.append("-");
        }

        SentenceEncoder sentenceEncoder = new SentenceEncoder();
        try (ZooModel<String, float[]> model = ModelZoo.loadModel(sentenceEncoder.criteria());
             Predictor<String, float[]> predictor = model.newPredictor()) {
            float[] embeddings = predictor.predict(sb.toString());
            logger.info("Sentence1 embeddings: {}", Arrays.toString(embeddings));
            return embeddings;
        }
    }


    public static Double getSimilar(float[] f1, float[] f2){

        return (double) FeatureComparison.cosineSim(f1, f2);
    }

//    public static void main(String[] args) throws ModelException, TranslateException, IOException {
//        String input3 = "计算机，代码，前端，大理，旅游，旅行，广州，拍照，恋爱，异地恋";
//        String input4 = "程序员，编程";
//        String input5 = "女朋友";
//        String input6 = "北京";
//
//        float[] embeddings = getEmbeddings(input3);
//        float[] embeddings1 = getEmbeddings(input4);
//        Double similar = getSimilar(embeddings, embeddings1);
//        System.out.println(similar);
//    }
}
