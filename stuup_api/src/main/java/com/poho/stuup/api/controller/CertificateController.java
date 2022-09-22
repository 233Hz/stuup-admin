package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.exception.ExcelTitleException;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.api.config.PropertiesConfig;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.model.Certificate;
import com.poho.stuup.model.User;
import com.poho.stuup.model.dto.CertificateExcelDTO;
import com.poho.stuup.model.dto.RewardExcelDTO;
import com.poho.stuup.service.ICertificateService;
import com.poho.stuup.util.ExcelUtil;
import com.poho.stuup.util.ProjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Api(tags = "学生考证信息相关接口")
@RestController
@RequestMapping("/certificate")
public class CertificateController {

    private static final Logger logger = LoggerFactory.getLogger(CertificateController.class);

    @Resource
    private PropertiesConfig config;

    @Resource
    private ICertificateService certificateService;

    @ApiOperation(value = "获取列表", httpMethod = "GET")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list(String name, String current, String size) {
        int page = ProjectUtil.getPageNum(current);
        int pageSize = ProjectUtil.getPageSize(size);
        return certificateService.findDataPageResult(name, page, pageSize);
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseModel importData(@RequestParam("importFile") MultipartFile importFile) {
        ResponseModel<Map<String, Object>> model = new ResponseModel<>();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMessage("导入失败，请稍后重试");
        try {
            String path = config.getBaseDoc() + File.separator + ProjectConstants.PROJECT_TEMP;
            MicrovanUtil.createFolder(path);

            String fix = importFile.getOriginalFilename().substring(importFile.getOriginalFilename().lastIndexOf(".")).toLowerCase();
            if (".xls".equals(fix) || ".xlsx".equals(fix)) {
                String fileName = System.currentTimeMillis() + "_certificate" + fix;
                File file = new File(path, fileName);
                FileCopyUtils.copy(importFile.getBytes(), file);
                String[] headNames = {"学籍号", "证书名称", "专业大类", "办证单位", "级别", "颁证日期", "证书编号"};
                String[] fieldNames = {"stuNo", "name", "major", "unitName", "level", "obtainDate", "certNo"};
                List<CertificateExcelDTO> list = new ExcelUtil().readExcel(file.getPath(), CertificateExcelDTO.class, headNames, fieldNames);
                if (MicrovanUtil.isNotEmpty(list)) {
                    Map<String, Object> result = certificateService.importList(list);
                    model.setCode(CommonConstants.CODE_SUCCESS);
                    model.setMessage("导入成功");
                    model.setData(result);
                } else {
                    model.setMessage("未读取到数据");
                    model.setCode(CommonConstants.CODE_EXCEPTION);
                }
            } else {
                model.setMessage("请选择Excel文件");
                model.setCode(CommonConstants.CODE_EXCEPTION);
            }
        } catch (ExcelTitleException e ) {
            model.setMessage(e.getMessage());
            model.setCode(CommonConstants.CODE_EXCEPTION);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

}
