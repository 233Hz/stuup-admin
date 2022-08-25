package com.poho.stuup.dao;

import com.poho.stuup.model.Msg;

import java.util.Map;

public interface MsgMapper extends BaseDao<Msg> {
    /**
     *
     * @param param
     * @return
     */
    Msg checkYearMsg(Map<String, Object> param);
}