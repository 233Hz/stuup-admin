package com.poho.stuup.dao;


import com.poho.common.custom.PageData;
import com.poho.stuup.model.Political;
import com.poho.stuup.model.dto.PoliticalDTO;
import com.poho.stuup.model.dto.PoliticalSearchDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PoliticalMapper extends BaseDao<Political> {

    int selectTotal(@Param("searchDTO") PoliticalSearchDTO searchDTO);

    List<PoliticalDTO> selectList(@Param("pageData") PageData pageData, @Param("searchDTO")PoliticalSearchDTO searchDTO);

}