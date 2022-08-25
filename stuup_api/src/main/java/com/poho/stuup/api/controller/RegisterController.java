package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.model.Register;
import com.poho.stuup.service.IRegisterService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author wupeng
 * @Description 考核登记表逻辑
 * @Date 2020-09-22 9:19
 * @return
 */
@RestController
@RequestMapping("/reg")
public class RegisterController {
    @Resource
    private IRegisterService registerService;
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
    public ResponseModel list(String yearId, String deptId, String rangeDept, String state, String key, String current, String size) {
        String userId = ProjectUtil.obtainLoginUser(request);
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return registerService.findDataPageResult(userId, yearId, deptId, rangeDept, state, key, page, pageSize);
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/userData/{yearId}", method = RequestMethod.POST)
    public ResponseModel userData(@PathVariable Long yearId) {
        String userId = ProjectUtil.obtainLoginUser(request);
        return registerService.findData(yearId, Long.valueOf(userId));
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public ResponseModel data(Long oid) {
        return registerService.findData(oid);
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseModel save(@RequestBody Register register) {
        String userId = ProjectUtil.obtainLoginUser(request);
        register.setUserId(Long.valueOf(userId));
        register.setState(ProjectConstants.REG_STATE_OPINION);
        return registerService.saveOrUpdate(register);
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/submitLeaderOpinion", method = RequestMethod.POST)
    public ResponseModel submitLeaderOpinion(@RequestBody Register register) {
        register.setState(ProjectConstants.REG_STATE_SUBMIT);
        return registerService.submitLeaderOpinion(register);
    }

    /**
     * @param params
     * @return
     */
    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    public ResponseModel del(@RequestBody Map params) {
        String ids = params.get("ids").toString();
        String state = params.get("state").toString();
        return registerService.batch(ids, state);
    }
}
