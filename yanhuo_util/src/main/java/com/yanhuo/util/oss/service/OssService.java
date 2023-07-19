package com.yanhuo.util.oss.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.List;

/**
 * @author 48423
 */
public interface OssService {
    /**
     * 上传文件
     *
     * @param file
     * @param type
     * @return
     */
    String uploadFile(MultipartFile file, Integer type);

    /**
     * 批量上传文件
     *
     * @param request
     * @param num
     * @param type
     * @return
     */
    List<String> uploadFiles(MultipartRequest request, Integer num, Integer type);

    /**
     * 删除文件
     *
     * @param fileName
     * @param type
     * @return
     */
    void deleteFile(String fileName, Integer type);

    /**
     * 批量删除文件
     *
     * @param fileNames
     * @param type
     * @return
     */
    void deleteFiles(List<String> fileNames, Integer type);
}
