package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RoleEnum;
import com.poho.stuup.constant.UserTypeEnum;
import com.poho.stuup.dao.ClassMapper;
import com.poho.stuup.dao.StuScoreMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.TeacherMapper;
import com.poho.stuup.model.StuScore;
import com.poho.stuup.model.dto.GrowGardenDTO;
import com.poho.stuup.model.vo.GrowGardenVO;
import com.poho.stuup.service.FileService;
import com.poho.stuup.service.StuScoreService;
import com.poho.stuup.service.manager.FlowerModelManager;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 学生积分表 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Slf4j
@Service
public class StuScoreServiceImpl extends ServiceImpl<StuScoreMapper, StuScore> implements StuScoreService {

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private ClassMapper classMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private FileService fileService;

    @Resource
    private FlowerModelManager flowerModelManager;


    @Override
    public void updateTotalScore(Long studentId, BigDecimal score) {
        StuScore stuScore = this.getOne(Wrappers.<StuScore>lambdaQuery().eq(StuScore::getStudentId, studentId));
        if (stuScore != null) {
            BigDecimal totalScore = stuScore.getScore();
            totalScore = totalScore.add(score);
            this.update(Wrappers.<StuScore>lambdaUpdate()
                    .set(StuScore::getScore, totalScore)
                    .eq(StuScore::getStudentId, studentId));
        } else {
            // 不存在则创建
            StuScore saveStuScore = new StuScore();
            saveStuScore.setStudentId(studentId);
            saveStuScore.setScore(score);
            this.save(saveStuScore);
        }
    }

    @Override
    public IPage<GrowGardenVO> pageGrowGarden(Page<GrowGardenVO> page, GrowGardenDTO query) {
        Long userId = query.getUser().getOid();
        boolean hasRolePermission = Utils.hasRole(userId, RoleEnum.ADMIN, RoleEnum.SCHOOL_LEADERS, RoleEnum.DEPT_LEADERS);
        if (!hasRolePermission) {
            Integer userType = query.getUser().getUserType();
            String loginName = query.getUser().getLoginName();
            if (userType == UserTypeEnum.STUDENT.getValue()) {
                Integer classId = studentMapper.getClassIdByStudentNo(loginName);
                query.setClassIds(Collections.singletonList(classId));
            } else if (userType == UserTypeEnum.TEACHER.getValue()) {
                Integer teacherId = teacherMapper.getIdByJobNo(loginName);
                if (teacherId == null) return page;
                List<Integer> classIds = classMapper.getClassIdFormTeacherId(teacherId);
                if (CollUtil.isEmpty(classIds)) return page;
                query.setClassIds(classIds);
            } else {
                return page;
            }
        }
        IPage<GrowGardenVO> resultPage = baseMapper.pageGrowGarden(page, query);
        List<GrowGardenVO> pageRecords = resultPage.getRecords();
        for (GrowGardenVO record : pageRecords) {
            Long avatarId = record.getAvatarId();
            try {
                record.setAvatar(fileService.getFileUrl(avatarId));
            } catch (Exception e) {
                log.error(e.getMessage(), (Object) e.getStackTrace());
            }
        }
        return resultPage;
    }


}
