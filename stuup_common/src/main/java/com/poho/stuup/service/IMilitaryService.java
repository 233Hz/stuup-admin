package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;

public interface IMilitaryService {

    ResponseModel findDataPageResult(String name, int page, int pageSize);
}
