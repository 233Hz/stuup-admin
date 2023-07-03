package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Year;

import java.util.Date;

public interface IYearService {
    int deleteByPrimaryKey(Long oid);

    int insert(Year record);

    int insertSelective(Year record);

    Year selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(Year record);

    int updateByPrimaryKey(Year record);

    /**
     * @param key
     * @param page
     * @param pageSize
     * @return
     */
    ResponseModel findDataPageResult(String key, int page, int pageSize);

    /**
     * @param year
     * @return
     */
    ResponseModel saveOrUpdate(Year year);

    /**
     * @param ids
     * @return
     */
    ResponseModel del(String ids);

    /**
     * @return
     */
    ResponseModel queryList();

    /**
     * @param oid
     * @return
     */
    ResponseModel updateCurrYear(Long oid);

    /**
     * @param yearId
     * @return
     */
    ResponseModel findData(Long yearId);

    /**
     * @description: 通过开始时间和结束时间查找学年
     * @param: startTime
     * @param: endTime
     * @return: com.poho.stuup.model.Year
     * @author BUNGA
     * @date: 2023/6/28 15:06
     */
    Year findYearForStartAndEndTime(Date startTime, Date endTime);

    /**
     * @description: 查找某个时间所属的学年
     * @param: date
     * @return: com.poho.stuup.model.Year
     * @author BUNGA
     * @date: 2023/6/28 15:26
     */
    Year findTimeBelongYear(Date date);

    /**
     * @description: 设置所有学年不为当前年
     * @param:
     * @return: void
     * @author BUNGA
     * @date: 2023/6/28 15:36
     */
    void setAllYearNotCurr();

    /**
     * 获取当前学年
     *
     * @return
     */
    Year getCurrentYear();

    /**
     * 设置当前学年
     *
     * @param oid
     */
    void setCurrentYear(Long oid);
}