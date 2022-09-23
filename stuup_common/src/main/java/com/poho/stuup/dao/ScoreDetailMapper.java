package com.poho.stuup.dao;

import com.poho.common.custom.PageData;
import com.poho.stuup.model.ScoreDetail;
import com.poho.stuup.model.dto.ScoreDetailDTO;
import com.poho.stuup.model.dto.ScoreDetailSearchDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScoreDetailMapper extends BaseDao<ScoreDetail> {

    int selectTotal(@Param("searchDTO") ScoreDetailSearchDTO searchDTO);

    List<ScoreDetailDTO> selectList(@Param("pageData") PageData pageData, @Param("searchDTO") ScoreDetailSearchDTO searchDTO);

}