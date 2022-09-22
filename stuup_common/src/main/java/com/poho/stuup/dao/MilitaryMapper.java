package com.poho.stuup.dao;

import com.poho.common.custom.PageData;
import com.poho.stuup.model.Military;
import com.poho.stuup.model.dto.MilitaryDTO;
import com.poho.stuup.model.dto.MilitarySearchDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MilitaryMapper extends BaseDao<Military> {

    int selectTotal(@Param("searchDTO") MilitarySearchDTO searchDTO);

    List<MilitaryDTO> selectList(@Param("pageData") PageData pageData, @Param("searchDTO") MilitarySearchDTO searchDTO);
}