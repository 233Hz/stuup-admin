package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.CertificateMapper;
import com.poho.stuup.model.Certificate;
import com.poho.stuup.service.ICertificateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CertificateServiceImpl implements ICertificateService {

    @Resource
    private CertificateMapper certificateMapper;

    @Override
    public ResponseModel findDataPageResult(String name, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        int count = certificateMapper.queryTotal(map);
        PageData pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Certificate> list = certificateMapper.queryList(map);
        if (MicrovanUtil.isNotEmpty(list)) {
            pageData.setRecords(list);
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
        } else {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("无数据");
        }
        model.setData(pageData);
        return model;
    }
}
