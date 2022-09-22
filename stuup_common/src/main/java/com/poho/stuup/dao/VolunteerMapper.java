package com.poho.stuup.dao;

import com.poho.common.custom.PageData;
import com.poho.stuup.model.Volunteer;
import com.poho.stuup.model.dto.VolunteerDTO;
import com.poho.stuup.model.dto.VolunteerSearchDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VolunteerMapper extends BaseDao<Volunteer> {

    int selectTotal(@Param("searchDTO") VolunteerSearchDTO searchDTO);

    List<VolunteerDTO> selectList(@Param("pageData") PageData pageData, @Param("searchDTO") VolunteerSearchDTO searchDTO);
}