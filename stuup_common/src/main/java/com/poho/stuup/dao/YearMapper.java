package com.poho.stuup.dao;

import com.poho.stuup.model.Year;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface YearMapper extends BaseDao<Year> {
    /**
     * @param param
     * @return
     */
    Year checkYear(Map<String, Object> param);

    /**
     * 查询当前年份
     *
     * @return
     */
    Year findCurrYear();

    Long findCurrYearId();

    /**
     * @param oid
     * @return
     */
    int updateSetCurrYear(Long oid);

    Year findYearForStartAndEndTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    Year findTimeBelongYear(@Param("date") Date date);

    void setAllYearNotCurr();

    void setCurrentYear(@Param("oid") Long oid);

    List<Long> findRangeYearStart(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    Year findByRange(@Param("date") Date date);
}