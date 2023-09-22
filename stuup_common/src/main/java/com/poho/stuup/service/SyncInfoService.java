package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.SyncInfo;


public interface SyncInfoService extends IService<SyncInfo> {

    public ResponseModel<Integer> getRemoteOpenCommunityTotal(String url);

    ResponseModel syncCommunityMember(String url, String termName);
    ResponseModel syncCommunityMember(String url);

}
