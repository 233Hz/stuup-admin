package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.exception.ExcelTitleException;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.api.config.PropertiesConfig;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.model.dto.PoliticalExcelDTO;
import com.poho.stuup.model.dto.PoliticalSearchDTO;
import com.poho.stuup.service.IPoliticalService;
import com.poho.stuup.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Api(tags = "学生党团活动信息相关接口")
@RestController
@RequestMapping("/political")
public class PoliticalController {

    private static final Logger logger = LoggerFactory.getLogger(PoliticalController.class);

    @Resource
    private PropertiesConfig config;

    @Resource
    private IPoliticalService politicalService;

    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true)})
    @ApiOperation(value = "获取列表", httpMethod = "GET")
    @GetMapping("/list")
    public ResponseModel list(PoliticalSearchDTO searchDTO) {
        return politicalService.findDataPageResult(searchDTO);
    }

    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true)})
    @ApiOperation(value = "导入", httpMethod = "POST")
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
                String fileName = System.currentTimeMillis() + "_political" + fix;
                File file = new File(path, fileName);
                FileCopyUtils.copy(importFile.getBytes(), file);
                String[] headNames = {"学籍号", "党团活动", "开始时间", "结束时间", "级别", "角色", "组织机构"};
                String[] fieldNames = {"stuNo", "title", "startDate", "endDate", "level", "role", "orgName"};
                List<PoliticalExcelDTO> list = new ExcelUtil().readExcel(file.getPath(), PoliticalExcelDTO.class, headNames, fieldNames);
                if (MicrovanUtil.isNotEmpty(list)) {
                    Map<String, Object> result = politicalService.importList(list);
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
        } catch (ExcelTitleException e) {
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
