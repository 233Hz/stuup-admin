package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.CusMap;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.DeptMapper;
import com.poho.stuup.model.Dept;
import com.poho.stuup.service.IDeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 11:24 2019/11/30
 * @Modified By:
 */
@Service
public class DeptServiceImpl implements IDeptService {
    @Resource
    private DeptMapper deptMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return deptMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(Dept record) {
        return deptMapper.insert(record);
    }

    @Override
    public int insertSelective(Dept record) {
        return deptMapper.insertSelective(record);
    }

    @Override
    public Dept selectByPrimaryKey(Long oid) {
        return deptMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(Dept record) {
        return deptMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Dept record) {
        return deptMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findDataPageResult(String key, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        int count = deptMapper.queryTotal(map);
        PageData pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Dept> list = deptMapper.queryList(map);
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
    public ResponseModel saveOrUpdate(Dept dept) {
        ResponseModel model = new ResponseModel();
        int line = 0;
        Map<String, Object> param = new HashMap<>();
        param.put("deptName", dept.getDeptName());
        if (MicrovanUtil.isNotEmpty(dept.getOid())) {
            param.put("oid", dept.getOid());
        }
        Dept checkDept = deptMapper.checkDept(param);
        if (MicrovanUtil.isNotEmpty(checkDept)) {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("部门名称已存在");
        } else {
            if (MicrovanUtil.isNotEmpty(dept.getOid())) {
                dept.setCreateUser(null);
                line = deptMapper.updateByPrimaryKeySelective(dept);
            } else {
                dept.setCreateTime(new Date());
                line = deptMapper.insertSelective(dept);
            }
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
            int line = deptMapper.deleteBatch(idArr);
            if (line > 0) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("删除成功");
            }
        }
        return model;
    }

    @Override
    public ResponseModel findData() {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        List<Dept> list = deptMapper.queryList(null);
        if (MicrovanUtil.isNotEmpty(list)) {
            List<CusMap> data = new ArrayList<>();
            for (Dept dept : list) {
                CusMap map = new CusMap(dept.getOid(), dept.getDeptName());
                data.add(map);
            }
            model.setData(data);
        }
        return model;
    }
}
