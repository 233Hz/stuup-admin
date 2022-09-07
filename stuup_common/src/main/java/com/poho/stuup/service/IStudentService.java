package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;


public interface IStudentService {

    ResponseModel findDataPageResult( String grade, String major, String key, int page, int pageSize);


}
