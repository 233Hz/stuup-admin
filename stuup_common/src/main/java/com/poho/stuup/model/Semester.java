package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 学期管理表
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-27
 */
@Getter
@Setter
@TableName("t_semester")
public class Semester implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属学年
     */
    @NotNull(message = "所属学年不能为空")
    private Long yearId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 学期名称
     */
    @NotBlank(message = "学期名称不能为空")
    private String name;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    /**
     * 是否为当前学期
     */
    private Integer isCurrent;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
