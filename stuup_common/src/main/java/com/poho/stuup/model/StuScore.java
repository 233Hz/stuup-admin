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
 * 学生积分表
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Getter
@Setter
@TableName("t_stu_score")
public class StuScore implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 拥有积分
     */
    private BigDecimal score;

    /**
     * 更新时间
     */
    private Date updateTime;


}
