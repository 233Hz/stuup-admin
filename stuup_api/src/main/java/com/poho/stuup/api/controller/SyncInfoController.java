package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.api.config.PropertiesConfig;
import com.poho.stuup.service.SyncInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/syncInfo")
public class SyncInfoController {

    @Resource
    private SyncInfoService syncInfoService;

    @Resource
    private PropertiesConfig propertiesConfig;

    /**
     * 同步当前学期社团成员
     * @return
     */
    @GetMapping("/syncCommunityMember")
    public ResponseModel syncCommunityMember() {
        return syncInfoService.syncCommunityMember(propertiesConfig.getCommunityUrl());
    }

}
