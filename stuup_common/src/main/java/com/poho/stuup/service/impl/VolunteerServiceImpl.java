package com.poho.stuup.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ProjectRuleEnum;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.VolunteerMapper;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.Volunteer;
import com.poho.stuup.model.dto.VolunteerDTO;
import com.poho.stuup.model.dto.VolunteerExcelDTO;
import com.poho.stuup.model.dto.VolunteerSearchDTO;
import com.poho.stuup.service.IScoreService;
import com.poho.stuup.service.IVolunteerService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VolunteerServiceImpl implements IVolunteerService {

    @Resource
    private VolunteerMapper volunteerMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    IScoreService scoreService;

    @Override
    public ResponseModel findDataPageResult(VolunteerSearchDTO searchDTO) {
        ResponseModel model = new ResponseModel();
        int count = volunteerMapper.selectTotal(searchDTO);
        PageData pageData = new PageData(searchDTO.getCurrPage(), searchDTO.getPageSize(), count);
        List<VolunteerDTO> list = volunteerMapper.selectList(pageData, searchDTO);
        if (MicrovanUtil.isNotEmpty(list)) {
            list = list.stream().map( o -> {
                o.setDurationName(String.valueOf(o.getDuration()));
                if(o.getOperDate() != null){
                    o.setOperDateStr(MicrovanUtil.formatDateToStr(MicrovanUtil.DATE_FORMAT_PATTERN, o.getOperDate()));
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
    public Map<String, Object> importList(List<VolunteerExcelDTO> list) {
        Map<String, Object> resultMap = new HashMap<>();
        int j = 0;
        int k = 0;
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            VolunteerExcelDTO dto = list.get(i);
            StringBuilder itemMsg = new StringBuilder();
            itemMsg.append("第").append(dto.getRowNum()).append("行：");
            //基础验证不空的字段
            boolean isVia = this.verifyBaseInfo(dto, itemMsg);
            //验证服务总时间长(学时)
            Integer duration = null;
            if(isVia && !NumberUtil.isInteger(dto.getDuration())){
                itemMsg.append("服务总时间长(学时)字段内容不合法；");
                isVia = false;
            } else {
                duration = Integer.parseInt(dto.getDuration());
            }
            Date date = null;
            //验证服务日期是否符合
            if(isVia){
                date = MicrovanUtil.formatDate(MicrovanUtil.DATE_FORMAT_PATTERN, dto.getOperDate());
                if(date == null){
                    itemMsg.append("服务日期格式不正确必须是yyy-MM-dd；");
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
                Volunteer volunteer = new Volunteer();
                volunteer.setStuId(stu.getId());
                volunteer.setDuration(duration);
                volunteer.setOperDate(date);
                this.convertExcelObjToEntity(dto, volunteer);
                int line = volunteerMapper.insertSelective(volunteer);
                //TODO 以后优化移到同步流程
                scoreService.saveScoreDetail(ProjectRuleEnum.VOLUNTEER_RULE.getProjectRule().handler(volunteer));
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

    private boolean verifyBaseInfo(VolunteerExcelDTO dto, StringBuilder itemMsg){
        //基础验证不空的字段
        boolean isVia = true;
        if (StrUtil.isBlank(dto.getStuNo())) {
            itemMsg.append("学籍号不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getName())) {
            itemMsg.append("基地/项目名称不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getAddress())) {
            itemMsg.append("基地/项目地址不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getSubName())) {
            itemMsg.append("子项目不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getPost())) {
            itemMsg.append("岗位不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getDuration())) {
            itemMsg.append("服务总时间长(学时)不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getOperDate())) {
            itemMsg.append("服务日期不能为空；");
            isVia = false;
        }
        return isVia;
    }

    private void convertExcelObjToEntity(VolunteerExcelDTO dto, Volunteer volunteer){
        volunteer.setName(dto.getName());
        volunteer.setAddress(dto.getAddress());
        volunteer.setSubName(dto.getSubName());
        volunteer.setPost(dto.getPost());
        if(StrUtil.isNotBlank(dto.getMemo())){
            volunteer.setMemo(dto.getMemo());
        }
    }
}
