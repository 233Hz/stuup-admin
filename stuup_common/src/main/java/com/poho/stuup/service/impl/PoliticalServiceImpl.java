package com.poho.stuup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ProjectRuleEnum;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.GradeMapper;
import com.poho.stuup.dao.PoliticalMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.Grade;
import com.poho.stuup.model.Political;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.dto.PoliticalDTO;
import com.poho.stuup.model.dto.PoliticalExcelDTO;
import com.poho.stuup.model.dto.PoliticalSearchDTO;
import com.poho.stuup.service.IPoliticalService;
import com.poho.stuup.service.IScoreService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PoliticalServiceImpl implements IPoliticalService {

    @Resource
    private PoliticalMapper politicalMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private GradeMapper gradeMapper;

    @Resource
    IScoreService scoreService;

    @Override
    public ResponseModel findDataPageResult(PoliticalSearchDTO searchDTO) {
        ResponseModel model = new ResponseModel();
        int count = politicalMapper.selectTotal(searchDTO);
        PageData pageData = new PageData(searchDTO.getCurrPage(), searchDTO.getPageSize(), count);
        List<PoliticalDTO> list = politicalMapper.selectList(pageData, searchDTO);
        if (MicrovanUtil.isNotEmpty(list)) {
            List<Grade> gradeList = gradeMapper.findGrades();
            Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(Grade::getOid,Grade::getGradeName));
            list = list.stream().map( o -> {
                o.setLevelName(ProjectUtil.POLITICAL_LEVEL_DICT_MAP.get(o.getLevel()));
                String durationDateStr = "";
                if(o.getStartDate() != null){
                    durationDateStr = MicrovanUtil.formatDateToStr(MicrovanUtil.DATE_FORMAT_PATTERN, o.getStartDate());
                }
                if(o.getEndDate() != null){
                    durationDateStr = durationDateStr + " - " + MicrovanUtil.formatDateToStr(MicrovanUtil.DATE_FORMAT_PATTERN, o.getEndDate());
                }
                if(o.getGradeId() != null){
                    o.setGradeName(gradeMap.get(o.getGradeId()));
                }
                o.setDurationDateStr(durationDateStr);
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
    public Map<String, Object> importList(List<PoliticalExcelDTO> list) {
        Map<String, Object> resultMap = new HashMap<>();
        int j = 0;
        int k = 0;
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            PoliticalExcelDTO dto = list.get(i);
            StringBuilder itemMsg = new StringBuilder();
            itemMsg.append("第").append(dto.getRowNum()).append("行：");
            //基础验证不空的字段
            boolean isVia = this.verifyBaseInfo(dto, itemMsg);
            //验证级别字典值
            Integer level = null;
            if(isVia){
                level = ProjectUtil.getDictKeyByValue(ProjectUtil.POLITICAL_LEVEL_DICT_MAP, dto.getLevel());
                if(level == null){
                    itemMsg.append("级别字段内容不合法；");
                    isVia = false;
                }
            }
            Date startDate = null;
            //验证开始时间是否符合
            if(isVia){
                startDate = MicrovanUtil.formatDate(MicrovanUtil.DATE_FORMAT_PATTERN, dto.getStartDate());
                if(startDate == null){
                    itemMsg.append("开始时间格式不正确必须是yyy-MM-dd；");
                    isVia = false;
                }
            }
            Date endDate = null;
            //验证结束时间是否符合
            if(isVia){
                endDate = MicrovanUtil.formatDate(MicrovanUtil.DATE_FORMAT_PATTERN, dto.getEndDate());
                if(endDate == null){
                    itemMsg.append("结束时间格式不正确必须是yyy-MM-dd；");
                    isVia = false;
                }
            }
            //验证学籍号存不存在
            Map<String, Object> param = new HashMap<>();
            param.put("studentNo", dto.getStuNo());
            Student stu = studentMapper.selectByStudentNo(param);
            if (stu == null) {
                itemMsg.append("学生不存，请检查学籍号是否正确；");
                isVia = false;
            }
            if (isVia) {
                Political political = new Political();
                political.setStuId(stu.getId());
                political.setLevel(level);
                political.setStartDate(startDate);
                political.setEndDate(endDate);
                this.convertExcelObjToEntity(dto, political);
                int line = politicalMapper.insertSelective(political);
                //TODO 以后优化移到同步流程
                scoreService.saveScoreDetail(ProjectRuleEnum.POLITICAL_RULE.getProjectRule().handler(political));
                if (line > 0) {
                    j++;
                } else {
                    msg.append("第").append(dto.getRowNum()).append("行，插入失败；");
                    k++;
                }
            } else {
                msg.append(itemMsg).append("。");
                k++;
            }
        }
        resultMap.put("okNum", j);
        resultMap.put("failNum", k);
        resultMap.put("msg", msg.toString());
        return resultMap;
    }

    private boolean verifyBaseInfo(PoliticalExcelDTO dto, StringBuilder itemMsg){
        //基础验证不空的字段
        boolean isVia = true;
        if (StrUtil.isBlank(dto.getStuNo())) {
            itemMsg.append("学籍号不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getName())) {
            itemMsg.append("党团活动不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getStartDate())) {
            itemMsg.append("开始时间不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getEndDate())) {
            itemMsg.append("结束时间不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getLevel())) {
            itemMsg.append("级别不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getRole())) {
            itemMsg.append("角色不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getOrgName())) {
            itemMsg.append("组织机构不能为空；");
            isVia = false;
        }
        return isVia;
    }

    private void convertExcelObjToEntity(PoliticalExcelDTO dto, Political political){
        political.setName(dto.getName());
        political.setRole(dto.getRole());
        political.setOrgName(dto.getOrgName());
    }

}
