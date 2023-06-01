package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.GrowSearchDTO;
import com.poho.stuup.model.dto.RecDefaultDTO;
import com.poho.stuup.model.vo.GrowRecordVO;
import com.poho.stuup.model.vo.RecDefaultVO;
import com.poho.stuup.service.RecDefaultService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 默认积分记录表（除综评表） 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-26
 */
@RestController
@RequestMapping("/recDefault")
public class RecDefaultController {

    @Resource
    private RecDefaultService recDefaultService;

    @GetMapping("/page")
    public ResponseModel<IPage<GrowRecordVO>> growthRecordPage(Page page, GrowSearchDTO query) {
        return ResponseModel.ok(recDefaultService.growthRecordPage(page, query));
    }

    @GetMapping("/details/{batchCode}")
    public ResponseModel<List<RecDefaultVO>> growthRecordDetails(@PathVariable("batchCode") Long batchCode, RecDefaultDTO query) {
        query.setBatchCode(batchCode);
        return ResponseModel.ok(recDefaultService.growthRecordDetails(query));
    }

}
