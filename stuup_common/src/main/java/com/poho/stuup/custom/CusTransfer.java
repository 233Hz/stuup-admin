package com.poho.stuup.custom;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 10:01 2020/10/20
 * @Modified By:
 */
public class CusTransfer {
    private Long key;
    private String label;
    private boolean disabled = false;

    public CusTransfer() {

    }

    public CusTransfer(Long key, String label) {
        this.key = key;
        this.label = label;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
