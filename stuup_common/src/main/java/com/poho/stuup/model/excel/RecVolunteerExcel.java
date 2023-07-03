package com.poho.stuup.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecVolunteerExcel {

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

    @ExcelProperty(value = "基地/项目名称")
    private String name;

    @ExcelProperty(value = "级别")
    private String level;

    @ExcelProperty(value = "子项目")
    private String child;

    @ExcelProperty(value = "岗位")
    private String post;

    @ExcelProperty(value = "学时")
    private String studyTime;

    @ExcelProperty(value = "服务时间")
    private String serviceTime;

    @ExcelProperty(value = "晚填理由")
    private String reason;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelIgnore
    @JsonIgnore
    private Long studentId;

    @ExcelIgnore
    @JsonIgnore
    private Integer levelValue;

}
