package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.ProjectRuleMapper;
import com.poho.stuup.dao.ProjectTypeMapper;
import com.poho.stuup.model.ProjectRule;
import com.poho.stuup.model.ProjectType;
import com.poho.stuup.service.IProjectRuleService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectRuleServiceImpl implements IProjectRuleService {

    @Resource
    private ProjectRuleMapper projectRuleMapper;
    @Resource
    private ProjectTypeMapper projectTypeMapper;

    @Override
    public ResponseModel findDataPageResult(String key, int page, int pageSize, Long typeId) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("typeId", typeId);
        int count = projectRuleMapper.queryTotal(map);
        PageData pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<ProjectRule> list = projectRuleMapper.queryList(map);
        if (MicrovanUtil.isNotEmpty(list)) {
            List<ProjectType> typeList = projectTypeMapper.queryAllList();
            if(!CollectionUtils.isEmpty(typeList)){
                Map<Long, String> proTypeMap = typeList.stream().collect(Collectors.toMap(ProjectType::getOid, ProjectType::getName));
                list.forEach( o -> o.setTypeName(proTypeMap.get(o.getTypeId())));
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
}
