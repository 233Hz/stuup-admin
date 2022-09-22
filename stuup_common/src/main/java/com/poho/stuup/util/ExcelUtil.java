package com.poho.stuup.util;

import cn.hutool.core.util.ReflectUtil;
import com.poho.common.constant.CommonConstants;
import com.poho.common.exception.ExcelTitleException;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.model.User;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ernest
 */
public class ExcelUtil {
    /**
     * 判断的版本,获取Workbook
     *
     * @param is
     * @param file
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbook(InputStream is, File file) throws IOException {
        Workbook workbook = null;
        if (file.getName().endsWith(CommonConstants.EXCEL_XLS)) {
            workbook = new HSSFWorkbook(is);
        } else if (file.getName().endsWith(CommonConstants.EXCEL_XLSX)) {
            workbook = new XSSFWorkbook(is);
        }
        return workbook;
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell != null) {
            CellType cellType = cell.getCellTypeEnum();
            if (cellType == CellType.STRING) {
                cellValue = cell.getRichStringCellValue().getString();
            } else if (cellType == CellType.NUMERIC) {
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = MicrovanUtil.formatDateToStr("yyyy-MM-dd", cell.getDateCellValue());
                } else {
                    cell.setCellType(CellType.STRING);
                    cellValue = cell.getRichStringCellValue().getString();
                }
            } else if (cellType == CellType.BOOLEAN) {
                cellValue = String.valueOf(cell.getBooleanCellValue());
            } else if (cellType == CellType.BLANK) {
                cellValue = cell.getStringCellValue();
            } else if (cellType == CellType.ERROR) {
                cellValue = cell.getStringCellValue();
            } else if (cellType == CellType.FORMULA) {
                cell.setCellType(CellType.STRING);
                cellValue = cell.getRichStringCellValue().getString();
            }
        }
        return cellValue;
    }


    public List<User> readUserExcel(String path) throws Exception {
        List<User> list = new ArrayList<>();
        File file = new File(path);
        FileInputStream is = new FileInputStream(file);
        Workbook workbook = getWorkbook(is, file);
        Sheet sheet = workbook.getSheetAt(0);
        User user = null;
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Cell nameCell = sheet.getRow(rowNum).getCell(0);
            Cell sexCell = sheet.getRow(rowNum).getCell(1);
            Cell mobileCell = sheet.getRow(rowNum).getCell(2);
            Cell userTypeCell = sheet.getRow(rowNum).getCell(3);
            Cell degreeCell = sheet.getRow(rowNum).getCell(4);
            Cell idCardCell = sheet.getRow(rowNum).getCell(5);
            Cell loginNameCell = sheet.getRow(rowNum).getCell(6);
            Cell deptNameCell = sheet.getRow(rowNum).getCell(7);

            if (MicrovanUtil.isEmpty(nameCell)
                    && MicrovanUtil.isEmpty(sexCell)
                    && MicrovanUtil.isEmpty(mobileCell)
                    && MicrovanUtil.isEmpty(userTypeCell)
                    && MicrovanUtil.isEmpty(degreeCell)
                    && MicrovanUtil.isEmpty(idCardCell)
                    && MicrovanUtil.isEmpty(loginNameCell)
                    && MicrovanUtil.isEmpty(deptNameCell)) {
                break;
            }
            user = new User();
            user.setUserName(getCellValue(nameCell));
            user.setSex(ProjectUtil.convertSexStr(getCellValue(sexCell)));
            String mobile = getCellValue(mobileCell);
            user.setMobile(mobile);
            user.setUserType(ProjectUtil.convertUserTypeStr(getCellValue(userTypeCell)));
            user.setDegree(getCellValue(degreeCell));
            String idCard = getCellValue(idCardCell);
            user.setIdCard(idCard);
            user.setBirthday(ProjectUtil.obtainBirthdayFromIdCard(idCard));
            user.setLoginName(getCellValue(loginNameCell));
            user.setDeptName(getCellValue(deptNameCell));

            user.setRowNum(rowNum + 1);

            list.add(user);
        }
        return list;
    }

    public <T> List<T> readExcel(String path, Class<T> clazz, String[] headNames, String[] fieldNames) throws Exception {
        List<T> list = new ArrayList<>();
        File file = new File(path);
        FileInputStream is = new FileInputStream(file);
        Workbook workbook = getWorkbook(is, file);
        Sheet sheet = workbook.getSheetAt(0);
        // 检验表头数据与模板是否一致，防止老师弄错模板了(新需求)
        int rowTotalNum = sheet.getPhysicalNumberOfRows();
        if(rowTotalNum <= 0){
            return  list;
        }
        this.checkHead(workbook, sheet, headNames);
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            T obj = ReflectUtil.newInstanceIfPossible(clazz);
            Cell cell;
            for(int i = 0; i < fieldNames.length; i++ ){
                cell = sheet.getRow(rowNum).getCell(i);
                ReflectUtil.setFieldValue(obj, fieldNames[i], getCellValue(workbook, cell));
            }
            ReflectUtil.setFieldValue(obj, "rowNum", rowNum + 1);
            list.add(obj);
        }
        return list;
    }

    private String getCellValue(Workbook workbook, Cell cell) {
        String cellValue = "";
        if (cell != null) {
            CellType cellType = cell.getCellTypeEnum();
            if (cellType == CellType.STRING) {
                if (workbook instanceof HSSFWorkbook) {
                    HSSFRichTextString richTextString = (HSSFRichTextString) cell.getRichStringCellValue();
                    HSSFCellStyle style = (HSSFCellStyle) cell.getCellStyle();
                    HSSFFont font = style.getFont(workbook);
                    int fromIndex = 0;
                    int toIndex;
                    if (richTextString != null && richTextString.numFormattingRuns() > 0) {
                        for (int k = 0; k < richTextString.numFormattingRuns(); k++) {
                            toIndex = richTextString.getIndexOfFormattingRun(k);
                            String temp = richTextString.toString().substring(fromIndex, toIndex);
                            if (font.getTypeOffset() == HSSFFont.SS_SUPER) {
                                temp = "<sup>" + temp + "</sup>";
                            }
                            if (font.getTypeOffset() == HSSFFont.SS_SUB) {
                                temp = "<sub>" + temp + "</sub>";
                            }
                            cellValue += temp;
                            if (MicrovanUtil.isNotEmpty(cellValue)) {
                                font = (HSSFFont) workbook.getFontAt(richTextString.getFontOfFormattingRun(k));
                            }
                            fromIndex = toIndex;
                        }
                        toIndex = richTextString.length();
                        String temp = richTextString.toString().substring(fromIndex, toIndex);
                        if (font.getTypeOffset() == HSSFFont.SS_SUPER) {
                            temp = "<sup>" + temp + "</sup>";
                        }
                        if (font.getTypeOffset() == HSSFFont.SS_SUB) {
                            temp = "<sub>" + temp + "</sub>";
                        }
                        cellValue += temp;
                    } else {
                        cellValue = cell.getRichStringCellValue().getString();
                    }
                } else if (workbook instanceof XSSFWorkbook) {
                    XSSFRichTextString richTextString = (XSSFRichTextString) cell.getRichStringCellValue();
                    XSSFCellStyle style = (XSSFCellStyle) cell.getCellStyle();
                    XSSFFont font = style.getFont();
                    int fromIndex = 0;
                    int toIndex = 0;
                    if (richTextString != null && richTextString.numFormattingRuns() > 0) {
                        for (int k = 0; k < richTextString.numFormattingRuns(); k++) {
                            toIndex = richTextString.getIndexOfFormattingRun(k);
                            String temp = richTextString.toString().substring(fromIndex, toIndex);
                            if (font.getTypeOffset() == XSSFFont.SS_SUPER) {
                                temp = "<sup>" + temp + "</sup>";
                            }
                            if (font.getTypeOffset() == XSSFFont.SS_SUB) {
                                temp = "<sub>" + temp + "</sub>";
                            }
                            cellValue += temp;
                            if (MicrovanUtil.isNotEmpty(cellValue)) {
                                font = richTextString.getFontOfFormattingRun(k);
                            }
                            fromIndex = toIndex;
                        }
                        toIndex = richTextString.length();
                        String temp = richTextString.toString().substring(fromIndex, toIndex);
                        if (font.getTypeOffset() == XSSFFont.SS_SUPER) {
                            temp = "<sup>" + temp + "</sup>";
                        }
                        if (font.getTypeOffset() == XSSFFont.SS_SUB) {
                            temp = "<sub>" + temp + "</sub>";
                        }
                        cellValue += temp;
                    } else {
                        cellValue = cell.getRichStringCellValue().getString();
                    }
                }
            } else if (cellType == CellType.NUMERIC) {
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    cellValue = MicrovanUtil.formatDateToStr("yyyy-MM-dd", cell.getDateCellValue());
                } else {
                    cell.setCellType(CellType.STRING);
                    cellValue = cell.getRichStringCellValue().getString();
                }
            } else if (cellType == CellType.BOOLEAN) {
                cellValue = String.valueOf(cell.getBooleanCellValue());
            } else if (cellType == CellType.BLANK) {
                cellValue = cell.getStringCellValue();
            } else if (cellType == CellType.ERROR) {
                cellValue = cell.getStringCellValue();
            } else if (cellType == CellType.FORMULA) {
                cell.setCellType(CellType.STRING);
                cellValue = cell.getRichStringCellValue().getString();
            }
        }
        return cellValue;
    }

    private void checkHead(Workbook workbook, Sheet sheet, String[] heads) {
        int titleRowNum = 0;
        String title;
        for(int i =0 ; i < heads.length; i++){
            Cell titleCell = sheet.getRow(titleRowNum).getCell(i);
            title = getCellValue(workbook, titleCell);
            if(!heads[i].equals(title)){
                throw new ExcelTitleException("模板格式不对，请将导入的excel与系统提供的模板进行认真比对");
            }
        }
    }

}
