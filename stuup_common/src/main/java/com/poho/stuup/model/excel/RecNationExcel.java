package com.poho.stuup.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecNationExcel {

    @ExcelProperty(value = "学号")
    private String studentNo;

    @ExcelProperty(value = "学生姓名")
    private String studentName;

    @ExcelProperty(value = "项目名称")
    private String name;

    @ExcelProperty(value = "获奖级别")
    private String level;

    @ExcelProperty(value = "组织机构（主办方）")
    private String org;

    @ExcelProperty(value = "累计时间（课时）")
    private String hour;

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
