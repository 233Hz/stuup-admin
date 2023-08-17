package com.poho.stuup.api.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.AnnouncementStateEnum;
import com.poho.stuup.model.Announcement;
import com.poho.stuup.model.dto.AnnouncementDTO;
import com.poho.stuup.model.vo.AnnouncementVO;
import com.poho.stuup.service.AnnouncementService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * 发布公告表 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-08
 */
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private AnnouncementService announcementService;

    @GetMapping("/page")
    public ResponseModel<IPage<AnnouncementVO>> notifyPage(Page<AnnouncementVO> page, AnnouncementDTO query) {
        return ResponseModel.ok(announcementService.notifyPage(page, query));
    }

    @PostMapping("/save_update")
    public ResponseModel<Boolean> saveOrUpdateNotify(@Valid @RequestBody AnnouncementDTO data) {
        String userId = ProjectUtil.obtainLoginUser(request);
        data.setUserId(Long.valueOf(userId));
        return announcementService.saveOrUpdateNotify(data);
    }

    @GetMapping("/publish/{id}")
    public ResponseModel<Boolean> publishNotify(@PathVariable("id") Long id) {
        Announcement announcement = announcementService.getById(id);
        Integer state = announcement.getState();
        boolean result;
        String message;
        if (state == AnnouncementStateEnum.PUBLISHED.getValue()) {
            result = announcementService.update(Wrappers.<Announcement>lambdaUpdate()
                    .set(Announcement::getState, AnnouncementStateEnum.UNPUBLISHED.getValue())
                    .eq(Announcement::getId, id));
            message = "撤回";
        } else if (state == AnnouncementStateEnum.UNPUBLISHED.getValue()) {
            result = announcementService.update(Wrappers.<Announcement>lambdaUpdate()
                    .set(Announcement::getState, AnnouncementStateEnum.PUBLISHED.getValue())
                    .eq(Announcement::getId, id));
            message = "发布";
        } else {
            return ResponseModel.failed("数据错误[公告类型不存在]");
        }
        return result ? ResponseModel.ok(true, StrUtil.format("{}成功", message)) : ResponseModel.failed(false, StrUtil.format("{}失败", message));
    }

    @DeleteMapping("/del/{id}")
    public ResponseModel<Boolean> delNotify(@PathVariable("id") Long id) {
        boolean result = announcementService.removeById(id);
        return result ? ResponseModel.ok(true, "删除成功") : ResponseModel.failed(false, "删除失败");
    }

    @GetMapping("/my/notify/page")
    public ResponseModel<IPage<AnnouncementVO>> myNotifyPage(Page<AnnouncementVO> page, AnnouncementDTO query) {
        String userId = ProjectUtil.obtainLoginUser(request);
        query.setUserId(Long.valueOf(userId));
        return ResponseModel.ok(announcementService.myNotifyPage(page, query));
    }

    @GetMapping("/my/system/page")
    public ResponseModel<IPage<AnnouncementVO>> mySystemPage(Page<AnnouncementVO> page, AnnouncementDTO query) {
        String userId = ProjectUtil.obtainLoginUser(request);
        query.setUserId(Long.valueOf(userId));
        return ResponseModel.ok(announcementService.mySystemPage(page, query));
    }

    @GetMapping("/{id}")
    public ResponseModel<Announcement> notifyById(@PathVariable("id") Long id) {
        return ResponseModel.ok(announcementService.getById(id));
    }

}
