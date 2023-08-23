package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @description: 学生成长信息
 * @date 2023/8/9 17:02
 */

@Data
public class UnCollectScore {

    private Long id;

    private BigDecimal score;
}
