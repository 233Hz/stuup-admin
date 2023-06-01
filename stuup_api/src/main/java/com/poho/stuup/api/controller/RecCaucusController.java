package com.poho.stuup.api.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.RecCaucus;
import com.poho.stuup.service.RecCaucusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 参加党团学习项目记录填报 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/recCaucus")
public class RecCaucusController {

    @Resource
    private RecCaucusService recCaucusService;

    @GetMapping("/page")
    public ResponseModel<Page<RecCaucus>> getRecCaucusPage(Page<RecCaucus> page, RecCaucus query) {
        return ResponseModel.ok(
                recCaucusService.page(page, Wrappers.<RecCaucus>lambdaQuery()
                        .eq(query.getYearId() != null, RecCaucus::getYearId, query.getYearId())
                        .eq(query.getStudentId() != null, RecCaucus::getStudentId, query.getStudentId())
                        .eq(query.getLevel() != null, RecCaucus::getLevel, query.getLevel())
                        .like(StrUtil.isNotBlank(query.getName()), RecCaucus::getName, query.getName()))
        );
    }


}
