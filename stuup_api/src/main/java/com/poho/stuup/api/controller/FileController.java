package com.poho.stuup.api.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.api.config.MinioConfig;
import com.poho.stuup.util.MinioUtils;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author BUNGA
 * @description: 文件相关接口
 * @date 2023/6/14 15:55
 */

@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private MinioConfig prop;

    /**
     * @description: 判断Bucket是否存在
     * @param: bucketName
     * @return: com.poho.common.custom.ResponseModel<java.lang.Boolean>
     * @author BUNGA
     * @date: 2023/6/14 16:24
     */
    @SneakyThrows
    @GetMapping("/bucketExists")
    public ResponseModel<Boolean> bucketExists(@RequestParam("bucketName") String bucketName) {
        return ResponseModel.ok(MinioUtils.bucketExists(bucketName));
    }

    /**
     * @description: 创建桶
     * @param: bucketName
     * @return: com.poho.common.custom.ResponseModel<java.lang.Boolean>
     * @author BUNGA
     * @date: 2023/6/14 16:42
     */
    @SneakyThrows
    @GetMapping("/createBucket")
    public ResponseModel<Boolean> createBucket(@RequestParam("bucketName") String bucketName) {
        MinioUtils.createBucket(bucketName);
        return ResponseModel.ok(null, "创建成功");
    }

    /**
     * @description: 上传文件
     * @param: multipartFile
     * @return: com.poho.common.custom.ResponseModel<java.util.Map < java.lang.String, java.lang.Object>>
     * @author BUNGA
     * @date: 2023/6/14 16:24
     */
    @SneakyThrows
    @PostMapping(value = "/upload")
    public ResponseModel<Map<String, Object>> upload(@RequestParam(name = "file") MultipartFile multipartFile) {
        String originName = multipartFile.getOriginalFilename();
        MinioUtils.createBucket(prop.getBucketName());
        String fileName = UUID.randomUUID().toString().replace("-", "") + StrUtil.DOT + FileUtil.extName(originName);
        Map<String, Object> resultMap = new HashMap<>();
        MinioUtils.uploadFile(prop.getBucketName(), multipartFile, fileName);
        String url = MinioUtils.getPreSignedObjectUrl(prop.getBucketName(), fileName);
        resultMap.put("buketName", prop.getBucketName());
        resultMap.put("originName", originName);
        resultMap.put("fileName", fileName);
        resultMap.put("url", url);
        return ResponseModel.ok(resultMap);
    }

    /**
     * @description: 下载文件
     * @param: fileName
     * @param: response
     * @return: void
     * @author BUNGA
     * @date: 2023/6/14 16:24
     */
    @GetMapping(value = "/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        MinioUtils.download(prop.getBucketName(), fileName, response);
    }

    /**
     * @description: 删除文件
     * @param: bucketName
     * @param: fileName
     * @return: com.poho.common.custom.ResponseModel<java.lang.Boolean>
     * @author BUNGA
     * @date: 2023/6/14 16:49
     */
    @SneakyThrows
    @GetMapping("/delFile")
    public ResponseModel<Boolean> delFile(@RequestParam("fileName") String fileName) {
        MinioUtils.removeFile(prop.getBucketName(), fileName);
        return ResponseModel.ok(null, "删除成功");

    }

    /**
     * @description: 获取文件url
     * @param: fileName
     * @return: com.poho.common.custom.ResponseModel<java.lang.String>
     * @author BUNGA
     * @date: 2023/6/14 16:23
     */
    @SneakyThrows
    @GetMapping("/getFileUrl")
    public ResponseModel<String> getFileUrl(@RequestParam("fileName") String fileName) {
        String url = MinioUtils.getPreSignedObjectUrl(prop.getBucketName(), fileName, 60 * 30);
        return ResponseModel.ok(url, "获取成功");
    }

}
