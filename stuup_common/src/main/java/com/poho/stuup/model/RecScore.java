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
 * 积分记录表
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Getter
@Setter
@TableName("t_rec_score")
public class RecScore implements Serializable {

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
     * 项目id
     */
    private Long growId;

    /**
     * 得分
     */
    private BigDecimal score;

    /**
     * 获得所属学年
     */
    private Long yearId;

    /**
     * 获取所属学期
     */
    private Long semesterId;

    /**
     * 获取状态（0.未获取 1.已获取）
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;


}
