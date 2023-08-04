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
 * 积分计算失败记录
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-03
 */
@Getter
@Setter
@TableName("t_calculate_fail_log")
public class CalculateFailLog implements Serializable {

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
     * 是否执行成功（1.是 2.否）
     */
    private Integer status;

    /**
     * 项目id
     */
    private Long growId;

    /**
     * 年份id
     */
    private Long yearId;

    /**
     * 学期id
     */
    private Long semesterId;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 统计次数
     */
    private Integer count;

    /**
     * 计算类型
     */
    private Integer calculateType;


}
