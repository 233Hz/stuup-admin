package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.RecRoleEnum;
import com.poho.stuup.dao.RecSocietyMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecSociety;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.dto.RecSocietyDTO;
import com.poho.stuup.model.excel.RecSocietyExcel;
import com.poho.stuup.model.vo.RecSocietyVO;
import com.poho.stuup.service.RecScoreService;
import com.poho.stuup.service.RecSocietyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 参加社团记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecSocietyServiceImpl extends ServiceImpl<RecSocietyMapper, RecSociety> implements RecSocietyService {

    @Resource
    private YearMapper yearMapper;

    @Resource
    private RecScoreService recScoreService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecSocietyExcel(long batchCode, GrowthItem growthItem, List<RecSocietyExcel> excels, Map<String, Object> params) {
        Year year = yearMapper.findCurrYear();
        //=================保存数据=================
        List<RecSociety> recSocieties = excels.stream().map(excel -> {
            RecSociety recSociety = new RecSociety();
            recSociety.setYearId(year.getOid());
            recSociety.setGrowId(growthItem.getId());
            recSociety.setStudentId(excel.getStudentId());
            recSociety.setName(excel.getName());
            recSociety.setLevel(RecLevelEnum.getLabelValue(excel.getLevel()));
            recSociety.setStartTime(DateUtil.parseDate(excel.getStartTime()));
            recSociety.setEndTime(DateUtil.parseDate(excel.getEndTime()));
            recSociety.setRole(RecRoleEnum.getRoleValue(excel.getRole()));
            recSociety.setBatchCode(batchCode);
            return recSociety;
        }).collect(Collectors.toList());
        this.saveBatch(recSocieties);
        // 计算学生成长积分
        List<Long> studentIds = recSocieties.stream().map(RecSociety::getStudentId).collect(Collectors.toList());
        recScoreService.calculateScore(studentIds, growthItem);
    }

    @Override
    public IPage<RecSocietyVO> getRecSocietyPage(Page<RecSocietyVO> page, RecSocietyDTO query) {
        return baseMapper.getRecSocietyPage(page, query);
    }

}
