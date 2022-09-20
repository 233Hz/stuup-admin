package com.poho.stuup.service.impl;

import com.poho.common.custom.ExcelExportTemplateEnum;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.*;
import com.poho.stuup.service.IExportService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;


@Service
public class ExportServiceImpl implements IExportService {

    @Resource
    private RewardMapper rewardMapper;

    @Resource
    private ContestMapper contestMapper;

    @Resource
    private CertificateMapper certificateMapper;

    @Resource
    private MilitaryMapper militaryMapper;

    @Resource
    private PoliticalMapper politicalMapper;

    @Resource
    private VolunteerMapper volunteerMapper;


    @Override
    public boolean exportReward(String yearId, String deptId, InputStream inputStream, HttpServletResponse response) {
        List<List<String>> dataList = null;
        return this.export(inputStream, response, ExcelExportTemplateEnum.REWARD, dataList);
    }

    @Override
    public boolean exportContest(String yearId, String deptId, InputStream inputStream, HttpServletResponse response) {
        List<List<String>> dataList = null;
        return this.export(inputStream, response, ExcelExportTemplateEnum.CONTEST, dataList);
    }

    @Override
    public boolean exportCertificate(String yearId, String deptId, InputStream inputStream, HttpServletResponse response) {
        List<List<String>> dataList = null;
        return this.export(inputStream, response, ExcelExportTemplateEnum.CERTIFICATE, dataList);
    }

    @Override
    public boolean exportMilitary(String yearId, String deptId, InputStream inputStream, HttpServletResponse response) {
        List<List<String>> dataList = null;
        return this.export(inputStream, response, ExcelExportTemplateEnum.MILITARY, dataList);
    }

    @Override
    public boolean exportPolitical(String yearId, String deptId, InputStream inputStream, HttpServletResponse response) {
        List<List<String>> dataList = null;
        return this.export(inputStream, response, ExcelExportTemplateEnum.POLITICAL, dataList);
    }

    @Override
    public boolean exportVolunteer(String yearId, String deptId, InputStream inputStream, HttpServletResponse response) {
        List<List<String>> dataList = null;
        return this.export(inputStream, response, ExcelExportTemplateEnum.VOLUNTEER, dataList);
    }

    private boolean export(InputStream inputStream, HttpServletResponse response
            , ExcelExportTemplateEnum exportTemplateEnum
            , List<List<String>> dataList) {
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell;
        CellStyle style = workbook.createCellStyle();
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        int rowNum;
        if (MicrovanUtil.isNotEmpty(dataList)) {
            rowNum = 1;
            for (int i = 0; i < dataList.size(); i++) {
                row = sheet.createRow(rowNum);
                row.setHeightInPoints(24);
                int cellNum = 0;
                List<String> rowData = dataList.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    cell = row.createCell(cellNum++);
                    cell.setCellValue(rowData.get(j));
                    cell.setCellStyle(style);
                }
                rowNum++;
            }
        }
        OutputStream output = null;
        try {
            StringBuilder title = new StringBuilder(exportTemplateEnum.getName());
            title.append("_" + System.currentTimeMillis() + ".xlsx");
            String fileName = URLEncoder.encode(title.toString(), "UTF-8");
            output = response.getOutputStream();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.flushBuffer();
            workbook.write(output);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;

    }

}
