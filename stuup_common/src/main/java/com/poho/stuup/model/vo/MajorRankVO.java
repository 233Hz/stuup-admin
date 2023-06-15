package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @description: 全校专业排名对象
 * @date 2023/6/2 13:20
 */
@Data
public class MajorRankVO {

    /**
     * 专业id
     */
    private Long id;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 所属系部
     */
    private String facultyName;

    /**
     * 成长值
     */
    private BigDecimal score;


}
