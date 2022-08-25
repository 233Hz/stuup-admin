package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.model.Year;
import com.poho.stuup.service.IYearService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

    /**
     *
     * @param key
     * @param current
     * @param size
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list(String key, String current, String size) {
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        return yearService.findDataPageResult(key, page, pageSize);
    }

    /**
     *
     * @param yearId
     * @return
     */
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public ResponseModel data(Long yearId) {
        return yearService.findData(yearId);
    }

    /**
     * 查询所有的数据
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseModel all() {
        return yearService.queryList();
    }

    /**
     *
     * @param year
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseModel save(@RequestBody Year year) {
        String sesUser = ProjectUtil.obtainLoginUser(request);
        year.setCreateUser(Long.valueOf(sesUser));
        return yearService.saveOrUpdate(year);
    }

    /**
     * @param params
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public ResponseModel del(@RequestBody Map params) {
        String ids = params.get("ids").toString();
        return yearService.del(ids);
    }

    /**
     * @param oid
     * @return
     */
    @RequestMapping(value = "/setCurr/{oid}", method = RequestMethod.POST)
    public ResponseModel setCurr(@PathVariable Long oid) {
        return yearService.updateCurrYear(oid);
    }
}
