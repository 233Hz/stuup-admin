package com.poho.stuup.custom;

import com.poho.stuup.model.Semester;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CusUser {
    private Long userId;
    private String userName;
    private String loginName;
    private Integer sex;
    private String mobile;
    private String idCard;
    private String birthday;
    private Long deptId;
    private String deptName;
    private Integer userType;
    private String degree;
    private String roleIds;
    private Long yearId;
    private Long semesterId;
    private String avatar;
    private Integer ranking;
    private BigDecimal totalScore;
    private Integer studentId;
    private List<Semester> semesters;
}
