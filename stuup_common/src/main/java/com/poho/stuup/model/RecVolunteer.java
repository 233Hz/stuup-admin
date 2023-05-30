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
 * 志愿者活动记录填报
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Getter
@Setter
@TableName("t_rec_volunteer")
public class RecVolunteer implements Serializable {

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
     * 基地/项目名称
     */
    private String name;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 子项目
     */
    private String child;

    /**
     * 岗位
     */
    private String post;

    /**
     * 学时
     */
    private Integer studyTime;

    /**
     * 服务时间
     */
    private Date serviceTime;

    /**
     * 晚填理由
     */
    private String reason;

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
