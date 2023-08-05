package com.poho.stuup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.dao.StuScoreLogMapper;
import com.poho.stuup.dao.StuScoreMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.model.StuScore;
import com.poho.stuup.model.StuScoreLog;
import com.poho.stuup.model.User;
import com.poho.stuup.model.vo.StudentRecScoreVO;
import com.poho.stuup.model.vo.StudentScoreDetailsVO;
import com.poho.stuup.service.StuScoreLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 学生积分日志 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-27
 */
@Service
public class StuScoreLogServiceImpl extends ServiceImpl<StuScoreLogMapper, StuScoreLog> implements StuScoreLogService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private StuScoreMapper stuScoreMapper;

    @Override
    public ResponseModel<StudentScoreDetailsVO> pageStudentRecScore(Page<StudentRecScoreVO> page, Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) return ResponseModel.failed("未查询到您的用户信息，请联系管理员");
        String loginName = user.getLoginName();
        Long studentId = studentMapper.findStudentId(loginName);
        if (studentId == null) return ResponseModel.failed("未查询到您的学生信息，请联系管理员");
        StudentScoreDetailsVO studentScoreDetailsVO = new StudentScoreDetailsVO();
        StuScore stuScore = stuScoreMapper.selectOne(Wrappers.<StuScore>lambdaQuery()
                .select(StuScore::getScore)
                .eq(StuScore::getStudentId, studentId));
        if (stuScore == null) {
            studentScoreDetailsVO.setTotalScore(BigDecimal.ZERO);
        } else {
            studentScoreDetailsVO.setTotalScore(stuScore.getScore());
        }
        IPage<StudentRecScoreVO> iPage = baseMapper.pageStudentRecScore(page, studentId);
        List<StudentRecScoreVO> records = iPage.getRecords();
        int size = records.size();
        for (int i = 0; i < size; i++) {
            StudentRecScoreVO studentRecScoreVO = records.get(i);
            String firstName = studentRecScoreVO.getFirstName();
            String secondName = studentRecScoreVO.getSecondName();
            String thirdName = studentRecScoreVO.getThirdName();
            String growName = studentRecScoreVO.getGrowName();
            String name = firstName;
            if (StrUtil.isNotBlank(secondName)) name += StrUtil.format("-{}", secondName);
            if (StrUtil.isNotBlank(thirdName)) name += StrUtil.format("-{}", thirdName);
            if (StrUtil.isNotBlank(growName)) name += StrUtil.format("-{}", growName);
            studentRecScoreVO.setName(name);
        }
        studentScoreDetailsVO.setRecords(records);
        return ResponseModel.ok(studentScoreDetailsVO);
    }
}
