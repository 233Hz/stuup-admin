package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.ContestExcelDTO;
import com.poho.stuup.model.dto.ContestSearchDTO;

import java.util.List;
import java.util.Map;

public interface IContestService {

    ResponseModel findDataPageResult(ContestSearchDTO searchDTO);

    Map<String, Object> importList(List<ContestExcelDTO> list);
}
