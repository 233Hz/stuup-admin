package com.poho.stuup.api.controller;

import com.poho.common.custom.ExcelExportTemplateEnum;
import com.poho.common.custom.ExcelImportTemplateEnum;
import com.poho.stuup.model.dto.*;
import com.poho.stuup.service.IExportService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;


@Controller
@RequestMapping("/download")
public class DownloadController {

    @Resource
    private IExportService exportService;

    @Resource
    private HttpServletResponse response;

    /**
     * 下载导入模板
     *
     * @return
     */
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "下载接口", httpMethod = "POST")
    @RequestMapping(value = "/template/{param}", method = RequestMethod.POST)
    public void template(@PathVariable String param) throws Exception {
        String templatePath = "";
        String finalName = "";
        ExcelImportTemplateEnum excelTemplateEnum = ExcelImportTemplateEnum.valueOf(param);
        if (excelTemplateEnum != null) {
            templatePath = excelTemplateEnum.getPath();
            finalName = excelTemplateEnum.getName();
        }
        finalName = URLEncoder.encode(finalName.toString(), "UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + finalName);
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        ClassPathResource resource = new ClassPathResource(templatePath);
        BufferedInputStream inputStream = new BufferedInputStream(resource.getInputStream());
        ServletOutputStream outputStream = response.getOutputStream();
        int b = 0;
        byte[] buffer = new byte[1024];
        while ((b = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, b);
        }
        inputStream.close();
        if (outputStream != null) {
            outputStream.flush();
            outputStream.close();
        }
    }

    //奖励信息导出
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @PostMapping("/exportReward")
    public void exportReward(@RequestBody RewardSearchDTO searchDTO) {
        String templatePath = ExcelExportTemplateEnum.REWARD.getPath();
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exportService.exportReward( inputStream, response, searchDTO);
    }

    //技能大赛信息导出
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @PostMapping("/exportContest")
    public void exportContest(@RequestBody ContestSearchDTO searchDTO) {
        String templatePath = ExcelExportTemplateEnum.CONTEST.getPath();
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exportService.exportContest(inputStream, response, searchDTO);
    }

    //考证信息导出
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @PostMapping("/exportCertificate")
    public void exportCertificate(@RequestBody CertificateSearchDTO searchDTO) {
        String templatePath = ExcelExportTemplateEnum.CERTIFICATE.getPath();
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exportService.exportCertificate(inputStream, response, searchDTO);
    }

    //军训信息导出
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @PostMapping("/exportMilitary")
    public void exportMilitary(@RequestBody MilitarySearchDTO searchDTO) {
        String templatePath = ExcelExportTemplateEnum.MILITARY.getPath();
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exportService.exportMilitary(inputStream, response, searchDTO);
    }

    //党团活动信息导出
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @PostMapping("/exportPolitical")
    public void exportPolitical(@RequestBody PoliticalSearchDTO searchDTO) {
        String templatePath = ExcelExportTemplateEnum.POLITICAL.getPath();
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exportService.exportPolitical(inputStream, response, searchDTO);
    }

    //志愿者服务信息导出
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @PostMapping("/exportVolunteer")
    public void exportVolunteer(@RequestBody VolunteerSearchDTO searchDTO) {
        String templatePath = ExcelExportTemplateEnum.VOLUNTEER.getPath();
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exportService.exportVolunteer(inputStream, response, searchDTO);
    }

}
