package com.yanhuo.util.oss.controller;

import com.yanhuo.common.result.Result;
import com.yanhuo.util.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.List;

/**
 * oss对象存储模块
 */
@RestController
@RequestMapping("/api/util/fileoss")
public class OssController {

    @Autowired
    private OssService ossService;

    /**
     * 上传文件
     *
     * @param file
     * @param type
     * @return
     */
    @PostMapping("uploadOssFile/{type}")
    public Result<?> uploadOssFile(MultipartFile file, @PathVariable Integer type) {
        String url = ossService.uploadFile(file, type);
        return Result.ok(url);
    }

    /**
     * 批量上传文件
     *
     * @param request
     * @param num
     * @param type
     * @return
     */
    @PostMapping("uploadOssFiles/{type}")
    public Result<?> uploadOssFiles(MultipartRequest request, Integer num, @PathVariable Integer type) {
        List<String> paths = ossService.uploadFiles(request, num, type);
        return Result.ok(paths);
    }

    /**
     * 删除文件
     *
     * @param fileName
     * @param type
     * @return
     */
    @RequestMapping("deleteFile/{type}")
    public Result<?> deleteFile(@RequestParam String fileName, @PathVariable Integer type) {
        ossService.deleteFile(fileName, type);
        return Result.ok(null);
    }

    /**
     * 批量删除文件
     *
     * @param fileNames
     * @param type
     * @return
     */
    @RequestMapping("deleteFiles/{type}")
    public Result<?> deleteFiles(List<String> fileNames, @PathVariable Integer type) {
        ossService.deleteFiles(fileNames, type);
        return Result.ok(null);
    }
}
