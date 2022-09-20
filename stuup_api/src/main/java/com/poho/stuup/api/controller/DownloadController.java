package com.poho.stuup.api.controller;

import com.poho.common.custom.ExcelExportTemplateEnum;
import com.poho.common.custom.ExcelImportTemplateEnum;
import com.poho.stuup.service.IExportService;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;


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
    @RequestMapping(value = "/exportReward", method = RequestMethod.POST)
    public void exportReward(@RequestBody Map<String, String> params) {
        String templatePath = ExcelExportTemplateEnum.REWARD.getPath();
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String yearId = params.get("yearId");
        String deptId = params.get("deptId");
        exportService.exportReward(yearId, deptId, inputStream, response);
    }

    //技能大赛信息导出
    @RequestMapping(value = "/exportContest", method = RequestMethod.POST)
    public void exportContest(@RequestBody Map<String, String> params) {
        String templatePath = ExcelExportTemplateEnum.CONTEST.getPath();
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String yearId = params.get("yearId");
        String deptId = params.get("deptId");
        exportService.exportContest(yearId, deptId, inputStream, response);
    }

    //考证信息导出
    @RequestMapping(value = "/exportCertificate", method = RequestMethod.POST)
    public void exportCertificate(@RequestBody Map<String, String> params) {
        String templatePath = ExcelExportTemplateEnum.CERTIFICATE.getPath();
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String yearId = params.get("yearId");
        String deptId = params.get("deptId");
        exportService.exportCertificate(yearId, deptId, inputStream, response);
    }

    //军训信息导出
    @RequestMapping(value = "/exportMilitary", method = RequestMethod.POST)
    public void exportMilitary(@RequestBody Map<String, String> params) {
        String templatePath = ExcelExportTemplateEnum.MILITARY.getPath();
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String yearId = params.get("yearId");
        String deptId = params.get("deptId");
        exportService.exportMilitary(yearId, deptId, inputStream, response);
    }

    //党团活动信息导出
    @RequestMapping(value = "/exportPolitical", method = RequestMethod.POST)
    public void exportPolitical(@RequestBody Map<String, String> params) {
        String templatePath = ExcelExportTemplateEnum.POLITICAL.getPath();
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String yearId = params.get("yearId");
        String deptId = params.get("deptId");
        exportService.exportPolitical(yearId, deptId, inputStream, response);
    }

    //志愿者服务信息导出
    @RequestMapping(value = "/exportVolunteer", method = RequestMethod.POST)
    public void exportVolunteer(@RequestBody Map<String, String> params) {
        String templatePath = ExcelExportTemplateEnum.VOLUNTEER.getPath();
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String yearId = params.get("yearId");
        String deptId = params.get("deptId");
        exportService.exportVolunteer(yearId, deptId, inputStream, response);
    }

}
