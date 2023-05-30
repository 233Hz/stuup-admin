package com.poho.stuup.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecHonorExcel {

    @ExcelProperty(value = "学号")
    private String studentNo;

    @ExcelProperty(value = "学生姓名")
    private String studentName;

    @ExcelProperty(value = "荣誉称号")
    private String name;

    @ExcelProperty(value = "级别")
    private String level;

    @ExcelProperty(value = "评选单位")
    private String unit;

    @ExcelProperty(value = "评选时间")
    private String time;

    @ExcelIgnore
    @JsonIgnore
    private Long studentId;

    @ExcelIgnore
    @JsonIgnore
    private Long batchCode;

    @ExcelIgnore
    @JsonIgnore
    private Integer rowIndex;

}
