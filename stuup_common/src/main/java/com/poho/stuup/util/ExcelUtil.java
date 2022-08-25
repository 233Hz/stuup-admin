package com.poho.stuup.util;

import com.poho.common.constant.CommonConstants;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.model.User;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
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

    /**
     * 读取考试信息
     *
     * @param path
     * @return
     * @throws Exception
     */
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
}
