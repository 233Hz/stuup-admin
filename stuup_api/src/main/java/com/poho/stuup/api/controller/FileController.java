package com.poho.stuup.api.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.api.config.MinioConfig;
import com.poho.stuup.model.File;
import com.poho.stuup.model.vo.FileVO;
import com.poho.stuup.service.FileService;
import com.poho.stuup.util.MinioUtils;
import com.poho.stuup.util.ProjectUtil;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author BUNGA
 * @description: 文件相关接口
 * @date 2023/6/14 15:55
 */

@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private MinioConfig prop;

    @Resource
    private FileService fileService;

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
    public ResponseModel<FileVO> upload(@RequestParam(name = "file") MultipartFile multipartFile) {
        String originName = multipartFile.getOriginalFilename();
        MinioUtils.createBucket(prop.getBucketName());
        String fileName = UUID.randomUUID().toString().replace("-", "") + StrUtil.DOT + FileUtil.extName(originName);
        MinioUtils.uploadFile(prop.getBucketName(), multipartFile, fileName);
        String url = MinioUtils.getPreSignedObjectUrl(prop.getBucketName(), fileName);
        // 保存文件信息
        String userId = ProjectUtil.obtainLoginUser(request);
        File file = new File();
        file.setStorageName(fileName);
        file.setOriginalName(originName);
        file.setBucket(prop.getBucketName());
        file.setSuffix(FileUtil.extName(originName));
        file.setCreateUser(Long.valueOf(userId));
        fileService.save(file);
        // 返回文件信息
        FileVO fileVO = new FileVO();
        fileVO.setId(file.getId());
        fileVO.setStorageName(fileName);
        fileVO.setOriginalName(originName);
        fileVO.setBucket(prop.getBucketName());
        fileVO.setSuffix(FileUtil.extName(originName));
        fileVO.setUrl(url);
        return ResponseModel.ok(fileVO);
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

    @GetMapping("/files/{ids}")
    public ResponseModel<List<File>> getFileListForIds(@PathVariable("ids") String ids) {
        List<Long> fileIds = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        return ResponseModel.ok(fileService.list(Wrappers.<File>lambdaQuery().in(File::getId, fileIds).orderByAsc(File::getCreateTime)));
    }

}
