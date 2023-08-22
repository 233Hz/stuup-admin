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
 * 扣分记录
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-18
 */
@Getter
@Setter
@TableName("t_rec_deduct_score")
public class RecDeductScore implements Serializable {

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
     * 学期id
     */
    private Long semesterId;

    /**
     * 项目id
     */
    private Long growId;

    /**
     * 分值
     */
    private BigDecimal score;

    /**
     * 获得所属学年
     */
    private Long yearId;

    /**
     * 获取状态（1.已获取 2.未获取）
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;


}
