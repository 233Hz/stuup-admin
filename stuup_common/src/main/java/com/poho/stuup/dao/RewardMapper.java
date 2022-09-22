package com.poho.stuup.dao;

import com.poho.common.custom.PageData;
import com.poho.stuup.model.Reward;
import com.poho.stuup.model.dto.RewardDTO;
import com.poho.stuup.model.dto.RewardSearchDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RewardMapper extends BaseDao<Reward> {

    int selectTotal(@Param("searchDTO") RewardSearchDTO searchDTO);

    List<RewardDTO> selectList(@Param("pageData") PageData pageData, @Param("searchDTO") RewardSearchDTO searchDTO);

}