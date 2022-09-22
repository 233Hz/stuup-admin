package com.poho.stuup.dao;

import com.poho.common.custom.PageData;
import com.poho.stuup.model.Contest;
import com.poho.stuup.model.dto.ContestDTO;
import com.poho.stuup.model.dto.ContestSearchDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContestMapper extends BaseDao<Contest> {

    int selectTotal(@Param("searchDTO") ContestSearchDTO searchDTO);

    List<ContestDTO> selectList(@Param("pageData") PageData pageData, @Param("searchDTO") ContestSearchDTO searchDTO);
}