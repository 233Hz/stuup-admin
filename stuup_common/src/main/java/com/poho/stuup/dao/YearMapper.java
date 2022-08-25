package com.poho.stuup.dao;

import com.poho.stuup.model.Year;

import java.util.Map;

public interface YearMapper extends BaseDao<Year> {
    /**
     *
     * @param param
     * @return
     */
    Year checkYear(Map<String, Object> param);

    /**
     * 查询当前年份
     * @return
     */
    Year findCurrYear();

    /**
     *
     * @param oid
     * @return
     */
    int updateSetCurrYear(Long oid);
}