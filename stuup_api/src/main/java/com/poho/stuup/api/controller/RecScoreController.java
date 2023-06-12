package com.poho.stuup.api.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.vo.RecScoreVO;
import com.poho.stuup.service.RecScoreService;
import com.poho.stuup.util.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 积分记录表 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@RestController
@RequestMapping("/recScore")
public class RecScoreController {

    @Resource
    private RecScoreService recScoreService;

    @GetMapping("/page")
    public ResponseModel<IPage<RecScoreVO>> getRecScorePage(Page<RecScoreVO> page, RecScoreDTO query) {
        if (StrUtil.isNotBlank(query.getStartTime())) {
            boolean isDateTime = Utils.isDateTime(query.getStartTime());
            if (!isDateTime) return ResponseModel.failed("开始时间格式错误");
        }
        if (StrUtil.isNotBlank(query.getEndTime())) {
            boolean isDateTime = Utils.isDateTime(query.getEndTime());
            if (!isDateTime) return ResponseModel.failed("结束时间格式错误");
        }
        return ResponseModel.ok(recScoreService.getRecScorePage(page, query));
    }

}
