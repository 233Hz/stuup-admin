package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.api.config.PropertiesConfig;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.custom.CusUploadResult;
import io.swagger.annotations.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 18:13 2019-06-09
 * @Modified By:
 */
@Api(tags = "文件上传相关接口")
@RestController
@RequestMapping("/upload")
public class UploadController {
    /** 支持上传的图片格式 */
    private static String[] supportPicArr = new String[]{".jpg", ".jpeg", ".png", ".bmp"};
    /** 支持上传的文件格式 */
    private static String[] supportDocArr = new String[]{".jpg", ".jpeg", ".png", ".bmp", ".txt", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf"};
    @Resource
    private PropertiesConfig propertiesConfig;

    /**
     * 图片上传
     * @param picFile
     * @return
     */
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "上传图片", httpMethod = "POST")
    @RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
    public ResponseModel<CusUploadResult> uploadPic(@ApiParam(required = true, value = "图片") @RequestParam("file") MultipartFile picFile) {
        ResponseModel model = new ResponseModel();
        if (picFile.isEmpty()) {
            model.setMsg("请选择图片");
            model.setCode(CommonConstants.CODE_EXCEPTION);
        } else {
            try {
                String picPath = propertiesConfig.getBaseDoc() + File.separator + ProjectConstants.PROJECT_PIC;
                MicrovanUtil.createFolder(picPath);

                String fileName = picFile.getOriginalFilename();
                String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                if (Arrays.asList(supportPicArr).contains(suffix)) {
                    String newFileName = System.currentTimeMillis() + suffix;
                    File file = new File(picPath, newFileName);
                    FileCopyUtils.copy(picFile.getBytes(), file);
                    //file.transferTo(dest);
                    model.setCode(CommonConstants.CODE_SUCCESS);

                    String path = ProjectConstants.PROJECT_PIC + "/" + newFileName;
                    String url = propertiesConfig.getBaseUrl() + path;
                    model.setData(new CusUploadResult(url, path, fileName));
                    model.setMsg("上传成功");
                } else {
                    model.setMsg("只支持"+Arrays.toString(supportPicArr)+"格式的图片");
                    model.setCode(CommonConstants.CODE_EXCEPTION);
                }
            } catch (IOException e) {
                model.setMsg("上传失败，请稍后重试");
                model.setCode(CommonConstants.CODE_EXCEPTION);
                e.printStackTrace();
            }
        }
        return model;
    }

    /**
     * 文件上传
     * @param picFile
     * @return
     */
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "上传文件", httpMethod = "POST")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseModel<CusUploadResult> uploadFile(@ApiParam(required = true, value = "文件") @RequestParam("file") MultipartFile picFile) {
        ResponseModel model = new ResponseModel();
        if (picFile.isEmpty()) {
            model.setMsg("请选择文件");
            model.setCode(CommonConstants.CODE_EXCEPTION);
        } else {
            try {
                String picPath = propertiesConfig.getBaseDoc() + File.separator + ProjectConstants.PROJECT_COMMON;
                MicrovanUtil.createFolder(picPath);

                String fileName = picFile.getOriginalFilename();
                String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                if (Arrays.asList(supportDocArr).contains(suffix)) {
                    String newFileName = System.currentTimeMillis() + suffix;
                    File file = new File(picPath, newFileName);
                    FileCopyUtils.copy(picFile.getBytes(), file);

                    String path = ProjectConstants.PROJECT_COMMON + "/" + newFileName;
                    String url = propertiesConfig.getBaseUrl() + path;
                    model.setCode(CommonConstants.CODE_SUCCESS);
                    model.setMsg("上传成功");
                    model.setData(new CusUploadResult(url, path, fileName));
                } else {
                    model.setMsg("只支持"+Arrays.toString(supportDocArr)+"格式的文件");
                    model.setCode(CommonConstants.CODE_EXCEPTION);
                }
            } catch (IOException e) {
                model.setMsg("上传失败，请稍后重试");
                model.setCode(CommonConstants.CODE_EXCEPTION);
                e.printStackTrace();
            }
        }
        return model;
    }
}
