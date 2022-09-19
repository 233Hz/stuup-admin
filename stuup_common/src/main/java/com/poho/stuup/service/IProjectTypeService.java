package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;

public interface IProjectTypeService {

    ResponseModel findDataPageResult(String key, int page, int pageSize);
}
