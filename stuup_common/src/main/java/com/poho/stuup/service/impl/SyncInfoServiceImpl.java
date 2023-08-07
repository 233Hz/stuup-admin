package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.constant.SyncInfoStateEnum;
import com.poho.stuup.dao.SemesterMapper;
import com.poho.stuup.dao.SyncInfoMapper;
import com.poho.stuup.event.CommunityMemberEvent;
import com.poho.stuup.model.SyncCommunityMember;
import com.poho.stuup.model.SyncInfo;
import com.poho.stuup.model.dto.CommunityMemberDTO;
import com.poho.stuup.service.SyncCommunityMemberService;
import com.poho.stuup.service.SyncInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class SyncInfoServiceImpl extends ServiceImpl<SyncInfoMapper, SyncInfo> implements SyncInfoService {


    private final SyncCommunityMemberService syncCommunityMemberService;

    private final SemesterMapper semesterMapper;

    private final ApplicationContext applicationContext;

    @Override
    public ResponseModel<Integer> getRemoteOpenCommunityTotal(String url){
        ResponseModel<Integer> responseModel;
        Map<String, Object> paramMap = new HashMap<>();
        final String uri = "openApi/community/openTotal.htm"; //社团系统获取开放社团总数
        url = url + uri;
        String jsonStr = "";
        try {
            jsonStr = HttpUtil.post(url, paramMap);
            JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
            if(jsonObject.get("success") != null && !jsonObject.getBool("success")){ //接口返回失败
                responseModel = ResponseModel.failed(jsonObject.getStr("msg"));
                log.error(StrUtil.format("调用远程接口:{} 失败原因 :{}", url, JSONUtil.toJsonStr(responseModel)));
            } else { //返回数据列表
                responseModel = ResponseModel.ok();
                int total = jsonObject.getInt("obj");
                responseModel.setData(total);
                log.info(StrUtil.format("调用远程接口:{} 返回total值 :{}", url, total));
            }
        } catch (Exception e) {
            log.error(StrUtil.format("url:{}  返回jsonStr:{} 异常：{}", url, jsonStr, e.getMessage()));
            responseModel = ResponseModel.failed(e.getMessage());
            e.printStackTrace();
        }
        return responseModel;
    }

    @Override
    public ResponseModel syncCommunityMember(String url){
        String termName = semesterMapper.getCurrTermName();
        if(StrUtil.isBlank(termName)){
            return ResponseModel.failed("学期名称没有取到，请查看有没有设置当前学期");
        }
        //适配学期名称转化为社团系统学期名称格式
        // 2022学年上学期  --> 2022-2023学年第一学期
        String year = termName.substring(0,4);
        termName = StrUtil.format("{}-{}学年第{}学期", year, Integer.parseInt(year)+1
                , termName.contains("上") ? "一" : "二");
        log.info(StrUtil.format("syncCommunityMember termName:{}",  termName));

        //检查同步表中同步表有已经同步记录
        SyncInfo syncInfo = this.getOne(Wrappers.<SyncInfo>lambdaQuery()
                .eq(SyncInfo::getBusinessCode, ProjectConstants.SYNC_INFO_BUSINESS_CODE_COMMUNITY)
                .eq(SyncInfo::getBusinessKey, termName));
        if(syncInfo != null){
            //发送处理社团记录，调用学生积分功能模块，用于补偿执行之前整个同步流程可能中断的情况
            applicationContext.publishEvent(new CommunityMemberEvent(syncInfo.getId()));

            int state = syncInfo.getState().intValue();
            if(SyncInfoStateEnum.SYNC_IN.getValue() ==  state){
                Long memberNum = syncCommunityMemberService.count(Wrappers.<SyncCommunityMember>lambdaQuery()
                        .eq(SyncCommunityMember::getSyncInfoId, syncInfo.getId()));
                if(memberNum != null && memberNum > 0){
                    return ResponseModel.failed(StrUtil.format("{} 正在同步中, 如果已经同步很长时间，请联系管理员查看是否有异常情况", termName));
                }
            }else if(SyncInfoStateEnum.SYNC_SUCCESS.getValue() == state){
                return ResponseModel.failed(StrUtil.format("{} 数据已同步", termName));
            } else if(SyncInfoStateEnum.SYNC_FAIL.getValue() == state) {
                syncInfo.setMemo("");
                syncInfo.setUpdateTime(null);
                syncInfo.setState(SyncInfoStateEnum.SYNC_IN.getValue()); //设置为同步中，重新同步
                this.updateById(syncInfo);
            }
        } else {
            syncInfo = new SyncInfo(ProjectConstants.SYNC_INFO_BUSINESS_CODE_COMMUNITY, "社团成员同步", termName);
            this.save(syncInfo);
        }
        //调用外部系统接口，获取社团成员数据
        ResponseModel<List<CommunityMemberDTO>> responseModel = this.getRemoteCommunityMember(url, termName);
        if(CommonConstants.CODE_FAIL == responseModel.getCode()){ //调用接口失败
           return responseModel;
        }
        List<CommunityMemberDTO> list = responseModel.getData();
        if(CollUtil.isEmpty(list)){
            return ResponseModel.ok("同步成功： 同步总记录数：0");
        }
        //保存同步过来的社团成员信息
        Integer infoId = syncInfo.getId();
        ResponseModel res = batchSaveMembers(infoId, list);
        if(CommonConstants.CODE_FAIL == res.getCode()){ //插入记录失败
            //更新同步记录状态
            syncInfo.setMemo(StrUtil.format("插入记录失败： {}", res.getMessage()));
            syncInfo.setState(SyncInfoStateEnum.SYNC_FAIL.getValue());
            this.updateById(syncInfo);
            return ResponseModel.failed(StrUtil.format("同步失败 插入记录异常：{}", res.getMessage()));
        }
        //更新同步记录状态
        syncInfo.setUpdateTime(null);
        syncInfo.setMemo(StrUtil.format("同步成功 同步总记录数:{}", list.size()));
        syncInfo.setState(SyncInfoStateEnum.SYNC_SUCCESS.getValue());
        this.updateById(syncInfo);
        //发送处理社团记录,调用学生积分功能模块
        applicationContext.publishEvent(new CommunityMemberEvent(infoId));
        return ResponseModel.ok(StrUtil.format("同步成功 同步总记录数：{}", list.size()));
    }

    private ResponseModel<List<CommunityMemberDTO>> getRemoteCommunityMember(String url, String termName){
        ResponseModel<List<CommunityMemberDTO>> responseModel;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("termName", termName);
        final String uri = "openApi/community/member.htm"; //社团系统获取社团成员接口地址
        url = url + uri;
        String jsonStr = "";
        try {
             jsonStr = HttpUtil.post(url, paramMap);
            JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
            if(jsonObject.get("success") != null && !jsonObject.getBool("success")){ //接口返回失败
                responseModel = ResponseModel.failed(jsonObject.getStr("msg"));
                log.error(StrUtil.format("调用远程接口:{} 失败原因 :{}", url, JSONUtil.toJsonStr(responseModel)));
            } else { //返回数据列表
                    responseModel = ResponseModel.ok();
                    JSONArray jsonArray = jsonObject.getJSONArray("obj");
                    if(jsonArray != null && jsonArray.size() > 0 ){
                        List<CommunityMemberDTO> list = jsonObject.getBeanList("obj", CommunityMemberDTO.class);
                        responseModel.setData(list);
                    }
                    int total = 0;
                    if(responseModel.getData() != null){
                        total = responseModel.getData().size();
                    }
                log.info(StrUtil.format("调用远程接口:{} 返回total值 :{}", url, total));
            }

        } catch (Exception e) {
            log.error(StrUtil.format("url:{} termName:{} 返回jsonStr:{} 异常：{}", url, termName, jsonStr, e.getMessage()));
            responseModel = ResponseModel.failed(e.getMessage());
            e.printStackTrace();
        }
        return responseModel;
    }
    private ResponseModel batchSaveMembers(Integer infoId, List<CommunityMemberDTO> list){
        List<SyncCommunityMember> communityMemberList = new ArrayList<>();
        ResponseModel responseModel;
        try {
            for (CommunityMemberDTO memberDTO : list) {
                SyncCommunityMember syncCommunityMember = SyncCommunityMember.builder()
                        .syncInfoId(infoId)
                        .memberId(memberDTO.getMemberId())
                        .termName(memberDTO.getTermName())
                        .communityName(memberDTO.getCommunityName())
                        .stuNo(memberDTO.getStuNo())
                        .stuName(memberDTO.getStuName())
                        .build();
                communityMemberList.add(syncCommunityMember) ;
            }
            syncCommunityMemberService.saveBatch(communityMemberList);
            responseModel = ResponseModel.ok();
        } catch (Exception e ) {
            log.error(StrUtil.format("infoId:{} 异常：{}", infoId, e.getMessage()));
            responseModel = ResponseModel.failed(e.getMessage());
            e.printStackTrace();
        }
        return responseModel;

    }


}
