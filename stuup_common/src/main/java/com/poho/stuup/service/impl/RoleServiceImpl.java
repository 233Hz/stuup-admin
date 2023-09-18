package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.CusMap;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.RoleMapper;
import com.poho.stuup.model.Role;
import com.poho.stuup.service.IRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author wupeng
 * @Description 角色管理接口实现类
 * @Date 2020-08-07 22:18
 * @return
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return roleMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(Role record) {
        return roleMapper.insert(record);
    }

    @Override
    public int insertSelective(Role record) {
        return roleMapper.insertSelective(record);
    }

    @Override
    public Role selectByPrimaryKey(Long oid) {
        return roleMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(Role record) {
        return roleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Role record) {
        return roleMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findDataPageResult(String key, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        int count = roleMapper.queryTotal(map);
        PageData<Role> pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Role> list = roleMapper.queryList(map);
        if (MicrovanUtil.isNotEmpty(list)) {
            pageData.setRecords(list);
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMsg("请求成功");
        } else {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMsg("无数据");
        }
        model.setData(pageData);
        return model;
    }

    @Override
    public ResponseModel saveOrUpdate(Role role) {
        ResponseModel model = new ResponseModel();
        int line;
        Map<String, Object> param = new HashMap<>();
        param.put("roleName", role.getRoleName());
        if (MicrovanUtil.isNotEmpty(role.getOid())) {
            param.put("oid", role.getOid());
        }
        Role checkRole = roleMapper.checkRole(param);
        if (MicrovanUtil.isNotEmpty(checkRole)) {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMsg("角色名称已存在");
            return model;
        } else {
            if (MicrovanUtil.isNotEmpty(role.getOid())) {
                role.setCreateUser(null);
                line = roleMapper.updateByPrimaryKeySelective(role);
            } else {
                role.setCreateTime(new Date());
                line = roleMapper.insertSelective(role);
            }
        }
        if (line > 0) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMsg("保存成功");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMsg("保存失败，请稍后重试");
        }
        return model;
    }

    @Override
    public ResponseModel del(String ids) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMsg("删除失败，请稍后重试");
        String[] idArr = ids.split(",");
        if (MicrovanUtil.isNotEmpty(idArr)) {
            int line = roleMapper.deleteBatch(idArr);
            if (line > 0) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMsg("删除成功");
            }
        }
        return model;
    }

    @Override
    public ResponseModel findData() {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMsg("请求成功");
        List<Role> list = roleMapper.queryList(null);
        if (MicrovanUtil.isNotEmpty(list)) {
            List<CusMap> data = new ArrayList<>();
            for (Role role : list) {
                CusMap map = new CusMap(role.getOid(), role.getRoleName());
                if (role.getOid().longValue() < 5) {
                    map.setDisabled(true);
                }
                data.add(map);
            }
            model.setData(data);
        }
        return model;
    }
}
