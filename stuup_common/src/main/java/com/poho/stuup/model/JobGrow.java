package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-18
 */
@Getter
@Setter
@TableName("t_job_grow")
public class JobGrow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目id
     */
    private Long growId;

    /**
     * 执行日期
     */
    private LocalDate execDate;

    /**
     * 执行状态（1.完成 2.未完成）
     */
    private Integer state;

    /**
     * 备注
     */
    private String error;


}
