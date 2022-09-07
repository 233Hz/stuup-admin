package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.service.ITermService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 学期信息维护
 * 提供对学期信息的新增、修改、删除、查询等功能
 * 删除属于逻辑删除
 */

@Api(tags = "学期信息相关接口")
@RestController
@RequestMapping("/term")
public class TermController {

    private final static Logger logger = LoggerFactory.getLogger(TermController.class);
    @Autowired
    private ITermService termService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel termList(String key, String current, String size) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return  termService.findDataPageResult(key, page, pageSize);
    }

   /* */

    /*
    @ResponseBody
    @RequestMapping("/saveOrUpdate")
    public AjaxResponse saveOrUpdate(Term term, HttpServletRequest request) {
        AjaxResponse ajaxResponse = new AjaxResponse<>(false);
        if (term == null) {
            ajaxResponse.setMessage("参数为空，保存失败");
            return ajaxResponse;
        }
        try {
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            if (!MicrovanUtil.isEmpty(startDate)) {
                term.setBeginTime(DateUtil.parse(startDate, DateUtil.DATE_FORAMT));
            }
            if (!MicrovanUtil.isEmpty(endDate)) {
                term.setEndTime(DateUtil.parse(endDate, DateUtil.DATE_FORAMT));
            }
            return termService.saveOrUpdateTerm(term);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存学期失败，原因：", e);
            ajaxResponse.setMessage("系统异常，请联系管理员");
        }
        return ajaxResponse;
    }

    */

    @RequestMapping(value = "/delMultiTerm", method = RequestMethod.POST)
    public ResponseModel delMultiTerm(@RequestBody Map params) {
        ResponseModel model = new ResponseModel();
        String ids = params.get("ids").toString();
        String[] idArr = ids.split(",");
        if (ids != null && idArr.length > 0) {
            for (String id : idArr) {
                termService.deleteByPrimaryKey(Integer.valueOf(id));
            }
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("删除成功");
        }
        else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("请选择后点击删除");
        }
        return model;
    }
}
