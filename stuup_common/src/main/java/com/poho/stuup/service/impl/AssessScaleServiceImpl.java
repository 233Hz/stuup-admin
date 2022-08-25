package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.custom.CusAssessScale;
import com.poho.stuup.dao.AssessScaleMapper;
import com.poho.stuup.dao.DeptMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.model.AssessScale;
import com.poho.stuup.model.Dept;
import com.poho.stuup.service.IAssessScaleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 19:20 2020/9/2
 * @Modified By:
 */
@Service
public class AssessScaleServiceImpl implements IAssessScaleService {
    @Resource
    private AssessScaleMapper assessScaleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private DeptMapper deptMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return assessScaleMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(AssessScale record) {
        return assessScaleMapper.insert(record);
    }

    @Override
    public int insertSelective(AssessScale record) {
        return assessScaleMapper.insertSelective(record);
    }

    @Override
    public AssessScale selectByPrimaryKey(Long oid) {
        return assessScaleMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(AssessScale record) {
        return assessScaleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AssessScale record) {
        return assessScaleMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findDataPageResult(String yearId, String key, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("yearId", Long.valueOf(yearId));
        int count = assessScaleMapper.queryTotal(map);
        PageData<AssessScale> pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<AssessScale> list = assessScaleMapper.queryList(map);
        if (MicrovanUtil.isNotEmpty(list)) {
            for (AssessScale assessScale : list) {
                map.put("deptId", assessScale.getDeptId());
                map.put("userType", ProjectConstants.RANGE_TYPE_PTYG);
                int total = userMapper.findAssessUserTotal(map);
                assessScale.setTotal(total);
            }
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
    public ResponseModel findData(Long yearId, Long deptId) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", yearId);
        param.put("deptId", deptId);
        AssessScale assessScale = assessScaleMapper.checkAssessScale(param);
        if (MicrovanUtil.isNotEmpty(assessScale)) {
            CusAssessScale cusAssessScale = new CusAssessScale();
            cusAssessScale.setaMin(assessScale.getaMin());
            cusAssessScale.setaMax(assessScale.getaMax());
            cusAssessScale.setbMin(assessScale.getbMin());
            cusAssessScale.setbMax(assessScale.getbMax());
            cusAssessScale.setcMin(assessScale.getcMin());
            cusAssessScale.setcMax(assessScale.getcMax());
            cusAssessScale.setdMin(assessScale.getdMin());
            cusAssessScale.setdMax(assessScale.getdMax());
            param.put("userType", ProjectConstants.RANGE_TYPE_PTYG);
            int total = userMapper.findAssessUserTotal(param);
            cusAssessScale.setTotal(total);
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
            model.setData(cusAssessScale);
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("无数据");
        }
        return model;
    }

    @Override
    public ResponseModel saveOrUpdate(AssessScale scale) {
        ResponseModel model = new ResponseModel();
        int line;
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", scale.getYearId());
        param.put("deptId", scale.getDeptId());
        AssessScale checkAssessScale = assessScaleMapper.checkAssessScale(param);
        if (MicrovanUtil.isNotEmpty(checkAssessScale)) {
            scale.setCreateUser(null);
            line = assessScaleMapper.updateByPrimaryKeySelective(scale);
        } else {
            scale.setCreateTime(new Date());
            line = assessScaleMapper.insertSelective(scale);
        }
        if (line > 0) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("保存成功");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("保存失败，请稍后重试");
        }
        return model;
    }

    @Override
    public ResponseModel del(String ids) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMessage("删除失败，请稍后重试");
        String[] idArr = ids.split(",");
        if (MicrovanUtil.isNotEmpty(idArr)) {
            int line = assessScaleMapper.deleteBatch(idArr);
            if (line > 0) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("删除成功");
            }
        }
        return model;
    }

    @Override
    public ResponseModel init(Long yearId) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        int res = 0;
        List<Dept> deptList = deptMapper.queryList(null);
        if (MicrovanUtil.isNotEmpty(deptList)) {
            for (Dept dept : deptList) {
                Map<String, Object> param = new HashMap<>();
                param.put("yearId", yearId);
                param.put("deptId", dept.getOid());
                AssessScale scale = assessScaleMapper.checkAssessScale(param);
                if (MicrovanUtil.isEmpty(scale)) {
                    scale = new AssessScale();
                    scale.setYearId(yearId);
                    scale.setDeptId(dept.getOid());
                    param.put("userType", ProjectConstants.RANGE_TYPE_PTYG);
                    scale.setaMin(0);
                    scale.setaMax(20);
                    scale.setbMin(0);
                    scale.setbMax(100);
                    scale.setcMin(0);
                    scale.setcMax(10);
                    scale.setdMin(0);
                    scale.setdMax(10);
                    scale.setCreateTime(new Date());
                    int line = assessScaleMapper.insertSelective(scale);
                    if (line > 0) {
                        res = res + 1;
                    }
                }
            }
        }
        if (res > 0) {
            model.setMessage("成功发现并初始化" + res + "个新部门");
        } else {
            model.setMessage("未发现新的部门");
        }
        model.setData(res);
        return model;
    }
}
