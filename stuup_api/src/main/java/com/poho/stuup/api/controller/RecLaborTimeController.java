package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.RecLaborTimeDTO;
import com.poho.stuup.model.vo.RecLaborTimeVO;
import com.poho.stuup.service.RecLaborTimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 生产劳动实践记录填报 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/recLaborTime")
public class RecLaborTimeController {

    @Resource
    private RecLaborTimeService recLaborTimeService;

    @GetMapping("/page")
    public ResponseModel<IPage<RecLaborTimeVO>> getRecLaborTimePage(Page<RecLaborTimeVO> page, RecLaborTimeDTO query) {
        return ResponseModel.ok(recLaborTimeService.getRecLaborTimePage(page, query));
    }

}
