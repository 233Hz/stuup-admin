package com.poho.stuup.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.service.IExportService;
import com.poho.stuup.util.ProjectUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 16:16 2020/10/16
 * @Modified By:
 */
@Service
public class ExportServiceImpl implements IExportService {
    @Resource
    private RegisterMapper registerMapper;
    @Resource
    private AssessScoreMapper assessScoreMapper;
    @Resource
    private AssessRecordMapper assessRecordMapper;
    @Resource
    private AssessRangeMapper assessRangeMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private YearMapper yearMapper;


    @Override
    public ResponseModel exportRegPdf(String baseDoc, String baseUrl, Long oid) {
        ResponseModel model = new ResponseModel();
        String pdf = "";
        Register register = registerMapper.selectByPrimaryKey(oid);
        if (MicrovanUtil.isNotEmpty(register)) {
            User user = userMapper.selectByPrimaryKey(register.getUserId());
            if (MicrovanUtil.isNotEmpty(user)) {
                String leader = "";
                Map<String, Object> param = new HashMap<>();
                param.put("yearId", register.getYearId());
                param.put("userId", register.getUserId());
                AssessRange assessRange = assessRangeMapper.checkAssessRange(param);
                if (MicrovanUtil.isNotEmpty(assessRange)) {
                    if (assessRange.getUserType() == ProjectConstants.RANGE_TYPE_PTYG) {
                        param.put("deptId", assessRange.getDeptId());
                        param.put("userType", ProjectConstants.RANGE_TYPE_BMFZR);
                        leader = assessRangeMapper.queryRangeLeader(param);
                    }
                }

                // 1.新建document对象
                Document document = new Document();
                try {
                    String pdfPath = baseDoc + File.separator + ProjectConstants.PROJECT_PDF;
                    MicrovanUtil.createFolder(pdfPath);

                    String pdfName = MicrovanUtil.generateShortUuid();
                    //输出路径
                    String outPath = pdfPath + File.separator + pdfName + ".pdf";
                    pdf = ProjectConstants.PROJECT_PDF + "/" + pdfName + ".pdf";
                    // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
                    // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
                    PdfWriter.getInstance(document, new FileOutputStream(outPath));
                    // 3.打开文档
                    document.open();
                    //中文字体,解决中文不能显示问题
                    BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                    Font font = new Font(bfChinese);
                    Font fontBold = new Font(bfChinese, 12, Font.BOLD);
                    Font fontBig = new Font(bfChinese, 18);
                    Font fontBigBold = new Font(bfChinese, 18, Font.BOLD);

                    Paragraph title = new Paragraph("上海市医药学校年度考核登记表", fontBigBold);
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);

                    Paragraph school = new Paragraph(register.getYearName(), fontBig);
                    school.setAlignment(Element.ALIGN_CENTER);
                    document.add(school);

                    PdfPTable table = new PdfPTable(8);
                    table.setWidthPercentage(100);
                    table.setSpacingBefore(15f);
                    //内容过多不自动换页，能显示多少显示多少
                    table.setSplitLate(false);

                    //设置列宽
                    float[] columnWidths = {1f, 2f, 2f, 2f, 2f, 2f, 2f, 2f};
                    table.setWidths(columnWidths);

                    PdfPCell cell = null;

                    cell = new PdfPCell(new Paragraph("姓名", fontBold));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setFixedHeight(30f);
                    cell.setColspan(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(user.getUserName(), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(2);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("性别", fontBold));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(user.getSex() == 1 ? "男" : "女", font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("出生年月", fontBold));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(2);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(user.getBirthday(), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("文化程度", fontBold));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setFixedHeight(30f);
                    cell.setColspan(2);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(user.getDegree(), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(2);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("现任职务（岗位）", fontBold));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(2);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(register.getPosition(), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(2);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("部门", fontBold));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setFixedHeight(30f);
                    cell.setColspan(2);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(user.getDeptName(), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(6);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("分管工作", fontBold));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setFixedHeight(30f);
                    cell.setColspan(2);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(register.getJob(), font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(6);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("本年度思想、工作总结", fontBold));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(1);
                    cell.setPadding(10);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_TOP);
                    cell.setColspan(7);
                    cell.setPadding(10);

                    Paragraph paragraph = new Paragraph(register.getSummary(), font);
                    cell.addElement(paragraph);

                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("分管领导评鉴意见", fontBold));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(1);
                    cell.setPadding(10);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_TOP);
                    cell.setColspan(7);
                    cell.setPadding(10);

                    paragraph = new Paragraph(register.getLeaderOpinion(), font);
                    cell.addElement(paragraph);

                    if (MicrovanUtil.isNotEmpty(leader)) {
                        paragraph = new Paragraph("分管领导：" + leader, font);
                        paragraph.setAlignment(Element.ALIGN_RIGHT);
                        paragraph.setSpacingBefore(20);
                        cell.addElement(paragraph);
                    }
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("考核小组意见", fontBold));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(1);
                    cell.setPadding(10);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_TOP);
                    cell.setColspan(7);
                    cell.setPadding(10);

                    paragraph = new Paragraph(register.getGroupOpinion(), font);
                    cell.addElement(paragraph);

                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("单位意见", fontBold));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(1);
                    cell.setPadding(10);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_TOP);
                    cell.setColspan(7);
                    cell.setPadding(10);

                    paragraph = new Paragraph(register.getOrgOpinion(), font);
                    cell.addElement(paragraph);

                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("备注", fontBold));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setColspan(1);
                    cell.setPadding(10);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_TOP);
                    cell.setColspan(7);
                    cell.setPadding(10);

                    paragraph = new Paragraph(register.getNote(), font);
                    cell.addElement(paragraph);

                    table.addCell(cell);

                    document.add(table);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 5.关闭文档
                document.close();
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("生成成功");
                model.setData(baseUrl + pdf);
            } else {
                model.setCode(CommonConstants.CODE_EXCEPTION);
                model.setMessage("数据有误，无法完成打印");
            }
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("数据有误，无法完成打印");
        }
        return model;
    }

    @Override
    public boolean exportResult(Long yearId, InputStream inputStream, HttpServletResponse response) {
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell = null;

        row = sheet.getRow(0);
        cell = row.getCell(0);
        Year year = yearMapper.selectByPrimaryKey(yearId);
        StringBuffer title = new StringBuffer(year.getYearName()).append("绩效考核汇总表");
        cell.setCellValue(title.toString());

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

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

        int rowNum = 0;
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", yearId);
        List<AssessScore> assessScores = assessScoreMapper.queryList(param);
        if (MicrovanUtil.isNotEmpty(assessScores)) {
            rowNum = 3;
            for (int i = 0; i < assessScores.size(); i++) {
                AssessScore assessScore = assessScores.get(i);
                row = sheet.createRow(rowNum);
                row.setHeightInPoints(24);
                int cellNum = 0;

                //系处
                cell = row.createCell(cellNum++);
                cell.setCellValue(assessScore.getDeptName());
                cell.setCellStyle(style);

                //姓名
                cell = row.createCell(cellNum++);
                cell.setCellValue(assessScore.getUserName());
                cell.setCellStyle(style);

                //群众测评
                cell = row.createCell(cellNum++);
                cell.setCellValue(assessScore.getAdjustQzcp() == null ? assessScore.getQzcp() : assessScore.getAdjustQzcp());
                cell.setCellStyle(style);

                //自评互评
                cell = row.createCell(cellNum++);
                cell.setCellValue(assessScore.getZphp());
                cell.setCellStyle(style);

                //分管领导
                cell = row.createCell(cellNum++);
                cell.setCellValue(assessScore.getFgld());
                cell.setCellStyle(style);

                //党政领导
                cell = row.createCell(cellNum++);
                cell.setCellValue(assessScore.getDzld());
                cell.setCellStyle(style);

                //总分
                cell = row.createCell(cellNum++);
                cell.setCellValue(assessScore.getAdjustScore() == null ? assessScore.getScore() : assessScore.getAdjustScore());
                cell.setCellStyle(style);

                //排名
                cell = row.createCell(cellNum++);
                cell.setCellValue(i + 1);
                cell.setCellStyle(style);

                rowNum++;
            }
        }
        OutputStream output = null;
        try {
            title = title.append("_" + System.currentTimeMillis() + ".xls");
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

    @Override
    public boolean exportStaff(String yearId, String deptId, InputStream inputStream, HttpServletResponse response) {
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell = null;

        row = sheet.getRow(0);
        cell = row.getCell(0);
        Year year = yearMapper.selectByPrimaryKey(Long.valueOf(yearId));
        StringBuffer title = new StringBuffer(year.getYearName()).append("绩效考核汇总表（员工）");
        cell.setCellValue(title.toString());

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

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

        int rowNum = 0;
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", Long.valueOf(yearId));
        if (MicrovanUtil.isNotEmpty(deptId)) {
            param.put("deptId", Long.valueOf(deptId));
        }
        param.put("assessType", ProjectConstants.ASSESS_TYPE_ZCPYG);
        List<AssessRecord> assessRecords = assessRecordMapper.queryList(param);
        if (MicrovanUtil.isNotEmpty(assessRecords)) {
            rowNum = 2;
            for (int i = 0; i < assessRecords.size(); i++) {
                AssessRecord assessRecord = assessRecords.get(i);
                row = sheet.createRow(rowNum);
                row.setHeightInPoints(24);
                int cellNum = 0;

                //排名
                cell = row.createCell(cellNum++);
                cell.setCellValue(i + 1);
                cell.setCellStyle(style);

                //部门
                cell = row.createCell(cellNum++);
                cell.setCellValue(assessRecord.getDeptName());
                cell.setCellStyle(style);

                //人员
                cell = row.createCell(cellNum++);
                cell.setCellValue(assessRecord.getUserName());
                cell.setCellStyle(style);

                //类别
                cell = row.createCell(cellNum++);
                cell.setCellValue(ProjectUtil.convertUserType(assessRecord.getUserType()));
                cell.setCellStyle(style);

                //等级
                cell = row.createCell(cellNum++);
                String grade = "";
                if (assessRecord.getAdjustScore() != null) {
                    grade = ProjectUtil.convertGrade(assessRecord.getAdjustScore());
                } else {
                    grade = ProjectUtil.convertGrade(assessRecord.getScore());
                }
                cell.setCellValue(grade);
                cell.setCellStyle(style);

                //备注
                cell = row.createCell(cellNum++);
                cell.setCellValue(assessRecord.getNote());
                cell.setCellStyle(style);

                rowNum++;
            }
        }
        OutputStream output = null;
        try {
            title = title.append("_" + System.currentTimeMillis() + ".xls");
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
