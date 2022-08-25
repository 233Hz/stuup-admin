package com.poho.stuup.custom;

import java.util.List;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 16:38 2020/9/3
 * @Modified By:
 */
public class TreeData {
    private Long id;
    private String label;
    private List<TreeData> children;

    public TreeData() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeData> getChildren() {
        return children;
    }

    public void setChildren(List<TreeData> children) {
        this.children = children;
    }
}
