package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 学期排行榜
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
@Getter
@Setter
@TableName("t_rank_month")
public class RankMonth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 积分
     */
    private BigDecimal score;

    /**
     * 排名趋势（1.上升 2.下降 3.不变）
     */
    private Integer rankTrend;

    /**
     * 排名变化名次
     */
    private Integer rankChange;

    /**
     * 本月获取分数
     */
    private BigDecimal scoreChange;

    /**
     * 创建时间
     */
    private Date createTime;


}
