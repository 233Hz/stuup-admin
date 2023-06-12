package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.AnnouncementScopeEnum;
import com.poho.stuup.constant.AnnouncementStateEnum;
import com.poho.stuup.constant.UserTypeEnum;
import com.poho.stuup.model.Announcement;
import com.poho.stuup.model.User;
import com.poho.stuup.model.dto.AnnouncementDTO;
import com.poho.stuup.model.dto.AnnouncementPremUserDTO;
import com.poho.stuup.model.vo.AnnouncementPremUserVO;
import com.poho.stuup.model.vo.AnnouncementVO;
import com.poho.stuup.service.AnnouncementService;
import com.poho.stuup.service.IStudentService;
import com.poho.stuup.service.IUserService;
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
    private IUserService userService;

    @Resource
    private IStudentService studentService;

    @Resource
    private AnnouncementService announcementService;

    @GetMapping("/page")
    public ResponseModel<IPage<AnnouncementVO>> getAnnouncementPage(Page<AnnouncementVO> page, AnnouncementDTO query) {
        return ResponseModel.ok(announcementService.getAnnouncementPage(page, query));
    }

    @PostMapping("/save_update")
    public ResponseModel<Boolean> saveOrUpdateAnnouncement(@Valid @RequestBody Announcement announcement) {
        String userId = ProjectUtil.obtainLoginUser(request);
        announcement.setCreateUser(Long.valueOf(userId));
        return ResponseModel.ok(announcementService.saveOrUpdate(announcement));
    }

    @GetMapping("/publish/{id}")
    public ResponseModel<Boolean> publishAnnouncement(@PathVariable("id") Long id) {
        Announcement announcement = announcementService.getById(id);
        Integer type = announcement.getType();
        boolean result;
        if (type == AnnouncementStateEnum.PUBLISHED.getValue()) {
            result = announcementService.update(Wrappers.<Announcement>lambdaUpdate()
                    .set(Announcement::getType, AnnouncementStateEnum.UNPUBLISHED.getValue()));
        } else if (type == AnnouncementStateEnum.UNPUBLISHED.getValue()) {
            result = announcementService.update(Wrappers.<Announcement>lambdaUpdate()
                    .set(Announcement::getType, AnnouncementStateEnum.PUBLISHED.getValue()));
        } else {
            return ResponseModel.failed("数据错误[公告类型不存在]");
        }
        return result ? ResponseModel.ok(true, "发布成功") : ResponseModel.failed(false, "发布失败");
    }

    @DeleteMapping("/del/{id}")
    public ResponseModel<Boolean> delAnnouncement(@PathVariable("id") Long id) {
        boolean result = announcementService.removeById(id);
        return result ? ResponseModel.ok(true, "删除成功") : ResponseModel.failed(false, "删除失败");
    }

    @GetMapping("/premUser/page")
    public ResponseModel<IPage<AnnouncementPremUserVO>> getAnnouncementPremUserVO(Page<AnnouncementPremUserVO> page, AnnouncementPremUserDTO query) {
        String userId = ProjectUtil.obtainLoginUser(request);
        query.setCurrUser(Long.valueOf(userId));
        return ResponseModel.ok(announcementService.getAnnouncementPremUserVO(page, query));
    }

    @GetMapping("/myPage")
    public ResponseModel<IPage<AnnouncementVO>> getAnnouncementMyPage(Page<AnnouncementVO> page, AnnouncementDTO query) {
        String userId = ProjectUtil.obtainLoginUser(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        Integer userType = user.getUserType();
        Integer scope = null;
        if (userType == UserTypeEnum.STUDENT.getValue()) {
            scope = AnnouncementScopeEnum.ALL_TEACHER.getValue();
        }
        query.setScope(scope);
        return ResponseModel.ok(announcementService.getAnnouncementPage(page, query));
    }

}
