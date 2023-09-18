package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.CusMap;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.Year;
import com.poho.stuup.service.IYearService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author wupeng
 * @Description 学年管理接口实现类
 * @Date 2020-08-07 22:25
 * @return
 */
@Service
public class YearServiceImpl implements IYearService {
    @Resource
    private YearMapper yearMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return yearMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(Year record) {
        return yearMapper.insert(record);
    }

    @Override
    public int insertSelective(Year record) {
        return yearMapper.insertSelective(record);
    }

    @Override
    public Year selectByPrimaryKey(Long oid) {
        return yearMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(Year record) {
        return yearMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Year record) {
        return yearMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findDataPageResult(String yearName, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", yearName);
        int count = yearMapper.queryTotal(map);
        PageData<Year> pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Year> list = yearMapper.queryList(map);
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
    public ResponseModel saveOrUpdate(Year year) {
        ResponseModel model = new ResponseModel();
        int line;
        Map<String, Object> param = new HashMap<>();
        param.put("yearName", year.getYearName());
        if (MicrovanUtil.isNotEmpty(year.getOid())) {
            param.put("oid", year.getOid());
        }
        Year checkYear = yearMapper.checkYear(param);
        if (MicrovanUtil.isNotEmpty(checkYear)) {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMsg("学年已存在");
            return model;
        } else {
            if (MicrovanUtil.isNotEmpty(year.getOid())) {
                year.setCreateUser(null);
                line = yearMapper.updateByPrimaryKeySelective(year);
            } else {
                year.setCreateTime(new Date());
                line = yearMapper.insertSelective(year);
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
            int line = yearMapper.deleteBatch(idArr);
            if (line > 0) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMsg("删除成功");
            }
        }
        return model;
    }

    @Override
    public ResponseModel queryList() {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMsg("请求成功");
        List<Year> years = yearMapper.queryList(null);
        if (MicrovanUtil.isNotEmpty(years)) {
            List<CusMap> data = new ArrayList<>();
            for (Year year : years) {
                CusMap map = new CusMap(year.getOid(), year.getYearName());
                data.add(map);
            }
            model.setData(data);
        }
        return model;
    }

    @Override
    public ResponseModel updateCurrYear(Long oid) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMsg("设置失败，请稍后重试");
        Year year = yearMapper.selectByPrimaryKey(oid);
        if (MicrovanUtil.isNotEmpty(year)) {
            int line = yearMapper.updateSetCurrYear(oid);
            if (line > 0) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMsg("设置成功");
            }
        }
        return model;
    }

    @Override
    public ResponseModel findData(Long yearId) {
        ResponseModel model = new ResponseModel();
        Year year = yearMapper.selectByPrimaryKey(yearId);
        if (MicrovanUtil.isNotEmpty(year)) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMsg("请求成功");
            Map<String, Object> map = new HashMap<>();
            map.put("yearStart", MicrovanUtil.formatDateToStr("yyyy-MM-dd HH:mm", year.getYearStart()));
            map.put("yearEnd", MicrovanUtil.formatDateToStr("yyyy-MM-dd HH:mm", year.getYearEnd()));
            model.setData(map);
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMsg("请求失败，请稍后重试");
        }
        return model;
    }

    @Override
    public Year findYearForStartAndEndTime(Date startTime, Date endTime) {
        return yearMapper.findYearForStartAndEndTime(startTime, endTime);
    }

    @Override
    public Year findTimeBelongYear(Date date) {
        return yearMapper.findTimeBelongYear(date);
    }

    @Override
    public void setAllYearNotCurr() {
        yearMapper.setAllYearNotCurr();
    }

    @Override
    public Year getCurrentYear() {
        return yearMapper.getCurrentYear();
    }

    @Override
    public Long getCurrentYearId() {
        return yearMapper.getCurrentYearId();
    }

    @Override
    public void setCurrentYear(Long oid) {
        yearMapper.setCurrentYear(oid);
    }

}
