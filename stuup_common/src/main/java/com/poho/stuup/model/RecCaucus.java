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
 * 参加党团学习项目记录填报
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Getter
@Setter
@TableName("t_rec_caucus")
public class RecCaucus implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 学生id
     */
    private Long studentId;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 角色
     */
    private Integer role;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 每次导入的code（当前导入的时间戳）
     */
    private Long batchCode;


}
