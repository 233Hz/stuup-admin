package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;

public interface IVolunteerService {

    ResponseModel findDataPageResult(String name, int page, int pageSize);
}
