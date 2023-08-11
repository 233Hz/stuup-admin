package com.poho.stuup.model.vo;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 学生画像-学习课程
 * @date 2023/8/7 17:08
 */
@Data
public class PortraitStudyCourseVO {

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程分数
     */
    private Float score;
}
