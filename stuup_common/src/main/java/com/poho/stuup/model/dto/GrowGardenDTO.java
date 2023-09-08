package com.poho.stuup.model.dto;

import com.poho.stuup.model.User;
import lombok.Data;

import java.util.List;

@Data
public class GrowGardenDTO {

    /**
     * 花园类型
     */
    private Integer gardenType;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 所属年级
     */
    private Long gradeId;

    /**
     * 所属班级
     */
    private String className;

    /**
     * 班级id
     */
    private List<Integer> classIds;

    private User user;

}
