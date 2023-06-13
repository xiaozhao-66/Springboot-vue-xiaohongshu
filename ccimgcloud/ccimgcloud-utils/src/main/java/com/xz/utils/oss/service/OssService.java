package com.xz.utils.oss.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.List;

public interface OssService {
    //上传头像到oss
    String uploadFile(MultipartFile file);

    List<String> uploadFiles(MultipartRequest request, Integer num);

    void deleteFile(String fileName);

    void deleteFiles(List<String> fileNames);

    String qiNiuUploadFile(MultipartFile file);

    List<String> qiNiuUploadFiles(MultipartRequest request, Integer num);

    void qiNiuDeleteFile(String fileName);

    void qiNiuDeleteFiles(List<String> fileNames);
}
