package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.OperRecord;
import com.poho.stuup.service.IOperRecordService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: wupeng
 * @Description: 测评表下发记录
 * @Date: Created in 15:02 2020/9/18
 * @Modified By:
 */
@RestController
@RequestMapping("/config")
public class OperRecordController {
    @Resource
    private IOperRecordService operRecordService;
    @Resource
    private HttpServletRequest request;

    /**
     * @param yearId
     * @param operType
     * @return
     */
    @RequestMapping(value = "/operData", method = RequestMethod.GET)
    public ResponseModel operData(Long yearId, Integer operType) {
        return operRecordService.findOperData(yearId, operType);
    }

    /**
     * @param record
     * @return
     */
    @RequestMapping(value = "/sendNorm", method = RequestMethod.POST)
    public ResponseModel sendNorm(@RequestBody OperRecord record) {
        String sesUser = ProjectUtil.obtainLoginUser(request);
        record.setOperUser(Long.valueOf(sesUser));
        return operRecordService.sendNorm(record);
    }
}
