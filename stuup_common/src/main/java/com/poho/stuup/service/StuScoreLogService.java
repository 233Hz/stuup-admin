package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.StuScoreLog;
import com.poho.stuup.model.vo.StudentRecScoreVO;

/**
 * <p>
 * 学生积分日志 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-27
 */
public interface StuScoreLogService extends IService<StuScoreLog> {

    ResponseModel<IPage<StudentRecScoreVO>> pageStudentRecScore(Page<StudentRecScoreVO> page, Long userId);

}
