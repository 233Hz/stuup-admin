package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;

public interface IRewardService {

    ResponseModel findDataPageResult(String name, int page, int pageSize);
}
