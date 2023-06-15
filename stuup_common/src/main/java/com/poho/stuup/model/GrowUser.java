package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 项目负责人表
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Getter
@Setter
@TableName("t_grow_user")
public class GrowUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    private Long growId;

    /**
     * 用户id
     */
    private Long userId;


}
