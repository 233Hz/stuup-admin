package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.RecHonorDTO;
import com.poho.stuup.model.vo.RecHonorVO;
import com.poho.stuup.service.RecHonorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 人荣誉记录填报 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/recHonor")
public class RecHonorController {

    @Resource
    private RecHonorService recHonorService;

    @GetMapping("/page")
    public ResponseModel<IPage<RecHonorVO>> getRecHonorPage(Page<RecHonorVO> page, RecHonorDTO query) {
        return ResponseModel.ok(recHonorService.getRecHonorPage(page, query));
    }

}