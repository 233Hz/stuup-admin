package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.api.config.PropertiesConfig;
import com.poho.stuup.service.IExportService;
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
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 16:14 2020/10/16
 * @Modified By:
 */
@Controller
@RequestMapping("/download")
public class DownloadController {
    @Resource
    private IExportService exportService;
    @Resource
    private PropertiesConfig config;
    @Resource
    private HttpServletResponse response;

    /**
     * 下载导入模板
     *
     * @return
     */
    @RequestMapping(value = "/template/{param}", method = RequestMethod.POST)
    public void template(@PathVariable String param) throws Exception {
        String templatePath = "";
        String finalName = "";
        if ("user".equals(param)) {
            templatePath = "template/userTemplate.xls";
            finalName = "员工信息导入模板.xls";
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

    /**
     * 导出考核登记信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/reg/{oid}", method = RequestMethod.POST)
    public ResponseModel reg(@PathVariable Long oid) {
        return exportService.exportRegPdf(config.getBaseDoc(), config.getBaseUrl(), oid);
    }

    /**
     * 导出绩效考核结果
     * @return
     */
    @RequestMapping(value = "/exportResult/{yearId}", method = RequestMethod.POST)
    public void exportResult(@PathVariable Long yearId) {
        String templatePath = "template/resultTemplate.xls";
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exportService.exportResult(yearId, inputStream, response);
    }

    /**
     * 导出员工绩效考核表
     * @return
     */
    @RequestMapping(value = "/exportStaff", method = RequestMethod.POST)
    public void exportStaff(@RequestBody Map<String, String> params) {
        String templatePath = "template/staffTemplate.xls";
        ClassPathResource resource = new ClassPathResource(templatePath);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String yearId = params.get("yearId");
        String deptId = params.get("deptId");
        exportService.exportStaff(yearId, deptId, inputStream, response);
    }
}
