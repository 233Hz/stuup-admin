package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.model.RecProject;
import com.poho.stuup.model.excel.RecProjectExcel;

import java.util.List;

/**
 * <p>
 * 参加项目记录表 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-10-13
 */
public interface RecProjectService extends IService<RecProject> {

    void saveRecVolunteerExcel(List<RecProjectExcel> recProjectExcels, RecImportParams params);
}
