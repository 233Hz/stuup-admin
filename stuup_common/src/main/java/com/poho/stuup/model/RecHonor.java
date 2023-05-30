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
 * 人荣誉记录填报
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Getter
@Setter
@TableName("t_rec_honor")
public class RecHonor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 年份id
     */
    private Long yearId;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 荣誉称号
     */
    private String name;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 评选单位
     */
    private String unit;

    /**
     * 评选时间
     */
    private Date time;

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
