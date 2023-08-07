package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.SyncInfo;


public interface SyncInfoService extends IService<SyncInfo> {


    ResponseModel syncCommunityMember(String url);

}
