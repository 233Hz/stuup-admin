package com.poho.stuup.api.controller;

import com.poho.stuup.api.config.PropertiesConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.net.URLEncoder;

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


}
