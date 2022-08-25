package com.poho.stuup.custom;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 09:13 2020/10/16
 * @Modified By:
 */
public class CusMap {
    private String type;
    private double value;

    public CusMap(String type, double value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
