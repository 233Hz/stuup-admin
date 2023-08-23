package com.poho.stuup.model.vo;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 近30天每日访问量
 * @date 2023/8/22 14:24
 */

@Data
public class DailyVisitsVO {

    /**
     * 日期
     */
    private String date;

    /**
     * 访问量
     */
    private Integer count;
}
