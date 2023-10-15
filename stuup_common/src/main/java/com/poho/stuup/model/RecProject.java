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
 * 参加项目记录表
 * </p>
 *
 * @author BUNGA
 * @since 2023-10-13
 */
@Getter
@Setter
@TableName("t_rec_project")
public class RecProject implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 年份id
     */
    private Long yearId;

    /**
     * 学期id
     */
    private Long semesterId;

    /**
     * 项目id
     */
    private Long growId;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 参加时间
     */
    private Date time;

    /**
     * 备注
     */
    private String remark;

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
