package com.poho.stuup.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author BUNGA
 * @description: 系部排名对象
 * @date 2023/6/28 19:17
 */
@Data
public class FacultyRankVO {

    /**
     * 系部id
     */
    private Integer facultyId;

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 所属系部
     */
    private String facultyName;

    /**
     * 成长值
     */
    private BigDecimal score;

    /**
     * 班级id
     */
    @JsonIgnore
    private Integer classId;

}
