package com.poho.stuup.custom;

import java.util.List;

/**
 * @Author wupeng
 * @Description 评价用户数据模型
 * @Date 2020-09-19 14:57
 * @return
 */
public class CusAssessUser {
    private Long userId;
    private String userName;
    private List<CusNormScore> scores;

    public CusAssessUser () {

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<CusNormScore> getScores() {
        return scores;
    }

    public void setScores(List<CusNormScore> scores) {
        this.scores = scores;
    }
}