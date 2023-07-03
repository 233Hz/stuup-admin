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
 * 年度排行榜
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
@Getter
@Setter
@TableName("t_rank_year")
public class RankYear implements Serializable {

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
     * 年份id
     */
    private Long yearId;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 积分
     */
    private BigDecimal score;

    /**
     * 创建时间
     */
    private Date createTime;


}
