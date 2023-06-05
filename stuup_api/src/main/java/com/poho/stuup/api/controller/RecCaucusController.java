package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.RecCaucusDTO;
import com.poho.stuup.model.vo.RecCaucusVO;
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
    public ResponseModel<IPage<RecCaucusVO>> getRecCaucusPage(Page<RecCaucusVO> page, RecCaucusDTO query) {
        return ResponseModel.ok(recCaucusService.getRecCaucusPage(page, query));
    }


}
