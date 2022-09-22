package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.MilitaryExcelDTO;

import java.util.List;
import java.util.Map;

public interface IMilitaryService {

    ResponseModel findDataPageResult(String name, int page, int pageSize);

    Map<String, Object> importList(List<MilitaryExcelDTO> list);
}
