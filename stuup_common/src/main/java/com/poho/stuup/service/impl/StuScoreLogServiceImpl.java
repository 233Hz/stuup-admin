package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.dao.StuScoreLogMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.model.StuScoreLog;
import com.poho.stuup.model.User;
import com.poho.stuup.model.vo.StudentRecScoreVO;
import com.poho.stuup.service.StuScoreLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Override
    public ResponseModel<IPage<StudentRecScoreVO>> pageStudentRecScore(Page<StudentRecScoreVO> page, Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) return ResponseModel.failed("未查询到您的用户信息，请联系管理员");
        String loginName = user.getLoginName();
        Long studentId = studentMapper.findStudentId(loginName);
        if (studentId == null) return ResponseModel.failed("未查询到您的学生信息，请联系管理员");
        return ResponseModel.ok(baseMapper.pageStudentRecScore(page, studentId));
    }
}
