package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.StandardNormMapper;
import com.poho.stuup.model.StandardNorm;
import com.poho.stuup.service.IStandardNormService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 14:29 2020/9/18
 * @Modified By:
 */
@Service
public class StandardNormServiceImpl implements IStandardNormService {
    @Resource
    private StandardNormMapper standardNormMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return standardNormMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(StandardNorm record) {
        return standardNormMapper.insert(record);
    }

    @Override
    public int insertSelective(StandardNorm record) {
        return standardNormMapper.insertSelective(record);
    }

    @Override
    public StandardNorm selectByPrimaryKey(Long oid) {
        return standardNormMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(StandardNorm record) {
        return standardNormMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(StandardNorm record) {
        return standardNormMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findDataPageResult(String categoryId, String key, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        if (MicrovanUtil.isNotEmpty(categoryId)) {
            map.put("categoryId", Long.valueOf(categoryId));
        }
        int count = standardNormMapper.queryTotal(map);
        PageData<StandardNorm> pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<StandardNorm> list = standardNormMapper.queryList(map);
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
    public ResponseModel saveOrUpdate(StandardNorm standardNorm) {
        ResponseModel model = new ResponseModel();
        int line;
        Map<String, Object> param = new HashMap<>();
        param.put("categoryId", standardNorm.getCategoryId());
        param.put("normName", standardNorm.getNormName());
        if (MicrovanUtil.isNotEmpty(standardNorm.getOid())) {
            param.put("oid", standardNorm.getOid());
        }
        StandardNorm checkNorm = standardNormMapper.checkNorm(param);
        if (MicrovanUtil.isNotEmpty(checkNorm)) {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("考核内容已存在");
            return model;
        } else {
            if (MicrovanUtil.isNotEmpty(standardNorm.getOid())) {
                standardNorm.setCreateUser(null);
                line = standardNormMapper.updateByPrimaryKeySelective(standardNorm);
            } else {
                standardNorm.setCreateTime(new Date());
                line = standardNormMapper.insertSelective(standardNorm);
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
            int line = standardNormMapper.deleteBatch(idArr);
            if (line > 0) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("删除成功");
            }
        }
        return model;
    }
}
