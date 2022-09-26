package com.poho.stuup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ModelRuleEnum;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.GradeMapper;
import com.poho.stuup.dao.ScoreDetailMapper;
import com.poho.stuup.dao.ScoreMapper;
import com.poho.stuup.model.Grade;
import com.poho.stuup.model.ScoreDetail;
import com.poho.stuup.model.dto.*;
import com.poho.stuup.service.IScoreService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                this.handlerModelRuleToScoreDTO(o);
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
                this.handlerModelRuleToScoreDTO(o);
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
            this.handlerModelRuleToScoreDTO(scoreDTO);
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

    @Transactional
    @Override
    public boolean saveScoreDetail(ScoreDetail scoreDetail){
        int num = scoreDetailMapper.insertSelective(scoreDetail);
        if(num > 0){
            int score = scoreDetail.getScore();
            this.scoreMapper.updateScoreByStuId(scoreDetail.getStuId(), score);
        }
        return true;
    }
    //处理模型规则转换逻辑
    private void handlerModelRuleToScoreDTO (ScoreDTO scoreDTO){
        int totalScore = scoreDTO.getTotalScore();
        int num = totalScore/ModelRuleEnum.TOMATO.getSeedScore();
        if(num >= 1){
            totalScore = totalScore - ModelRuleEnum.TOMATO.getSeedScore()*num;
        }
        scoreDTO.setTomatoNum(num);

        num = totalScore/ModelRuleEnum.WINTERSWEET.getSeedScore();
        if(num >= 1){
            totalScore = totalScore - ModelRuleEnum.WINTERSWEET.getSeedScore()*num;
        }
        scoreDTO.setWintersweetNum(num);

        num = totalScore/ModelRuleEnum.DAISY.getSeedScore();
        scoreDTO.setDaisyNum(num);
    }
}
