package com.poho.stuup.dao;

import com.poho.common.custom.PageData;
import com.poho.stuup.model.Score;
import com.poho.stuup.model.dto.ScoreDTO;
import com.poho.stuup.model.dto.ScoreSearchDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScoreMapper extends BaseDao<Score> {

    int selectTotal(@Param("searchDTO") ScoreSearchDTO searchDTO);

    List<ScoreDTO> selectList(@Param("pageData") PageData pageData, @Param("searchDTO") ScoreSearchDTO searchDTO);
}