package com.poho.stuup.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/8/5 23:08
 */
@Data
public class StudentScoreDetailsVO {

    private BigDecimal totalScore;

    private List<StudentRecScoreVO> records;
}
