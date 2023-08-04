package com.poho.stuup.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CalculateFailDTO {

    private Long studentId;
    private Long growId;
    private String error;
    private Long yearId;
    private Long semesterId;
    private Integer count;
    private Date createTime;
    private Integer calculateType;
}
