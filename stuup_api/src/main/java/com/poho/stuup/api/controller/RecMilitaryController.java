package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.RecMilitary;
import com.poho.stuup.service.RecMilitaryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 军事训练记录填报 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/recMilitary")
public class RecMilitaryController {

    @Resource
    private RecMilitaryService recMilitaryService;

    public ResponseModel<IPage<RecMilitary>> getRecMilitaryPage(Page<RecMilitary> page, RecMilitary recMilitary) {
        return null;
    }

}
