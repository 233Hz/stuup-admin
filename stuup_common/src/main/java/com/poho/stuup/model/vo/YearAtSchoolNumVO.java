package com.poho.stuup.model.vo;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 近三年在校生可视化对象
 * @date 2023/7/20 8:47
 */
@Data
public class YearAtSchoolNumVO {

    /**
     * 学年
     */
    private String year;

    /**
     * 人数
     */
    private Integer personNum;
}
