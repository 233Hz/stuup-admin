package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author BUNGA
 * @description: 学生画像-基础信息
 * @date 2023/8/7 9:07
 */
@Data
public class PortraitBasicInfoVO {

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 班级
     */
    private String className;

    /**
     * 班主任
     */
    private String classTeacher;

    /**
     * 专业
     */
    private String majorName;

    /**
     * 总成长值
     */
    private BigDecimal totalScore;

    /**
     * 总扣分
     */
    private BigDecimal totalMinusScore;

    /**
     * 当前排名
     */
    private Integer ranking;

    /**
     * 参加的社团
     */
    private List<String> associations;
}
