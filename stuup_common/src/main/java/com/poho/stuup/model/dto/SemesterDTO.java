package com.poho.stuup.model.dto;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 学期页面对象
 * @date 2023/6/27 10:08
 */
@Data
public class SemesterDTO {

    /**
     * 学年id
     */
    private Long yearId;

    /**
     * 学期名称
     */
    private String name;

}
