package com.poho.stuup.model.vo;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 学生画像-学习成绩
 * @date 2023/8/7 17:08
 */
@Data
public class PortraitStudyGradeVO {

    /**
     * 学期名称
     */
    private String semesterName;

    /**
     * 课程分数
     */
    private Float score;
}
