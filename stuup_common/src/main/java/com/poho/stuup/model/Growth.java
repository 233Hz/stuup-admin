package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 成长项目表
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-24
 */
@Getter
@Setter
@TableName("t_growth")
public class Growth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父项目id（0为根节点）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.NVARCHAR)
    private Long pid;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    private String name;

    /**
     * 项目说明
     */
    private String description;

    /**
     * 顺序
     */
    @NotNull(message = "顺序不能为空")
    private Integer sort;

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
