package com.poho.stuup.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecSocietyExcel {

    @ExcelProperty(value = "年级")
    private String gradeName;

    @ExcelProperty(value = "班级")
    private String className;

    @ExcelProperty(value = "学号")
    private String studentNo;

    @ExcelProperty(value = "学生姓名")
    private String studentName;

    @ExcelProperty(value = "证件号")
    private String idCard;

    @ExcelProperty(value = "社团名称")
    private String name;

    @ExcelProperty(value = "获奖级别")
    private String level;

    @ExcelProperty(value = "开始时间")
    private String startTime;

    @ExcelProperty(value = "结束时间")
    private String endTime;

    @ExcelProperty(value = "角色")
    private String role;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelIgnore
    @JsonIgnore
    private Long studentId;

    @ExcelIgnore
    @JsonIgnore
    private Integer levelValue;

    @ExcelIgnore
    @JsonIgnore
    private Integer roleValue;

}
