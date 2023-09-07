package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.AudStateEnum;
import com.poho.stuup.model.AudGrow;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.dto.GrowRecordDTO;
import com.poho.stuup.model.vo.AudGrowthVO;

import java.util.List;

/**
 * <p>
 * 成长项目审核记录 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
public interface AudGrowService extends IService<AudGrow> {

    IPage<AudGrowthVO> pageAud(Page<AudGrowthVO> page, GrowRecordDTO query);

    /**
     * @description: 申请成长项目
     * @param: data
     * @return: com.poho.common.custom.ResponseModel<com.poho.stuup.model.AudGrow>
     * @author BUNGA
     * @date: 2023/6/15 16:06
     */
    ResponseModel<Boolean> apply(AudGrow data);

    /**
     * @description: 更新记录状态
     * @param: id
     * @param: growthItemId
     * @param: audStateEnum
     * @param: userId
     * @param: reason
     * @return: void
     * @author BUNGA
     * @date: 2023/6/16 14:25
     */
    void updateState(Long id, Long growId, AudStateEnum audStateEnum, Long userId, String reason);


    /**
     * 批量提交申请记录
     *
     * @param allStudent       所有学生对象集合
     * @param importStudentIds 导入的学生id
     * @param yearId           年份id
     * @param semesterId       学期id
     * @param growthItem       成长项目
     * @param userId           创建人id
     */
    void batchSubmitAudGrows(List<Student> allStudent, List<Long> importStudentIds, Long yearId, Long semesterId, GrowthItem growthItem, Long userId);

    /**
     * 通过审核
     */
    boolean pass(Long id, Long currentUserId);

    /**
     * 不通过审核
     */
    boolean noPass(Long id, String reason, Long currentUserId);
}
