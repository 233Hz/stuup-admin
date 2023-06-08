package com.poho.stuup.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecLaborTimeExcel {

    @ExcelProperty(value = "学号")
    private String studentNo;

    @ExcelProperty(value = "学生姓名")
    private String studentName;

    @ExcelProperty(value = "累计课时")
    private String hours;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelIgnore
    @JsonIgnore
    private Long studentId;

}
