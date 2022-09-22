package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.PoliticalExcelDTO;
import com.poho.stuup.model.dto.PoliticalSearchDTO;

import java.util.List;
import java.util.Map;

public interface IPoliticalService {

    ResponseModel findDataPageResult(PoliticalSearchDTO searchDTO);

    Map<String, Object> importList(List<PoliticalExcelDTO> list);

}
