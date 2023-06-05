package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.RecVolunteerDTO;
import com.poho.stuup.model.vo.RecVolunteerVO;
import com.poho.stuup.service.RecVolunteerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 志愿者活动记录填报 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/recVolunteer")
public class RecVolunteerController {

    @Resource
    private RecVolunteerService recVolunteerService;

    @GetMapping("/page")
    public ResponseModel<IPage<RecVolunteerVO>> getVolunteerPage(Page<RecVolunteerVO> page, RecVolunteerDTO query) {
        return ResponseModel.ok(recVolunteerService.getVolunteerPage(page, query));
    }

}
