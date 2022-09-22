package com.poho.stuup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.RewardMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.Reward;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.dto.RewardExcelDTO;
import com.poho.stuup.service.IRewardService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RewardServiceImpl implements IRewardService {

    @Resource
    private RewardMapper rewardMapper;

    @Resource
    private StudentMapper studentMapper;

    @Override
    public ResponseModel findDataPageResult(String name, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        int count = rewardMapper.queryTotal(map);
        PageData pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Reward> list = rewardMapper.queryList(map);
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
    public Map<String, Object> importList(List<RewardExcelDTO> list) {
        Map<String, Object> resultMap = new HashMap<>();
        int j = 0;
        int k = 0;
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            RewardExcelDTO dto = list.get(i);
            StringBuilder itemMsg = new StringBuilder();
            itemMsg.append("第").append(dto.getRowNum()).append("行：");
            //基础验证不空的字段
            boolean isVia = this.verifyBaseInfo(dto, itemMsg);
            //验证级别字典值
            Integer level = null;
            if(isVia){
                level = ProjectUtil.getDictKeyByValue(ProjectUtil.LEVEL_DICT_MAP, dto.getLevel());
                if(level == null){
                    itemMsg.append("奖励级别字段内容不合法；");
                    isVia = false;
                }
            }
            Date date = null;
            //验证获奖时间是否符合
            if(isVia){
                date = MicrovanUtil.formatDate(MicrovanUtil.DATE_FORMAT_PATTERN, dto.getObtainDate());
                if(date == null){
                    itemMsg.append("获奖时间格式不正确必须是yyy-MM-dd；");
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
                Reward reward = new Reward();
                reward.setStuId(stu.getId());
                reward.setLevel(level);
                reward.setObtainDate(date);
                this.convertExcelObjToEntity(dto, reward);
                int line = rewardMapper.insertSelective(reward);
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

    private boolean verifyBaseInfo(RewardExcelDTO dto, StringBuilder itemMsg){
        //基础验证不空的字段
        boolean isVia = true;
        if (StrUtil.isBlank(dto.getStuNo())) {
            itemMsg.append("学籍号不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getName())) {
            itemMsg.append("奖励名称不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getLevel())) {
            itemMsg.append("奖励级别不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getObtainDate())) {
            itemMsg.append("获奖时间不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getUnitName())) {
            itemMsg.append("颁奖单位不能为空；");
            isVia = false;
        }

        if (isVia && MicrovanUtil.isEmpty(dto.getRank())) {
            itemMsg.append("名次或等第不能为空；");
            isVia = false;
        }
        return isVia;
    }

    private void convertExcelObjToEntity(RewardExcelDTO dto, Reward reward){
        reward.setName(dto.getName());
        reward.setUnitName(dto.getUnitName());
        reward.setRank(dto.getRank());
    }
}
