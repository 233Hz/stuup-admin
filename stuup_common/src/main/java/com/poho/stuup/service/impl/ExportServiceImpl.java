package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.poho.common.custom.ExcelExportTemplateEnum;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.Grade;
import com.poho.stuup.model.dto.*;
import com.poho.stuup.service.IExportService;
import com.poho.stuup.util.ProjectUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ExportServiceImpl implements IExportService {

    private static final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);


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

    @Resource
    private GradeMapper gradeMapper;


    @Override
    public boolean exportReward(InputStream inputStream, HttpServletResponse response, RewardSearchDTO searchDTO) {
        List<RewardDTO> list = rewardMapper.selectList(null, searchDTO);
        final List<List<String>> dataList = new ArrayList<>();
        if(CollUtil.isNotEmpty(list)){
            final String[] fieldNames = {"name", "levelName", "stuName", "obtainDateStr", "unitName", "rank"};
            list.forEach(
                o -> {
                    List<String> fieldValueList = new ArrayList<>();
                    o.setLevelName(ProjectUtil.LEVEL_DICT_MAP.get(o.getLevel()));
                    if(o.getObtainDate() != null){
                        o.setObtainDateStr(MicrovanUtil.formatDateToStr(MicrovanUtil.DATE_FORMAT_PATTERN, o.getObtainDate()));
                    }
                    Object value;
                    for (String fieldName : fieldNames) {
                        value = ReflectUtil.getFieldValue(o, fieldName);
                        fieldValueList.add(value != null ? value.toString() : "");
                    }
                    dataList.add(fieldValueList);
                }
            );
        }

        return this.export(inputStream, response, ExcelExportTemplateEnum.REWARD, dataList);
    }

    @Override
    public boolean exportContest(InputStream inputStream, HttpServletResponse response, ContestSearchDTO searchDTO) {
        List<ContestDTO> list = contestMapper.selectList(null, searchDTO);
        final List<List<String>> dataList = new ArrayList<>();
        if(CollUtil.isNotEmpty(list)){
            final String[] fieldNames = {"name", "unitName", "levelName", "stuName", "obtainDateStr", "rank"};
            list.forEach(
                    o -> {
                        List<String> fieldValueList = new ArrayList<>();
                        o.setLevelName(ProjectUtil.LEVEL_DICT_MAP.get(o.getLevel()));
                        if(o.getObtainDate() != null){
                            o.setObtainDateStr(MicrovanUtil.formatDateToStr(MicrovanUtil.DATE_FORMAT_PATTERN, o.getObtainDate()));
                        }
                        Object value;
                        for (String fieldName : fieldNames) {
                            value = ReflectUtil.getFieldValue(o, fieldName);
                            fieldValueList.add(value != null ? value.toString() : "");
                        }
                        dataList.add(fieldValueList);
                    }
            );
        }
        return this.export(inputStream, response, ExcelExportTemplateEnum.CONTEST, dataList);
    }

    @Override
    public boolean exportCertificate(InputStream inputStream, HttpServletResponse response, CertificateSearchDTO searchDTO) {
        List<CertificateDTO> list = certificateMapper.selectList(null, searchDTO);
        final List<List<String>> dataList = new ArrayList<>();
        if(CollUtil.isNotEmpty(list)){
            final String[] fieldNames = {"name", "major", "unitName", "stuName", "levelName", "obtainDateStr", "certNo"};
            list.forEach(
                    o -> {
                        List<String> fieldValueList = new ArrayList<>();
                        o.setLevelName(ProjectUtil.LEVEL_DICT_MAP.get(o.getLevel()));
                        if(o.getObtainDate() != null){
                            o.setObtainDateStr(MicrovanUtil.formatDateToStr(MicrovanUtil.DATE_FORMAT_PATTERN, o.getObtainDate()));
                        }
                        Object value;
                        for (String fieldName : fieldNames) {
                            value = ReflectUtil.getFieldValue(o, fieldName);
                            fieldValueList.add(value != null ? value.toString() : "");
                        }
                        dataList.add(fieldValueList);
                    }
            );
        }
        return this.export(inputStream, response, ExcelExportTemplateEnum.CERTIFICATE, dataList);
    }

    @Override
    public boolean exportMilitary(InputStream inputStream, HttpServletResponse response, MilitarySearchDTO searchDTO) {
        List<MilitaryDTO> list = militaryMapper.selectList(null, searchDTO);
        final List<List<String>> dataList = new ArrayList<>();
        if(CollUtil.isNotEmpty(list)){
            final String[] fieldNames = {"gradeName", "className", "stuNo", "stuName", "levelName", "goodFlagName"};
            List<Grade> gradeList = gradeMapper.findGrades();
            Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(Grade::getOid,Grade::getGradeName));
            list.forEach(
                    o -> {
                        List<String> fieldValueList = new ArrayList<>();
                        if(o.getLevel() != null){
                            o.setLevelName(ProjectUtil.MILITARY_LEVEL_DICT_MAP.get(o.getLevel()));
                        }
                        if(o.getGoodFlag() != null){
                            o.setGoodFlagName(ProjectUtil.GOOD_FLAG_DICT_MAP.get(o.getGoodFlag()));
                        }
                        if(o.getGradeId() != null){
                            o.setGradeName(gradeMap.get(o.getGradeId()));
                        }
                        Object value;
                        for (String fieldName : fieldNames) {
                            value = ReflectUtil.getFieldValue(o, fieldName);
                            fieldValueList.add(value != null ? value.toString() : "");
                        }
                        dataList.add(fieldValueList);
                    }
            );
        }
        return this.export(inputStream, response, ExcelExportTemplateEnum.MILITARY, dataList);
    }

    @Override
    public boolean exportPolitical(InputStream inputStream, HttpServletResponse response, PoliticalSearchDTO searchDTO) {
        List<PoliticalDTO> list = politicalMapper.selectList(null, searchDTO);
        final List<List<String>> dataList = new ArrayList<>();
        if(CollUtil.isNotEmpty(list)){
            final String[] fieldNames = {"gradeName", "className", "stuNo", "stuName", "name", "durationDateStr", "levelName", "role", "orgName"};
            List<Grade> gradeList = gradeMapper.findGrades();
            Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(Grade::getOid,Grade::getGradeName));
            list.forEach(
                    o -> {
                        List<String> fieldValueList = new ArrayList<>();
                        o.setLevelName(ProjectUtil.POLITICAL_LEVEL_DICT_MAP.get(o.getLevel()));
                        String durationDateStr = "";
                        if(o.getStartDate() != null){
                            durationDateStr = MicrovanUtil.formatDateToStr(MicrovanUtil.DATE_FORMAT_PATTERN, o.getStartDate());
                        }
                        if(o.getEndDate() != null){
                            durationDateStr = durationDateStr + " - " + MicrovanUtil.formatDateToStr(MicrovanUtil.DATE_FORMAT_PATTERN, o.getEndDate());
                        }
                        if(o.getGradeId() != null){
                            o.setGradeName(gradeMap.get(o.getGradeId()));
                        }
                        o.setDurationDateStr(durationDateStr);
                        Object value;
                        for (String fieldName : fieldNames) {
                            value = ReflectUtil.getFieldValue(o, fieldName);
                            fieldValueList.add(value != null ? value.toString() : "");
                        }
                        dataList.add(fieldValueList);
                    }
            );
        }
        return this.export(inputStream, response, ExcelExportTemplateEnum.POLITICAL, dataList);
    }

    @Override
    public boolean exportVolunteer(InputStream inputStream, HttpServletResponse response, VolunteerSearchDTO searchDTO) {
        List<VolunteerDTO> list = volunteerMapper.selectList(null, searchDTO);
        final List<List<String>> dataList = new ArrayList<>();
        if(CollUtil.isNotEmpty(list)){
            final String[] fieldNames = {"stuNo", "stuName", "name", "address", "subName", "post", "durationName", "operDateStr", "memo"};
            list.forEach(
                    o -> {
                        List<String> fieldValueList = new ArrayList<>();
                        o.setDurationName(String.valueOf(o.getDuration()));
                        if(o.getOperDate() != null){
                            o.setOperDateStr(MicrovanUtil.formatDateToStr(MicrovanUtil.DATE_FORMAT_PATTERN, o.getOperDate()));
                        }
                        Object value;
                        for (String fieldName : fieldNames) {
                            value = ReflectUtil.getFieldValue(o, fieldName);
                            fieldValueList.add(value != null ? value.toString() : "");
                        }
                        dataList.add(fieldValueList);
                    }
            );
        }
        return this.export(inputStream, response, ExcelExportTemplateEnum.VOLUNTEER, dataList);
    }

    private boolean export(InputStream inputStream, HttpServletResponse response
            , ExcelExportTemplateEnum exportTemplateEnum
            , List<List<String>> dataList) {
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            logger.error(StrUtil.format("导入出错：{}", e));
            return false;
        }
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;
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
        if (CollUtil.isNotEmpty(dataList)) {
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
