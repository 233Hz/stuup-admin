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
 * 学生积分日志
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-27
 */
@Getter
@Setter
@TableName("t_stu_score_log")
public class StuScoreLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 成长项id
     */
    private Long growId;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 获得积分
     */
    private BigDecimal score;

    /**
     * 获取学年id
     */
    private Long yearId;

    /**
     * 学期id
     */
    private Long semesterId;

    /**
     * 获取时间
     */
    private Date createTime;

    /**
     * 获取说明
     */
    private String description;


}
