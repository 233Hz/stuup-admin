package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.service.IYearService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author wupeng
 * @Description 学年管理
 * @Date 2020-08-07 22:47
 * @return
 */
@RestController
@RequestMapping("/year")
public class YearController {
    @Resource
    private IYearService yearService;
    @Resource
    private HttpServletRequest request;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list(String yearName, String current, String size) {
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        return yearService.findDataPageResult(yearName, page, pageSize);
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public ResponseModel data(Long yearId) {
        return yearService.findData(yearId);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseModel all() {
        return yearService.queryList();
    }


}
