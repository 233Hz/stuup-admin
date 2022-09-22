package com.poho.stuup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.ContestMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.Contest;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.dto.ContestDTO;
import com.poho.stuup.model.dto.ContestExcelDTO;
import com.poho.stuup.model.dto.ContestSearchDTO;
import com.poho.stuup.model.dto.RewardDTO;
import com.poho.stuup.service.IContestService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContestServiceImpl implements IContestService {

    @Resource
    private ContestMapper contestMapper;

    @Resource
    private StudentMapper studentMapper;

    @Override
    public ResponseModel findDataPageResult(ContestSearchDTO searchDTO) {
        ResponseModel model = new ResponseModel();
        int count = contestMapper.selectTotal(searchDTO);
        PageData pageData = new PageData(searchDTO.getCurrPage(), searchDTO.getPageSize(), count);
        List<ContestDTO> list = contestMapper.selectList(pageData, searchDTO);
        if (MicrovanUtil.isNotEmpty(list)) {
            list = list.stream().map( o -> {
                o.setLevelName(ProjectUtil.LEVEL_DICT_MAP.get(o.getLevel()));
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
    public Map<String, Object> importList(List<ContestExcelDTO> list) {
        Map<String, Object> resultMap = new HashMap<>();
        int j = 0;
        int k = 0;
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            ContestExcelDTO dto = list.get(i);
            StringBuilder itemMsg = new StringBuilder();
            itemMsg.append("第").append(dto.getRowNum()).append("行：");
            //基础验证不空的字段
            boolean isVia = this.verifyBaseInfo(dto, itemMsg);
            //验证级别字典值
            Integer level = null;
            if(isVia){
                level = ProjectUtil.getDictKeyByValue(ProjectUtil.LEVEL_DICT_MAP, dto.getLevel());
                if(level == null){
                    itemMsg.append("级别字段内容不合法；");
                    isVia = false;
                }
            }
            Date date = null;
            //验证获奖日期是否符合
            if(isVia){
                date = MicrovanUtil.formatDate(MicrovanUtil.DATE_FORMAT_PATTERN, dto.getObtainDate());
                if(date == null){
                    itemMsg.append("获奖日期格式不正确必须是yyy-MM-dd；");
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
                Contest contest = new Contest();
                contest.setStuId(stu.getId());
                contest.setLevel(level);
                contest.setObtainDate(date);
                this.convertExcelObjToEntity(dto, contest);
                int line = contestMapper.insertSelective(contest);
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

    private boolean verifyBaseInfo(ContestExcelDTO dto, StringBuilder itemMsg){
        //基础验证不空的字段
        boolean isVia = true;
        if (StrUtil.isBlank(dto.getStuNo())) {
            itemMsg.append("学籍号不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getName())) {
            itemMsg.append("项目名称不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getUnitName())) {
            itemMsg.append("主办单位不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getLevel())) {
            itemMsg.append("级别不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getObtainDate())) {
            itemMsg.append("获奖日期不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getRank())) {
            itemMsg.append("获奖等第不能为空；");
            isVia = false;
        }
        return isVia;
    }

    private void convertExcelObjToEntity(ContestExcelDTO dto, Contest contest){
        contest.setName(dto.getName());
        contest.setUnitName(dto.getUnitName());
        contest.setRank(dto.getRank());
    }
}
