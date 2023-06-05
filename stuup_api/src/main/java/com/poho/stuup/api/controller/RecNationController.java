package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.RecNationDTO;
import com.poho.stuup.model.vo.RecNationVO;
import com.poho.stuup.service.RecNationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 参加国防民防项目记录填报 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/recNation")
public class RecNationController {

    @Resource
    private RecNationService recNationService;

    @GetMapping("/page")
    public ResponseModel<IPage<RecNationVO>> getRecNationPage(Page<RecNationVO> page, RecNationDTO query) {
        return ResponseModel.ok(recNationService.getRecNationPage(page, query));
    }

}
