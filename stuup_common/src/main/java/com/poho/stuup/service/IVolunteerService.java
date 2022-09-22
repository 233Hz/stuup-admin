package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.VolunteerExcelDTO;
import com.poho.stuup.model.dto.VolunteerSearchDTO;

import java.util.List;
import java.util.Map;

public interface IVolunteerService {

    ResponseModel findDataPageResult(VolunteerSearchDTO searchDTO);

    Map<String, Object> importList(List<VolunteerExcelDTO> list);
}
