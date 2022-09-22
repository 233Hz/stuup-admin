package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.RewardExcelDTO;

import java.util.List;
import java.util.Map;

public interface IRewardService {

    ResponseModel findDataPageResult(String name, int page, int pageSize);

    Map<String, Object> importList(List<RewardExcelDTO> list);
}
