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
 * 发布公告表
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-08
 */
@Getter
@Setter
@TableName("t_announcement")
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告类型（1.系统通知 2.通知公告）
     */
    private Integer type;

    /**
     * 通知范围（1.全体用户 2.所有教师）
     */
    private Integer scope;

    /**
     * 文本类容
     */
    private String content;

    /**
     * 发布用户
     */
    private Long createUser;

    /**
     * 状态（1.未发布 2.发布）
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
