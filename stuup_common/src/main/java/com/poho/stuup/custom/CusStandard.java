package com.poho.stuup.custom;

import com.poho.stuup.model.StandardNorm;

import java.util.List;

/**
 * @Author wupeng
 * @Description 测评表数据
 * @Date 2020-09-18 20:10
 * @return
 */
public class CusStandard {
    private Long categoryId;
    private String categoryName;
    private Integer categoryScore;
    private List<StandardNorm> standardNorms;

    public CusStandard() {
    }

    public CusStandard(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryScore() {
        return categoryScore;
    }

    public void setCategoryScore(Integer categoryScore) {
        this.categoryScore = categoryScore;
    }

    public List<StandardNorm> getStandardNorms() {
        return standardNorms;
    }

    public void setStandardNorms(List<StandardNorm> standardNorms) {
        this.standardNorms = standardNorms;
    }
}
