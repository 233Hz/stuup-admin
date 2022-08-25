package com.poho.stuup.custom;

import io.swagger.annotations.ApiModelProperty;

public class CusDept {
    @ApiModelProperty("部门ID")
    private Long deptId;
    @ApiModelProperty("部门名称")
    private String deptName;

    public CusDept() {

    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
