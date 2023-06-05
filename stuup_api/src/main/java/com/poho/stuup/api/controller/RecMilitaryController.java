package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.RecMilitaryDTO;
import com.poho.stuup.model.vo.RecMilitaryVO;
import com.poho.stuup.service.RecMilitaryService;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/page")
    public ResponseModel<IPage<RecMilitaryVO>> getRecMilitaryPage(Page<RecMilitaryVO> page, RecMilitaryDTO query) {
        return ResponseModel.ok(recMilitaryService.getRecMilitaryPage(page, query));
    }

}
