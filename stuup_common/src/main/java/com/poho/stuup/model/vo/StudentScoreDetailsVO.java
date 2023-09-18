package com.poho.stuup.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @date 2023/8/5 23:08
 */
@Data
public class StudentScoreDetailsVO {

    private BigDecimal totalScore;

    private IPage<StudentRecScoreVO> detailPage;
}
