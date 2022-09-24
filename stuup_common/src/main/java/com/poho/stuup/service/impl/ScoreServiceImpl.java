package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.GradeMapper;
import com.poho.stuup.dao.ScoreDetailMapper;
import com.poho.stuup.dao.ScoreMapper;
import com.poho.stuup.model.Grade;
import com.poho.stuup.model.dto.*;
import com.poho.stuup.service.IScoreService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScoreServiceImpl implements IScoreService {

    @Resource
    private ScoreMapper scoreMapper;

    @Resource
    private ScoreDetailMapper scoreDetailMapper;

    @Resource
    private GradeMapper gradeMapper;

    @Override
    public ResponseModel findScorePageResult(ScoreSearchDTO searchDTO) {
        ResponseModel model = new ResponseModel();
        int count = scoreMapper.selectTotal(searchDTO);
        PageData pageData = new PageData(searchDTO.getCurrPage(), searchDTO.getPageSize(), count);
        List<ScoreDTO> list = scoreMapper.selectList(pageData, searchDTO);
        if (MicrovanUtil.isNotEmpty(list)) {
            List<Grade> gradeList = gradeMapper.findGrades();
            Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(Grade::getOid,Grade::getGradeName));
            list = list.stream().map( o -> {
                if(o.getGradeId() != null){
                    o.setGradeName(gradeMap.get(o.getGradeId()));
                }
                return o;
            }).collect(Collectors.toList());
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
    public ResponseModel findScoreDetailPageResult(ScoreDetailSearchDTO searchDTO) {
        ResponseModel model = new ResponseModel();
        int count = scoreDetailMapper.selectTotal(searchDTO);
        PageData pageData = new PageData(searchDTO.getCurrPage(), searchDTO.getPageSize(), count);
        List<ScoreDetailDTO> list = scoreDetailMapper.selectList(pageData, searchDTO);
        if (MicrovanUtil.isNotEmpty(list)) {
            List<Grade> gradeList = gradeMapper.findGrades();
            Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(Grade::getOid,Grade::getGradeName));
            list = list.stream().map( o -> {
                o.setScoreTypeStr(ProjectUtil.SCORE_TYPE_DICT_MAP.get(o.getScoreType()));
                if(o.getCreateTime() != null){
                    o.setCreateTimeStr(MicrovanUtil.formatDateToStr(MicrovanUtil.DATE_TIME_FORMAT_PATTERN, o.getCreateTime()));
                }
                if(o.getGradeId() != null){
                    o.setGradeName(gradeMap.get(o.getGradeId()));
                }
                return o;
            }).collect(Collectors.toList());
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
