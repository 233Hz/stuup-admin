package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@TableName("t_job_grow_student")
public class JobGrowStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父级定时任务id
     */
    private Long jobGrowId;

    /**
     * 学生id
     */
    private Long studentId;


}
