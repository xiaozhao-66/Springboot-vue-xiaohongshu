package com.xz.common.recommend;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * 句向量 支持 15 languages: Arabic, Chinese, Dutch, English, French, German, Italian, Korean, Polish,
 * Portuguese, Russian, Spanish, Turkish.
 *
 * Sentence Encoder - support 15 languages: Arabic, Chinese, Dutch, English, French, German, Italian, Korean, Polish,
 * Portuguese, Russian, Spanish, Turkish.
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class SentenceEncoderExample {

  private static final Logger logger = LoggerFactory.getLogger(SentenceEncoderExample.class);

  private SentenceEncoderExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String input1 = "This model generates embeddings for input sentence";
    String input2 = "This model generates embeddings";
    String input3 = "计算机|代码|前端|大理|旅游|旅行|广州|拍照|恋爱|异地恋|";
    String input4 = "程序员|编程|学习|";
    String input5 = "女朋友|异地恋|";
    String input6 = "北京|旅游|";

    logger.info("input Sentence1: {}", input1);
    logger.info("input Sentence2: {}", input2);

    logger.info("input Sentence3: {}", input3);
    logger.info("input Sentence4: {}", input4);

    SentenceEncoder sentenceEncoder = new SentenceEncoder();
    try (ZooModel<String, float[]> model = ModelZoo.loadModel(sentenceEncoder.criteria());
         Predictor<String, float[]> predictor = model.newPredictor()) {

      float[] embeddings1 = predictor.predict(input1);
      logger.info("Vector dimensions: {}", embeddings1.length);
      logger.info("Sentence1 embeddings: {}", Arrays.toString(embeddings1));
      float[] embeddings2 = predictor.predict(input2);
      logger.info("Sentence2 embeddings: {}", Arrays.toString(embeddings2));

      logger.info("English Similarity: {}", FeatureComparison.cosineSim(embeddings1, embeddings2));

      float[] embeddings3 = predictor.predict(input3);
      logger.info("Sentence1 embeddings: {}", Arrays.toString(embeddings3));
      float[] embeddings4 = predictor.predict(input4);
      logger.info("Sentence2 embeddings: {}", Arrays.toString(embeddings4));
      float[] embeddings5 = predictor.predict(input5);
      logger.info("Sentence2 embeddings: {}", Arrays.toString(embeddings5));
      float[] embeddings6 = predictor.predict(input6);
      logger.info("Sentence2 embeddings: {}", Arrays.toString(embeddings6));

      logger.info("Chinese Similarity: {}", FeatureComparison.cosineSim(embeddings3, embeddings4));
      logger.info("Chinese Similarity: {}", FeatureComparison.cosineSim(embeddings3, embeddings5));
      logger.info("Chinese Similarity: {}", FeatureComparison.cosineSim(embeddings3, embeddings6));
    }
  }
}
