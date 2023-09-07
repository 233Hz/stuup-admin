package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 成长项
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-24
 */
@Getter
@Setter
@TableName("t_growth_item")
public class GrowthItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目名称
     */
    @NotBlank(message = "请输入项目名称")
    private String name;

    /**
     * 项目code
     */
    private String code;

    /**
     * 项目说明
     */
    private String description;

    /**
     * 分值刷新周期（不限、每天、每周、每月、每学期、每年）
     */
    @NotNull(message = "请选择分值刷新周期")
    private Integer scorePeriod;

    /**
     * 每个周期内分值的上限
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.NVARCHAR)
    private BigDecimal scoreUpperLimit;

    /**
     * 可采集次数
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.NVARCHAR)
    private Integer collectLimit;

    /**
     * 分值计算类型（1.录入为加分 2.录入为扣分）
     */
    @NotNull(message = "请选择分值计算类型")
    private Integer calculateType;

    /**
     * 项目可获得分值
     */
    @NotNull(message = "请选择项目可获得分值")
    private BigDecimal score;

    private Integer gatherer;

    /**
     * 类型（1.自定义 2.系统类）
     */
    private Integer type;

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


    /**
     * 一级项目id
     */
    @NotNull(message = "请选择所属项目")
    private Long firstLevelId;

    /**
     * 二级项目id
     */
    private Long secondLevelId;

    /**
     * 成长项目id
     */
    private Long threeLevelId;


}
