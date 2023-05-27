package com.xz.utils.oss.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.List;
import java.util.Map;

public interface OssService {
    //上传头像到oss
    String uploadFile(MultipartFile file);

    List<String> uploadFiles(MultipartRequest request, Integer num);

    void deleteFile(String fileName);

    void deleteFiles(String[] fileNames);

}
