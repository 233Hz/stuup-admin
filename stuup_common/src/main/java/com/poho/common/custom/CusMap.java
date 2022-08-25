package com.poho.common.custom;

/**
 * @Author wupeng
 * @Description 模糊搜索数据模型
 * @Date 2020-09-13 9:58
 * @return
 */
public class CusMap {
    private Long oid;
    private String value;
    private boolean disabled;

    public CusMap(Long oid, String value) {
        this.oid = oid;
        this.value = value;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
