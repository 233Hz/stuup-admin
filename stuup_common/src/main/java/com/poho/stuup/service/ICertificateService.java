package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;

public interface ICertificateService {

    ResponseModel findDataPageResult(String name, int page, int pageSize);
}
