package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.RecLaborTime;
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
    public ResponseModel<IPage<RecLaborTime>> getRecLaborTimePage(Page<RecLaborTime> page, RecLaborTime query) {
        return ResponseModel.ok(recLaborTimeService.page(page, Wrappers.<RecLaborTime>lambdaQuery()
                .gt(query.getHours() != null, RecLaborTime::getHours, query.getHours())));
    }

}
