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
 * 学期榜
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-28
 */
@Getter
@Setter
@TableName("t_rank_semester")
public class RankSemester implements Serializable {

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
     * 学期id
     */
    private Long semesterId;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 成长值
     */
    private BigDecimal score;

    /**
     * 创建时间
     */
    private Date createTime;


}
