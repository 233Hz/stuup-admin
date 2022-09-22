package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.VolunteerExcelDTO;

import java.util.List;
import java.util.Map;

public interface IVolunteerService {

    ResponseModel findDataPageResult(String name, int page, int pageSize);

    Map<String, Object> importList(List<VolunteerExcelDTO> list);
}
