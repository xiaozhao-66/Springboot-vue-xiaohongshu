package com.xz.utils.oss.controller;

import com.xz.common.utils.Result;
import com.xz.utils.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.List;

@RestController
@RequestMapping("/qiniufileoss")
public class QiniuOssController {

    @Autowired
    private OssService ossService;

    //上传文件
    @PostMapping("uploadOssFile")
    public Result<?> uploadOssFile(MultipartFile file) {
        String url = ossService.qiNiuUploadFile(file);
        return new Result<String>().ok(url);
    }

    //上传多张图片
    @PostMapping("uploadOssFiles")
    public Result<?> uploadOssFiles(MultipartRequest request, Integer num) {

        List<String> paths = ossService.qiNiuUploadFiles(request, num);

        return new Result<List<String>>().ok(paths);
    }

    //删除文件
    @RequestMapping("deleteFile")
    public Result<?> deleteFile(@RequestParam String fileName) {
        ossService.qiNiuDeleteFile(fileName);
        return new Result<>().ok();
    }

    //删除文件
    @RequestMapping("deleteFiles")
    public Result<?> deleteFiles(List<String> fileNames) {
        ossService.qiNiuDeleteFiles(fileNames);
        return new Result<>().ok();
    }
}
