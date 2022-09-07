package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.ConfigMapper;
import com.poho.stuup.dao.TermMapper;
import com.poho.stuup.model.Term;
import com.poho.stuup.service.ITermService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TermServiceImpl implements ITermService {

    private final static Logger logger = LoggerFactory.getLogger(TermServiceImpl.class);
    @Autowired
    private TermMapper termMapper;
    @Autowired
    private ConfigMapper configMapper;

    @Override
    public ResponseModel findDataPageResult(String key, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        int count = termMapper.findTotalTermByCond(map);
        PageData pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Term> list = termMapper.findTermPageResultByCond(map);
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

   /* @Override
    public AjaxResponse saveOrUpdateTerm(Term term) {
        AjaxResponse ajaxResponse = new AjaxResponse(false);
        Term checkTerm = termMapper.checkTerm(term);
        if (checkTerm != null) {
            ajaxResponse.setSuccess(false);
            ajaxResponse.setMessage("同一学期的序号不能重复");
        }
        else {
            int line = 0;
            if (term.getId() != null) {
                line = termMapper.updateByPrimaryKeySelective(term) ;
                TeachEvaUtil.handleAjaxResponse(ajaxResponse, line > 0, "更新成功", "更新失败，请稍后重试");
            } else {
                line = termMapper.insertSelective(term);
                TeachEvaUtil.handleAjaxResponse(ajaxResponse, line > 0, "添加成功", "添加失败，请稍后重试");
            }
        }
        return ajaxResponse;
    }

    @Override
    public Term selectByPrimaryKey(String id) {
        return termMapper.selectByPrimaryKey(Integer.valueOf(id));
    }

    @Override
    public List<Term> findTerms() {
        return termMapper.findTerms();
    }

    @Override
    public List<Term> findMaxPublishTerms(int num) {
        return termMapper.findMaxPublishTerms(num);
    }


    @Override
    public Term findCurrentTerm() {
        Config config = configMapper.findConfigByKey(CommonConstants.PARAM_TERM_ID);
        if (config != null) {
            if (MicrovanUtil.isNotEmpty(config.getValue())) {
                Term term = termMapper.selectByPrimaryKey(Integer.valueOf(config.getValue()));
                if (term != null) {
                    return term;
                }
            }
        }
        return null;
    }*/

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return termMapper.deleteByPrimaryKey(id);
    }

}
