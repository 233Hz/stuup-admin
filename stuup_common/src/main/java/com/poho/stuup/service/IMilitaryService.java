package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.MilitaryExcelDTO;
import com.poho.stuup.model.dto.MilitarySearchDTO;

import java.util.List;
import java.util.Map;

public interface IMilitaryService {

    ResponseModel findDataPageResult(MilitarySearchDTO searchDTO);

    Map<String, Object> importList(List<MilitaryExcelDTO> list);
}
