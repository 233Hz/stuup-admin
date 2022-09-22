package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.PoliticalExcelDTO;

import java.util.List;
import java.util.Map;

public interface IPoliticalService {

    ResponseModel findDataPageResult(String name, int page, int pageSize);

    Map<String, Object> importList(List<PoliticalExcelDTO> list);

}
