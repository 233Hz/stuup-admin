package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;

public interface IPoliticalService {

    ResponseModel findDataPageResult(String name, int page, int pageSize);

}
