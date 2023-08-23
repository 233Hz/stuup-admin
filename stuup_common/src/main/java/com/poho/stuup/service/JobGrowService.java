package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.model.JobGrow;

import java.util.Date;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-18
 */
public interface JobGrowService extends IService<JobGrow> {

    void executeGrowJob(PeriodEnum periodEnum, Date date);

    void executeGrowJobCompensation();
}
