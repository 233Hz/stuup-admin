package com.poho.stuup.custom;

import java.util.List;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 17:58 2020/10/20
 * @Modified By:
 */
public class CusMiddleRangeSubmit {
    private Long leaderRangeId;
    private List<Long> rangeIds;
    private Long createUser;

    public CusMiddleRangeSubmit() {

    }

    public Long getLeaderRangeId() {
        return leaderRangeId;
    }

    public void setLeaderRangeId(Long leaderRangeId) {
        this.leaderRangeId = leaderRangeId;
    }

    public List<Long> getRangeIds() {
        return rangeIds;
    }

    public void setRangeIds(List<Long> rangeIds) {
        this.rangeIds = rangeIds;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }
}
