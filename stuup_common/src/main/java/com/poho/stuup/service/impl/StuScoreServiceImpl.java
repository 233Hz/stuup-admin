package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.GardenTypeEnum;
import com.poho.stuup.constant.RoleEnum;
import com.poho.stuup.constant.UserTypeEnum;
import com.poho.stuup.dao.ClassMapper;
import com.poho.stuup.dao.StuScoreMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.model.StuScore;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.User;
import com.poho.stuup.model.dto.GrowGardenDTO;
import com.poho.stuup.model.vo.FlowerVO;
import com.poho.stuup.model.vo.GrowGardenVO;
import com.poho.stuup.service.StuScoreService;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

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
    private UserMapper userMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private ClassMapper classMapper;

    @Resource
    private GrowthItemServiceImpl growthItemService;


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
    public ResponseModel<IPage<GrowGardenVO>> pageGrowGarden(Page<GrowGardenVO> page, GrowGardenDTO query, Long userId) {
        int startScore, endScore;
        FlowerVO flowerConfig = growthItemService.getFlowerConfig();
        int gardenType = query.getGardenType();
        if (gardenType == GardenTypeEnum.BMH.getValue()) {
            startScore = flowerConfig.getBmhSeed();
            endScore = flowerConfig.getBmhFruit();
        } else if (gardenType == GardenTypeEnum.XCJ.getValue()) {
            startScore = flowerConfig.getXcjSeed();
            endScore = flowerConfig.getXcjFruit();
        } else if (gardenType == GardenTypeEnum.XHH.getValue()) {
            startScore = flowerConfig.getXhhSeed();
            endScore = flowerConfig.getXhhFruit();
        } else {
            return ResponseModel.failed("查询的花园类型不存在");
        }
        Integer userClassId;   // 用户所属班级id
        User user = userMapper.selectByPrimaryKey(userId);
        Integer userType = user.getUserType();
        if (userType == UserTypeEnum.STUDENT.getValue()) {
            Student student = studentMapper.getStudentForStudentNO(user.getLoginName());
            userClassId = student.getClassId();
        } else if (userType == UserTypeEnum.TEACHER.getValue()) {
            userClassId = classMapper.getClassIdForTeacher(userId);
        } else {
            log.error("用户类型不存在，用户id:{}", userId);
            return ResponseModel.failed("用户类型不存在,请联系管理员");
        }
        if (Utils.hasRole(userId, RoleEnum.ADMIN, RoleEnum.SCHOOL_LEADERS, RoleEnum.DEPT_LEADERS)) {
            userClassId = null;
        } else {
            if (userClassId == null) return ResponseModel.failed("没有查看权限");
        }
        return ResponseModel.ok(baseMapper.getGrowGardenForStudent(page, query, userClassId, startScore, endScore));
    }


}
