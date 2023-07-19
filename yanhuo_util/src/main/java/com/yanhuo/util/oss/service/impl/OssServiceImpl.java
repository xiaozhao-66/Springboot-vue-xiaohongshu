package com.yanhuo.util.oss.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.yanhuo.util.constant.OssConstant;
import com.yanhuo.util.oss.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OssServiceImpl implements OssService {

    // 工具类获取值
    String endpoint = OssConstant.ENDPOINT;
    String accessKeyId = OssConstant.KEY_ID;
    String accessKeySecret = OssConstant.KEY_SECRET;
    String bucketName = OssConstant.BUCKET_NAME;

    //上传到oss
    @Override
    public String uploadFile(MultipartFile file, Integer type) {
        String url;
        if (type == OssConstant.CloudService.ALIYUN.getValue()) {
            url = aliYunUploadFile(file);
        } else {
            url = qiNiuUploadFile(file);
        }
        return url;
    }

    @Override
    public List<String> uploadFiles(MultipartRequest request, Integer num, Integer type) {

        List<MultipartFile> files = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        if (num > 0) {
            for (int i = 0; i < num; i++) {
                files.add(request.getFile("images" + i));
            }
            for (MultipartFile file : files) {
                if (file != null && file.getSize() > 0) {
                    String path = uploadFile(file, type);
                    paths.add(path);
                }
            }
        }
        return paths;
    }

    @Override
    public void deleteFile(String fileName, Integer type) {
        if (type == OssConstant.CloudService.ALIYUN.getValue()) {
            aliYunDeleteFile(fileName);
        } else {
            qiNiuDeleteFile(fileName);
        }
    }

    @Override
    public void deleteFiles(List<String> fileNames, Integer type) {
        for (String fileName : fileNames) {
            deleteFile(fileName, type);
        }
    }


    public String aliYunUploadFile(MultipartFile file) {
        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String fileName = file.getOriginalFilename();

            //1 在文件名称里面添加随机唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // yuy76t5rew01.jpg
            fileName = uuid + fileName;

            //2 把文件按照日期进行分类
            //获取当前日期
            //   2019/11/12
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            //  2019/11/12/ewtqr313401.jpg
            fileName = datePath + "/" + fileName;

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
            //第三个参数  上传文件输入流
//            ObjectMetadata meta = new ObjectMetadata();
//            meta.setContentType("image/jpg");
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //  https://edu-guli-1010.oss-cn-beijing.aliyuncs.com/01.jpg
            return "https://" + bucketName + "." + endpoint + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void aliYunDeleteFile(String fileName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            String replaceFileName = fileName.replace("https://" + bucketName + "." + endpoint + "/", "");
            ossClient.deleteObject(bucketName, replaceFileName);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());

        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


    public String qiNiuUploadFile(MultipartFile file) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.beimei());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本

        UploadManager uploadManager = new UploadManager(cfg);

        String filePath = "";
        String fileName = file.getOriginalFilename();

        //1 在文件名称里面添加随机唯一的值
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // yuy76t5rew01.jpg
        fileName = uuid + fileName;

        //2 把文件按照日期进行分类
        //获取当前日期
        //   2019/11/12
        String datePath = new DateTime().toString("yyyy/MM/dd");
        //拼接
        //  2019/11/12/ewtqr313401.jpg
        fileName = datePath + "/" + fileName;

        try {
            byte[] uploadBytes = file.getBytes();
            // 获取文件流
            InputStream input = new ByteArrayInputStream(uploadBytes);
            Auth auth = Auth.create(accessKeyId, accessKeySecret);
            String upToken = auth.uploadToken(bucketName);
            Response response = uploadManager.put(input, fileName, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            filePath = "http://" + endpoint + "/" + putRet.key;
            return filePath;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public void qiNiuDeleteFile(String fileName) {
        String replaceFileName = fileName.replace("http://" + endpoint + "/", "");
        Configuration cfg = new Configuration(Region.beimei());

        Auth auth = Auth.create(accessKeyId, accessKeySecret);
        BucketManager bucketManager = new BucketManager(auth, cfg);

        try {
            bucketManager.delete(bucketName, replaceFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
