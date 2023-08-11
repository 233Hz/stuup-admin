package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.vo.GrowthInfo;

public interface GrowthInfoService {


    ResponseModel<GrowthInfo> getInfo(Long userId);
}
