package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.dto.SemesterDTO;
import com.poho.stuup.model.vo.SemesterVO;

import java.util.Date;

/**
 * <p>
 * 学期管理表 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-27
 */
public interface SemesterService extends IService<Semester> {

    IPage<SemesterVO> pageSemester(Page<SemesterVO> page, SemesterDTO query);

    /**
     * @description: 获取当前学期id
     * @param:
     * @return: java.lang.Long
     * @author BUNGA
     * @date: 2023/6/27 15:48
     */
    Long getCurrentSemesterId();

    /**
     * @description: 获取当前学期
     * @param:
     * @return: com.poho.stuup.model.Semester
     * @author BUNGA
     * @date: 2023/6/27 15:48
     */
    Semester getCurrentSemester();

    /**
     * @description: 查找该时间所属的学期
     * @param: date
     * @return: com.poho.stuup.model.Semester
     * @author BUNGA
     * @date: 2023/6/28 15:41
     */
    Semester findTimeBelongYear(Date date);
}
