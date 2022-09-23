package com.poho.stuup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.CertificateMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.Certificate;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.dto.CertificateDTO;
import com.poho.stuup.model.dto.CertificateExcelDTO;
import com.poho.stuup.model.dto.CertificateSearchDTO;
import com.poho.stuup.service.ICertificateService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements ICertificateService {

    @Resource
    private CertificateMapper certificateMapper;

    @Resource
    private StudentMapper studentMapper;

    @Override
    public ResponseModel findDataPageResult(CertificateSearchDTO searchDTO) {
        ResponseModel model = new ResponseModel();
        int count = certificateMapper.selectTotal(searchDTO);
        PageData pageData = new PageData(searchDTO.getCurrPage(), searchDTO.getPageSize(), count);
        List<CertificateDTO> list = certificateMapper.selectList(pageData, searchDTO);
        if (MicrovanUtil.isNotEmpty(list)) {
            list = list.stream().map( o -> {
                o.setLevelName(ProjectUtil.LEVEL_DICT_MAP.get(o.getLevel()));
                if(o.getObtainDate() != null){
                    o.setObtainDateStr(MicrovanUtil.formatDateToStr(MicrovanUtil.DATE_FORMAT_PATTERN, o.getObtainDate()));
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
    public Map<String, Object> importList(List<CertificateExcelDTO> list) {
        Map<String, Object> resultMap = new HashMap<>();
        int j = 0;
        int k = 0;
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            CertificateExcelDTO dto = list.get(i);
            StringBuilder itemMsg = new StringBuilder();
            itemMsg.append("第").append(dto.getRowNum()).append("行：");
            //基础验证不空的字段
            boolean isVia = this.verifyBaseInfo(dto, itemMsg);
            //验证级别字典值
            Integer level = null;
            if(isVia){
                level = ProjectUtil.getDictKeyByValue(ProjectUtil.LEVEL_DICT_MAP, dto.getLevel());
                if(level == null){
                    itemMsg.append("级别不合法；");
                    isVia = false;
                }
            }
            Date date = null;
            //验证颁证日期是否符合
            if(isVia){
                date = MicrovanUtil.formatDate(MicrovanUtil.DATE_FORMAT_PATTERN, dto.getObtainDate());
                if(date == null){
                    itemMsg.append("颁证日期格式不正确必须是yyy-MM-dd；");
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
                Certificate certificate = new Certificate();
                certificate.setStuId(stu.getId());
                certificate.setLevel(level);
                certificate.setObtainDate(date);
                this.convertExcelObjToEntity(dto, certificate);
                int line = certificateMapper.insertSelective(certificate);
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

    private boolean verifyBaseInfo(CertificateExcelDTO dto, StringBuilder itemMsg){
        //基础验证不空的字段
        boolean isVia = true;
        if (StrUtil.isBlank(dto.getStuNo())) {
            itemMsg.append("学籍号不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getName())) {
            itemMsg.append("证书名称不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getMajor())) {
            itemMsg.append("专业大类不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getUnitName())) {
            itemMsg.append("办证单位不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getLevel())) {
            itemMsg.append("级别不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getObtainDate())) {
            itemMsg.append("颁证日期不能为空；");
            isVia = false;
        }
        if (isVia && MicrovanUtil.isEmpty(dto.getCertNo())) {
            itemMsg.append("证书编号不能为空；");
            isVia = false;
        }
        return isVia;
    }

    private void convertExcelObjToEntity(CertificateExcelDTO dto, Certificate certificate){
        certificate.setName(dto.getName());
        certificate.setMajor(dto.getMajor());
        certificate.setUnitName(dto.getUnitName());
        certificate.setCertNo(dto.getCertNo());
    }
}
