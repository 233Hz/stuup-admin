package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.RecDefaultMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.dto.GrowSearchDTO;
import com.poho.stuup.model.excel.RecDefaultExcel;
import com.poho.stuup.model.vo.GrowRecordVO;
import com.poho.stuup.service.GrowthService;
import com.poho.stuup.service.RecDefaultService;
import com.poho.stuup.service.RecScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 默认积分记录表（除综评表） 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-26
 */
@Service
public class RecDefaultServiceImpl extends ServiceImpl<RecDefaultMapper, RecDefault> implements RecDefaultService {

    @Resource
    private GrowthService growthService;

    @Resource
    private RecScoreService recScoreService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecDefault(long batchCode, GrowthItem growthItem, List<RecDefaultExcel> excels, Map<String, Object> params) {
        String userId = (String) params.get("userId");
        // 保存导入记录
        List<RecDefault> recDefaults = excels.stream().map(excel -> {
            RecDefault recDefault = new RecDefault();
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setGrowId(growthItem.getId());
            recDefault.setRemark(excel.getRemark());
            recDefault.setCreateUser(Long.valueOf(userId));
            recDefault.setBatchCode(batchCode);
            return recDefault;
        }).collect(Collectors.toList());
        this.saveBatch(recDefaults);
        // 计算学生成长积分
        List<Long> studentIds = recDefaults.stream().map(RecDefault::getStudentId).collect(Collectors.toList());
        recScoreService.calculateScore(studentIds, growthItem, params);
    }

    @Override
    public IPage<GrowRecordVO> growthRecordPage(Page page, GrowSearchDTO query) {
        return baseMapper.growthRecordPage(page, query);
    }
}
