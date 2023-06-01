package com.poho.stuup.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author BUNGA
 * @description: 页面默认记录查询对象
 * @date 2023/5/31 23:37
 */
@Data
public class RecDefaultDTO {

    /**
     * 年级
     */
    private Long gradeId;

    /**
     * 班级
     */
    private Long classId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 项目id
     */
    @JsonIgnore
    private Long batchCode;

}
