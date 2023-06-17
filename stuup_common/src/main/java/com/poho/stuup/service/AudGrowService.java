package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.AutStateEnum;
import com.poho.stuup.model.AudGrow;
import com.poho.stuup.model.dto.GrowRecordDTO;
import com.poho.stuup.model.vo.GrowApplyRecordVO;
import com.poho.stuup.model.vo.GrowAuditRecordVO;

/**
 * <p>
 * 成长项目审核记录 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
public interface AudGrowService extends IService<AudGrow> {

    /**
     * @description: 申请成长项目
     * @param: data
     * @return: com.poho.common.custom.ResponseModel<com.poho.stuup.model.AudGrow>
     * @author BUNGA
     * @date: 2023/6/15 16:06
     */
    ResponseModel<Boolean> applyGrowItem(AudGrow data);

    /**
     * @description: 查询我的申请记录（分页）
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.GrowApplyRecordVO>
     * @author BUNGA
     * @date: 2023/6/15 16:55
     */
    IPage<GrowApplyRecordVO> pageGrowApplyRecord(Page<GrowApplyRecordVO> page, GrowRecordDTO query);

    /**
     * @description: 查询我的审核记录（分页）
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.GrowAuditRecordVO>
     * @author BUNGA
     * @date: 2023/6/15 17:16
     */
    ResponseModel<IPage<GrowAuditRecordVO>> pageGrowAuditRecord(Page<GrowAuditRecordVO> page, GrowRecordDTO query);

    /**
     * @description: 更新记录状态
     * @param: id
     * @param: growId
     * @param: autStateEnum
     * @param: userId
     * @param: reason
     * @return: void
     * @author BUNGA
     * @date: 2023/6/16 14:25
     */
    void updateRecordState(Long id, Long growId, AutStateEnum autStateEnum, Long userId, String reason);

}
