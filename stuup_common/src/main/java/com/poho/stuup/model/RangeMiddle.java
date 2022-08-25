package com.poho.stuup.model;

import java.util.Date;

public class RangeMiddle {
    private Long oid;

    private Long leaderRangeId;

    private Long assessRangeId;

    private Long createUser;

    private Date createTime;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Long getLeaderRangeId() {
        return leaderRangeId;
    }

    public void setLeaderRangeId(Long leaderRangeId) {
        this.leaderRangeId = leaderRangeId;
    }

    public Long getAssessRangeId() {
        return assessRangeId;
    }

    public void setAssessRangeId(Long assessRangeId) {
        this.assessRangeId = assessRangeId;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}