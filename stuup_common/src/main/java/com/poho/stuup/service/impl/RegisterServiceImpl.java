package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.dao.AssessRangeMapper;
import com.poho.stuup.dao.RegisterMapper;
import com.poho.stuup.dao.UserRoleMapper;
import com.poho.stuup.model.AssessRange;
import com.poho.stuup.model.Register;
import com.poho.stuup.service.IRegisterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author wupeng
 * @Description 考核登记表逻辑接口实现类
 * @Date 2020-09-22 9:16
 * @return
 */
@Service
public class RegisterServiceImpl implements IRegisterService {
    @Resource
    private RegisterMapper registerMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private AssessRangeMapper assessRangeMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return registerMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(Register record) {
        return registerMapper.insert(record);
    }

    @Override
    public int insertSelective(Register record) {
        return registerMapper.insertSelective(record);
    }

    @Override
    public Register selectByPrimaryKey(Long oid) {
        return registerMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(Register record) {
        return registerMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Register record) {
        return registerMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findDataPageResult(String userId, String yearId, String deptId, String rangeDept, String state, String key, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("yearId", Long.valueOf(yearId));
        map.put("userId", Long.valueOf(userId));
        map.put("key", key);
        List<Long> roleIds = userRoleMapper.queryUserYearRoles(map);
        if (roleIds.contains(ProjectConstants.ROLE_YWGLY)) {
            if (MicrovanUtil.isNotEmpty(deptId)) {
                map.put("deptId", Long.valueOf(deptId));
            }
        } else if (roleIds.contains(ProjectConstants.ROLE_FGLD)) {
            map.put("cond", "fgld");
            AssessRange assessRange = assessRangeMapper.checkAssessRange(map);
            if (MicrovanUtil.isNotEmpty(assessRange)) {
                map.put("userRangeId", assessRange.getOid());
            }
        } else if (roleIds.contains(ProjectConstants.ROLE_DZLD)) {
            map.put("cond", "dzld");
            if (MicrovanUtil.isNotEmpty(deptId)) {
                map.put("deptId", Long.valueOf(deptId));
            }
        } else if (roleIds.contains(ProjectConstants.ROLE_RLZYCCZ)) {
            map.put("cond", "rlzyccz");
            map.put("rangeDept", Long.valueOf(rangeDept));
            if (MicrovanUtil.isNotEmpty(deptId)) {
                map.put("deptId", Long.valueOf(deptId));
            }
        } else if (roleIds.contains(ProjectConstants.ROLE_BMFZR)) {
            map.put("cond", "bmfzr");
            map.put("rangeDept", Long.valueOf(rangeDept));
        } else {
            if (MicrovanUtil.isNotEmpty(deptId)) {
                map.put("deptId", Long.valueOf(deptId));
            }
        }
        if (MicrovanUtil.isNotEmpty(state)) {
            map.put("state", Integer.valueOf(state));
        }
        int count = registerMapper.queryTotal(map);
        PageData<Register> pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Register> list = registerMapper.queryList(map);
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

    @Override
    public ResponseModel findData(Long yearId, Long userId) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", yearId);
        param.put("userId", userId);
        Register register = registerMapper.checkRegister(param);
        if (MicrovanUtil.isNotEmpty(register)) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
            model.setData(register);
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("无数据");
        }
        return model;
    }

    @Override
    public ResponseModel findData(Long oid) {
        ResponseModel model = new ResponseModel();
        Register register = registerMapper.selectByPrimaryKey(oid);
        if (MicrovanUtil.isNotEmpty(register)) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
            model.setData(register);
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("无数据");
        }
        return model;
    }

    @Override
    public ResponseModel saveOrUpdate(Register register) {
        ResponseModel model = new ResponseModel();
        int line;
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", register.getYearId());
        param.put("userId", register.getUserId());
        Register checkReg = registerMapper.checkRegister(param);
        if (MicrovanUtil.isNotEmpty(checkReg)) {
            register.setUserId(null);
            register.setOid(checkReg.getOid());
            line = registerMapper.updateByPrimaryKeySelective(register);
        } else {
            register.setCreateTime(new Date());
            line = registerMapper.insertSelective(register);
        }
        if (line > 0) {
            AssessRange assessRange = assessRangeMapper.checkAssessRange(param);
            if (MicrovanUtil.isNotEmpty(assessRange)) {
                if (assessRange.getUserType() == ProjectConstants.RANGE_TYPE_BMFZR || assessRange.getUserType() == ProjectConstants.RANGE_TYPE_QTZC) {
                    register.setState(ProjectConstants.REG_STATE_SUBMIT);
                    registerMapper.updateByPrimaryKeySelective(register);
                }
            }
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("保存成功");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("保存失败，请稍后重试");
        }
        return model;
    }

    @Override
    public ResponseModel batch(String ids, String state) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMessage("处理失败，请稍后重试");
        String[] idArr = ids.split(",");
        if (MicrovanUtil.isNotEmpty(idArr)) {
            Map<String, Object> param = new HashMap<>();
            param.put("ids", idArr);
            param.put("state", Integer.valueOf(state));
            int line = registerMapper.batchUpdate(param);
            if (line > 0) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("处理成功");
            }
        }
        return model;
    }

    @Override
    public ResponseModel submitLeaderOpinion(Register register) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMessage("保存失败，请稍后重试");
        int line = 0;
        if (MicrovanUtil.isNotEmpty(register.getOid())) {
            line = registerMapper.updateByPrimaryKeySelective(register);
        }
        if (line > 0) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("保存成功");
        }
        return model;
    }
}
