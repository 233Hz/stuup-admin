package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.RewardExcelDTO;
import com.poho.stuup.model.dto.RewardSearchDTO;

import java.util.List;
import java.util.Map;

public interface IRewardService {

    ResponseModel findDataPageResult(RewardSearchDTO searchDTO);

    Map<String, Object> importList(List<RewardExcelDTO> list);
}
