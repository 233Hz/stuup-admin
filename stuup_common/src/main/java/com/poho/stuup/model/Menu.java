package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.*;
import com.poho.stuup.constant.ValidationGroups;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Getter
@Setter
@TableName("t_menu")
public class Menu {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = {ValidationGroups.Update.class})
    @Null(message = "添加无需设置id", groups = {ValidationGroups.ADD.class})
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父节点id
     */
    @NotNull(message = "根菜单不能为空", groups = {ValidationGroups.ADD.class, ValidationGroups.Update.class})
    private Integer pid;

    /**
     * 权限名称
     */
    @NotNull(message = "权限名称不能为空", groups = {ValidationGroups.ADD.class, ValidationGroups.Update.class})
    private String name;

    /**
     * 权限编号
     */
    @NotNull(message = "权限编号不能为空", groups = {ValidationGroups.ADD.class, ValidationGroups.Update.class})
    private String code;

    /**
     * 菜单路径
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.NVARCHAR)
    private String path;

    /**
     * 菜单图标
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.NVARCHAR)
    private String icon;

    /**
     * 权限类型（0.菜单 1.按钮）
     */
    @NotNull(message = "权限类型不能为空", groups = {ValidationGroups.ADD.class, ValidationGroups.Update.class})
    private Integer type;

    /**
     * 前后台标识（1.前台 2.后台）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.NVARCHAR)
    private Integer flag;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {ValidationGroups.ADD.class, ValidationGroups.Update.class})
    private Integer sort;

    /**
     * 缓存状态（1.缓存 2.不缓存）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.NVARCHAR)
    private Integer keepAlive;

    /**
     * 权限标识
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.NVARCHAR)
    private String permission;

    /**
     * 是否使用layout布局（1.使用 2.不使用）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.NVARCHAR)
    private Integer layout;

    /**
     * 是否隐藏
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.NVARCHAR)
    private Integer hidden;

    /**
     * 重定向（不写默认子路由第一个）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.NVARCHAR)
    private String redirect;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}