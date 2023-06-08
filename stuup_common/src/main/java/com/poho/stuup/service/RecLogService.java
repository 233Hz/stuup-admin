package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.dto.RecLogDTO;
import com.poho.stuup.model.vo.RecLogDetailsVO;
import com.poho.stuup.model.vo.RecLogVO;

import java.util.List;

/**
 * <p>
 * 项目记录日志 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-01
 */
public interface RecLogService extends IService<RecLog> {

    /**
     * @description: 分页查询
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.RecLogVO>
     * @author BUNGA
     * @date: 2023/6/2 15:56
     */
    IPage<RecLogVO> getRecLogPage(Page<RecLogVO> page, RecLogDTO query);

    /**
     * @description: 查询记录详情
     * @param: id
     * @return: com.poho.stuup.model.vo.RecLogDetailsVO
     * @author BUNGA
     * @date: 2023/6/2 16:33
     */
    List<RecLogDetailsVO> getRecLogDetails(Long id);
}
