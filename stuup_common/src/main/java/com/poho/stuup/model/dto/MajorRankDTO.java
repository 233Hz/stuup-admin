package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * @author BUNGA
 * @date 2023/6/4 13:54
 */
@Data
public class MajorRankDTO {

    /**
     * 班级名称
     */
    private String majorName;

    /**
     * 所属系部
     */
    private Long facultyId;

}
