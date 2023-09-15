package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.vo.RecScoreVO;
import com.poho.stuup.service.RecDeductScoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 扣分记录 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-18
 */
@Service
public class RecDeductScoreServiceImpl extends ServiceImpl<RecDeductScoreMapper, RecDeductScore> implements RecDeductScoreService {

    @Resource
    private GrowthMapper growthMapper;

    @Resource
    private YearMapper yearMapper;

    @Resource
    private SemesterMapper semesterMapper;

    @Resource
    private GradeMapper gradeMapper;

    @Override
    public IPage<RecScoreVO> pageRecDeductScore(Page<RecScoreVO> page, RecScoreDTO query) {
        IPage<RecScoreVO> pageResult = baseMapper.pageRecDeductScore(page, query);
        List<RecScoreVO> pageRecords = pageResult.getRecords();
        if (pageRecords != null && !pageRecords.isEmpty()) {
            // 一二三级项目
            List<Growth> growths = growthMapper.selectList(Wrappers.<Growth>lambdaQuery()
                    .select(Growth::getId, Growth::getName));
            Map<Long, String> growthIdForNameMap = growths.stream().collect(Collectors.toMap(Growth::getId, Growth::getName));
            growths.clear();
            Set<Long> yearIdSet = new HashSet<>();
            Set<Long> semesterIdSet = new HashSet<>();
            Set<Integer> gradeIdSet = new HashSet<>();
            for (RecScoreVO record : pageRecords) {
                Optional.ofNullable(record.getYearId())
                        .ifPresent(yearIdSet::add);
                Optional.ofNullable(record.getSemesterId())
                        .ifPresent(semesterIdSet::add);
                Optional.ofNullable(record.getGradeId())
                        .ifPresent(gradeIdSet::add);
                Optional.ofNullable(record.getL1Id())
                        .map(growthIdForNameMap::get)
                        .ifPresent(record::setL1);
                Optional.ofNullable(record.getL2Id())
                        .map(growthIdForNameMap::get)
                        .ifPresent(record::setL2);
                Optional.ofNullable(record.getL3Id())
                        .map(growthIdForNameMap::get)
                        .ifPresent(record::setL3);
            }
            growthIdForNameMap.clear();
            // 年份
            Map<Long, String> yearIdForNameMap = new HashMap<>();
            Map<Long, String> semesterIdForNameMap = new HashMap<>();
            Map<Integer, String> gradeIdForNameMap = new HashMap<>();
            if (!yearIdSet.isEmpty()) {
                List<Year> yearList = yearMapper.getYearNameByIds(new ArrayList<>(yearIdSet));
                yearIdForNameMap = yearList.stream().collect(Collectors.toMap(Year::getOid, Year::getYearName));
                yearList.clear();
            }
            // 学期
            if (!semesterIdSet.isEmpty()) {
                List<Semester> semesterList = semesterMapper.getSemesterNameByIds(new ArrayList<>(semesterIdSet));
                semesterIdForNameMap = semesterList.stream().collect(Collectors.toMap(Semester::getId, Semester::getName));
                semesterList.clear();
            }
            // 年级
            if (!gradeIdSet.isEmpty()) {
                List<Grade> gradeList = gradeMapper.getGradeNameByIds(new ArrayList<>(gradeIdSet));
                gradeIdForNameMap = gradeList.stream().collect(Collectors.toMap(Grade::getOid, Grade::getGradeName));
                gradeList.clear();
            }
            for (RecScoreVO record : pageRecords) {
                Optional.ofNullable(record.getYearId())
                        .map(yearIdForNameMap::get)
                        .ifPresent(record::setYearName);
                Optional.ofNullable(record.getSemesterId())
                        .map(semesterIdForNameMap::get)
                        .ifPresent(record::setSemesterName);
                Optional.ofNullable(record.getGradeId())
                        .map(gradeIdForNameMap::get)
                        .ifPresent(record::setGradeName);
            }
            yearIdSet.clear();
            semesterIdSet.clear();
            gradeIdSet.clear();
        }
        return pageResult;
    }
}
