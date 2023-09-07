package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.poho.stuup.dao.FileMapper;
import com.poho.stuup.dao.RecAddScoreMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.File;
import com.poho.stuup.model.vo.ProgressRankVO;
import com.poho.stuup.model.vo.ProgressTop10VO;
import com.poho.stuup.model.vo.WholeClassTop10VO;
import com.poho.stuup.model.vo.WholeSchoolTop10VO;
import com.poho.stuup.service.RankMonthService;
import com.poho.stuup.service.RankService;
import com.poho.stuup.util.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RankServiceImpl implements RankService {

    @Resource
    private YearMapper yearMapper;

    @Resource
    private RecAddScoreMapper recAddScoreMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private RankMonthService rankMonthService;


    @Override
    public List<WholeSchoolTop10VO> getWholeSchoolTop10Ranking() {
        Long yearId = yearMapper.getCurrentYearId();
        if (yearId == null) return new ArrayList<>();
        List<WholeSchoolTop10VO> list = recAddScoreMapper.findWholeSchoolTop10Ranking(yearId);
        if (CollUtil.isNotEmpty(list)) {
            List<Long> avatarIds = list.stream().map(WholeSchoolTop10VO::getAvatarId).collect(Collectors.toList());
            Map<Long, File> fileMap = new HashMap<>();
            if (CollUtil.isNotEmpty(avatarIds)) {
                List<File> files = fileMapper.selectBatchIds(avatarIds);
                avatarIds.clear();
                for (File file : files) {
                    fileMap.put(file.getId(), file);
                }
                files.clear();
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                WholeSchoolTop10VO wholeSchoolTop10 = list.get(i);
                if (MapUtil.isNotEmpty(fileMap)) {
                    Long avatarId = wholeSchoolTop10.getAvatarId();
                    File file = fileMap.get(avatarId);
                    if (file != null) {
                        String bucket = file.getBucket();
                        String storageName = file.getStorageName();
                        try {
                            String url = MinioUtils.getPreSignedObjectUrl(bucket, storageName);
                            wholeSchoolTop10.setAvatar(url);
                        } catch (Exception e) {
                            log.error(StrUtil.format("获取头像失败:fileId: {}", avatarId));
                        }
                    }
                }
                wholeSchoolTop10.setRanking(i + 1);
            }
        }
        return list;
    }

    @Override
    public List<WholeClassTop10VO> getWholeClassTop10Ranking() {
        Long yearId = yearMapper.getCurrentYearId();
        if (yearId == null) return new ArrayList<>();
        List<WholeClassTop10VO> list = recAddScoreMapper.findWholeClassTop10Ranking(yearId);
        if (CollUtil.isNotEmpty(list)) {
            List<Long> avatarIds = list.stream().map(WholeClassTop10VO::getAvatarId).collect(Collectors.toList());
            Map<Long, File> fileMap = new HashMap<>();
            if (CollUtil.isNotEmpty(avatarIds)) {
                List<File> files = fileMapper.selectBatchIds(avatarIds);
                avatarIds.clear();
                for (File file : files) {
                    fileMap.put(file.getId(), file);
                }
                files.clear();
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                WholeClassTop10VO wholeClassTop10VO = list.get(i);
                if (MapUtil.isNotEmpty(fileMap)) {
                    Long avatarId = wholeClassTop10VO.getAvatarId();
                    File file = fileMap.get(avatarId);
                    if (file != null) {
                        String bucket = file.getBucket();
                        String storageName = file.getStorageName();
                        try {
                            String url = MinioUtils.getPreSignedObjectUrl(bucket, storageName);
                            wholeClassTop10VO.setAvatar(url);
                        } catch (Exception e) {
                            log.error(StrUtil.format("获取头像失败:fileId: {}", avatarId));
                        }
                    }
                }
                wholeClassTop10VO.setRanking(i + 1);
            }
        }
        return list;
    }

    @Override
    public List<ProgressTop10VO> getProgressTop10Ranking() {
        List<ProgressRankVO> progressRank = rankMonthService.getProgressRank();
        int size = progressRank.size();
        List<Long> avatarIds = new ArrayList<>();
        List<ProgressTop10VO> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (i + 1 > 10) break;
            ProgressRankVO progressRankVO = progressRank.get(i);
            String studentName = progressRankVO.getStudentName();
            String className = progressRankVO.getClassName();
            String classTeacher = progressRankVO.getClassTeacher();
            Integer ranking = progressRankVO.getRank();
            BigDecimal score = progressRankVO.getScore();
            Integer riseRanking = progressRankVO.getRankChange();
            Long avatarId = progressRankVO.getAvatarId();
            ProgressTop10VO progressTop10VO = new ProgressTop10VO();
            progressTop10VO.setStudentName(studentName);
            progressTop10VO.setClassName(className);
            progressTop10VO.setClassTeacher(classTeacher);
            progressTop10VO.setRanking(ranking);
            progressTop10VO.setScore(score);
            progressTop10VO.setRiseRanking(riseRanking);
            if (avatarId != null) {
                progressTop10VO.setAvatarId(avatarId);
                avatarIds.add(avatarId);
            }
            result.add(progressTop10VO);
        }
        if (CollUtil.isNotEmpty(avatarIds)) {
            Map<Long, File> fileMap = new HashMap<>();
            List<File> files = fileMapper.selectBatchIds(avatarIds);
            avatarIds.clear();
            for (File file : files) {
                fileMap.put(file.getId(), file);
            }
            files.clear();
            for (ProgressTop10VO progressTop10VO : result) {
                Long avatarId = progressTop10VO.getAvatarId();
                File file = fileMap.get(avatarId);
                if (file != null) {
                    String bucket = file.getBucket();
                    String storageName = file.getStorageName();
                    try {
                        String url = MinioUtils.getPreSignedObjectUrl(bucket, storageName);
                        progressTop10VO.setAvatar(url);
                    } catch (Exception e) {
                        log.error(StrUtil.format("获取头像失败:fileId: {}", avatarId));
                    }
                }
            }
        }
        return result;
    }
}
