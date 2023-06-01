package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.RecSocietyDTO;
import com.poho.stuup.model.vo.RecSocietyVO;
import com.poho.stuup.service.RecSocietyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 参加社团记录填报 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/recSociety")
public class RecSocietyController {

    @Resource
    private RecSocietyService recSocietyService;

    @GetMapping("/page")
    public ResponseModel<IPage<RecSocietyVO>> getRecSocietyPage(Page<RecSocietyVO> page, RecSocietyDTO query) {
        return ResponseModel.ok(recSocietyService.getRecSocietyPage(page, query));
    }

}
