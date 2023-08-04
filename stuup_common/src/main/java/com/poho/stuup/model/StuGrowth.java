package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 学生成长项目相关信息
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-04
 */
@Getter
@Setter
@TableName("t_stu_growth")
public class StuGrowth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 项目
     */
    private Long growId;

    /**
     * 采集次数
     */
    private Integer count;


}
