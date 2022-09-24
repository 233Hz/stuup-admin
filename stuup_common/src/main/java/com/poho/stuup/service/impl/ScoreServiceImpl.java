package com.poho.stuup.service.impl;

import cn.hutool.core.util.StrUtil;
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
                //TODO 处理花数量逻辑 先测试用
                o.setTomatoNum(3);
                o.setWintersweetNum(2);
                o.setDaisyNum(1);
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
    public ResponseModel getStuScoreTopList(StuScoreTopSearchDTO searchDTO){
        ResponseModel model = new ResponseModel();
        List<ScoreDTO> list = scoreMapper.selectStuScoreTopList(searchDTO);
        if (MicrovanUtil.isNotEmpty(list)) {
            List<Grade> gradeList = gradeMapper.findGrades();
            Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(Grade::getOid,Grade::getGradeName));
            list = list.stream().map( o -> {
                if(o.getGradeId() != null){
                    o.setGradeName(gradeMap.get(o.getGradeId()));
                }
                //TODO 处理花数量逻辑 先测试用
                o.setTomatoNum(3);
                o.setWintersweetNum(2);
                o.setDaisyNum(1);
                return o;
            }).collect(Collectors.toList());
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
        } else {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("无数据");
        }
        model.setData(list);
        return model;
    }

    @Override
    public  ResponseModel getStuScore(StuScoreSearchDTO searchDTO){
        ResponseModel model = new ResponseModel();
        if(searchDTO.getStuId() == null && StrUtil.isBlank(searchDTO.getStuNo())){
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("请求参数不能都为空");
            return model;
        }
        ScoreDTO scoreDTO = scoreMapper.selectStuScore(searchDTO);
        if (scoreDTO != null) {
            //TODO 处理花数量逻辑 先测试用
            scoreDTO.setTomatoNum(3);
            scoreDTO.setWintersweetNum(2);
            scoreDTO.setDaisyNum(1);
            if(scoreDTO.getGradeId() != null){
                List<Grade> gradeList = gradeMapper.findGrades();
                Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(Grade::getOid,Grade::getGradeName));
                scoreDTO.setGradeName(gradeMap.get(scoreDTO.getGradeId()));
            }
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
        } else {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("无数据");
        }
        model.setData(scoreDTO);
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

    @Override
    public ResponseModel getStuScoreDetailList(StuScoreSearchDTO searchDTO) {
        ResponseModel model = new ResponseModel();
        if(searchDTO.getStuId() == null && StrUtil.isBlank(searchDTO.getStuNo())){
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("请求参数不能都为空");
            return model;
        }
        List<ScoreDetailDTO> list = scoreDetailMapper.selectStuScoreDetailList(searchDTO);
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
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
        } else {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("无数据");
        }
        model.setData(list);
        return model;
    }
}
