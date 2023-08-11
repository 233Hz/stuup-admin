package com.poho.stuup.model.vo;

import com.poho.stuup.model.RecScore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author BUNGA
 * @description: 学生成长信息
 * @date 2023/8/9 17:02
 */

@Data
public class GrowthInfo {

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 总积分
     */
    private BigDecimal totalScore;

    /**
     * 当前排名
     */
    private Integer ranking;

    /**
     * 未获取的积分记录
     */
    private List<RecScore> unearnedPoints;
}
