package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("t_menu")
public class Menu {

    /**
     * id
     */
    @TableId(value = "oid", type = IdType.AUTO)
    private Integer oid;

    /**
     * 父节点id
     */
    private Integer pid;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单编号
     */
    private String code;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单类型（1.目录 2.菜单 3.按钮）
     */
    private Integer type;

    /**
     * 前后台标识（1.前台 2.后台）
     */
    private Integer flag;

    /**
     * 菜单排序
     */
    private Integer sort;

    /**
     * 缓存状态（1.缓存 2.不缓存）
     */
    private Integer keepAlive;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 是否使用layout布局（1.使用 2.不使用）
     */
    private Integer useLayout;

    /**
     * 是否隐藏
     */
    private Integer hidden;

    /**
     * 重定向（不写默认子路由第一个）
     */
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