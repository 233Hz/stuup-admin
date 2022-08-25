package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.custom.CusMsg;
import com.poho.stuup.model.Msg;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 13:55 2019/12/1
 * @Modified By:
 */
public interface IMsgService {
    int deleteByPrimaryKey(Long oid);

    int insert(Msg record);

    int insertSelective(Msg record);

    Msg selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(Msg record);

    int updateByPrimaryKey(Msg record);

    /**
     *
     * @param mobile
     * @return
     */
    ResponseModel<CusMsg> sendLoginCode(String mobile);
}
