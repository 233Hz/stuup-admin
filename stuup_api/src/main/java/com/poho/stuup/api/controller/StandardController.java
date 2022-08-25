package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.model.StandardCategory;
import com.poho.stuup.model.StandardNorm;
import com.poho.stuup.service.IStandardCategoryService;
import com.poho.stuup.service.IStandardNormService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 14:33 2020/9/18
 * @Modified By:
 */
@RestController
@RequestMapping("/config")
public class StandardController {
    @Resource
    private IStandardCategoryService standardCategoryService;
    @Resource
    private IStandardNormService standardNormService;
    @Resource
    private HttpServletRequest request;

    /**
     *
     * @param key
     * @param current
     * @param size
     * @return
     */
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ResponseModel category(String key, String current, String size) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return standardCategoryService.findDataPageResult(key, page, pageSize);
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/categoryAll", method = RequestMethod.GET)
    public ResponseModel categoryAll() {
        return standardCategoryService.findData();
    }

    /**
     *
     * @param category
     * @return
     */
    @RequestMapping(value = "/saveCategory", method = RequestMethod.POST)
    public ResponseModel saveCategory(@RequestBody StandardCategory category) {
        String sesUser = ProjectUtil.obtainLoginUser(request);
        category.setCreateUser(Long.valueOf(sesUser));
        return standardCategoryService.saveOrUpdate(category);
    }

    /**
     * @param params
     * @return
     */
    @RequestMapping(value = "/delCategory", method = RequestMethod.POST)
    public ResponseModel delCategory(@RequestBody Map params) {
        String ids = params.get("ids").toString();
        return standardCategoryService.del(ids);
    }

    /**
     *
     * @param key
     * @param current
     * @param size
     * @return
     */
    @RequestMapping(value = "/norm", method = RequestMethod.GET)
    public ResponseModel norm(String categoryId, String key, String current, String size) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return standardNormService.findDataPageResult(categoryId, key, page, pageSize);
    }

    /**
     *
     * @param standardNorm
     * @return
     */
    @RequestMapping(value = "/saveNorm", method = RequestMethod.POST)
    public ResponseModel saveNorm(@RequestBody StandardNorm standardNorm) {
        String sesUser = ProjectUtil.obtainLoginUser(request);
        standardNorm.setCreateUser(Long.valueOf(sesUser));
        return standardNormService.saveOrUpdate(standardNorm);
    }

    /**
     * @param params
     * @return
     */
    @RequestMapping(value = "/delNorm", method = RequestMethod.POST)
    public ResponseModel delNorm(@RequestBody Map params) {
        String ids = params.get("ids").toString();
        return standardNormService.del(ids);
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/standard", method = RequestMethod.GET)
    public ResponseModel standard() {
        return standardCategoryService.findStandardData();
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/assessStandard", method = RequestMethod.GET)
    public ResponseModel assessStandard() {
        return standardCategoryService.findAssessStandardData();
    }
}
