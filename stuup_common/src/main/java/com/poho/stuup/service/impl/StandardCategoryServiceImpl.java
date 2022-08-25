package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.CusMap;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.custom.CusStandard;
import com.poho.stuup.dao.StandardCategoryMapper;
import com.poho.stuup.dao.StandardNormMapper;
import com.poho.stuup.model.StandardCategory;
import com.poho.stuup.model.StandardNorm;
import com.poho.stuup.service.IStandardCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 14:28 2020/9/18
 * @Modified By:
 */
@Service
public class StandardCategoryServiceImpl implements IStandardCategoryService {
    @Resource
    private StandardCategoryMapper standardCategoryMapper;
    @Resource
    private StandardNormMapper standardNormMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return standardCategoryMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(StandardCategory record) {
        return standardCategoryMapper.insert(record);
    }

    @Override
    public int insertSelective(StandardCategory record) {
        return standardCategoryMapper.insertSelective(record);
    }

    @Override
    public StandardCategory selectByPrimaryKey(Long oid) {
        return standardCategoryMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(StandardCategory record) {
        return standardCategoryMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(StandardCategory record) {
        return standardCategoryMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findDataPageResult(String key, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        int count = standardCategoryMapper.queryTotal(map);
        PageData<StandardCategory> pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<StandardCategory> list = standardCategoryMapper.queryList(map);
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
    public ResponseModel findData() {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        List<StandardCategory> list = standardCategoryMapper.queryList(null);
        if (MicrovanUtil.isNotEmpty(list)) {
            List<CusMap> data = new ArrayList<>();
            for (StandardCategory category : list) {
                CusMap map = new CusMap(category.getOid(), category.getCategoryName());
                data.add(map);
            }
            model.setData(data);
        }
        return model;
    }

    @Override
    public ResponseModel saveOrUpdate(StandardCategory category) {
        ResponseModel model = new ResponseModel();
        int line;
        Map<String, Object> param = new HashMap<>();
        param.put("categoryName", category.getCategoryName());
        if (MicrovanUtil.isNotEmpty(category.getOid())) {
            param.put("oid", category.getOid());
        }
        StandardCategory checkCategory = standardCategoryMapper.checkCategory(param);
        if (MicrovanUtil.isNotEmpty(checkCategory)) {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("考核类别已存在");
            return model;
        } else {
            if (MicrovanUtil.isNotEmpty(category.getOid())) {
                category.setCreateUser(null);
                line = standardCategoryMapper.updateByPrimaryKeySelective(category);
            } else {
                category.setCreateTime(new Date());
                line = standardCategoryMapper.insertSelective(category);
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
            int line = standardCategoryMapper.deleteBatch(idArr);
            if (line > 0) {
                //删除类别同时删除内容
                standardNormMapper.delBatchByCategory(idArr);
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("删除成功");
            }
        }
        return model;
    }

    @Override
    public ResponseModel findStandardData() {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        List<StandardCategory> list = standardCategoryMapper.queryList(null);
        List<CusStandard> cusStandards = new ArrayList<>();
        if (MicrovanUtil.isNotEmpty(list)) {
            for (StandardCategory category : list) {
                CusStandard cusStandard = new CusStandard();
                cusStandard.setCategoryId(category.getOid());
                cusStandard.setCategoryName(category.getCategoryName());
                cusStandard.setCategoryScore(category.getCategoryScore());
                Map<String, Object> map = new HashMap<>(1);
                map.put("categoryId", Long.valueOf(category.getOid()));
                List<StandardNorm> standardNorms = standardNormMapper.queryList(map);
                if (MicrovanUtil.isNotEmpty(standardNorms)) {
                    cusStandard.setStandardNorms(standardNorms);
                }
                cusStandards.add(cusStandard);
            }
        }
        model.setData(cusStandards);
        return model;
    }

    @Override
    public ResponseModel findAssessStandardData() {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        List<StandardCategory> list = standardCategoryMapper.queryList(null);
        List<CusStandard> cusStandards = new ArrayList<>();
        if (MicrovanUtil.isNotEmpty(list)) {
            for (StandardCategory category : list) {
                CusStandard cusStandard = new CusStandard();
                cusStandard.setCategoryId(category.getOid());
                cusStandard.setCategoryName(category.getCategoryName());
                cusStandard.setCategoryScore(category.getCategoryScore());
                Map<String, Object> map = new HashMap<>(1);
                map.put("categoryId", Long.valueOf(category.getOid()));
                List<StandardNorm> standardNorms = standardNormMapper.queryList(map);
                if (MicrovanUtil.isNotEmpty(standardNorms)) {
                    cusStandard.setStandardNorms(standardNorms);
                }
                cusStandards.add(cusStandard);
            }
            //合计总分占位
            CusStandard countStandard = new CusStandard(ProjectConstants.TEMP_COUNT_ID, "");
            List<StandardNorm> countNorms = new ArrayList<>();
            StandardNorm standardNorm = new StandardNorm();
            standardNorm.setOid(ProjectConstants.TEMP_COUNT_ID);
            standardNorm.setCategoryId(ProjectConstants.TEMP_COUNT_ID);
            standardNorm.setNormName("");
            standardNorm.setEvaluation("");
            countNorms.add(standardNorm);
            countStandard.setStandardNorms(countNorms);
            cusStandards.add(countStandard);
        }
        model.setData(cusStandards);
        return model;
    }
}
