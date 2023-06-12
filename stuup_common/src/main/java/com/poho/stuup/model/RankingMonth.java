package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
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
@TableName("t_ranking_month")
public class RankingMonth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 排名
     */
    private Integer ranking;

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
    private Integer score;

    /**
     * 创建时间
     */
    private Date createTime;


}
