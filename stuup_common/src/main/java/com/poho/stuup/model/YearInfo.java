package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 年份信息表
 * </p>
 *
 * @author BUNGA
 * @since 2023-07-19
 */
@Getter
@Setter
@TableName("t_year_info")
public class YearInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 年份id
     */
    private Long yearId;

    /**
     * 在校生人数
     */
    private Integer studentNum;


}
