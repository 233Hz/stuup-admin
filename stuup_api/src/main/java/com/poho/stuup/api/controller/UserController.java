package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.api.config.PropertiesConfig;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.custom.CusUser;
import com.poho.stuup.model.Menu;
import com.poho.stuup.model.User;
import com.poho.stuup.service.IUserService;
import com.poho.stuup.util.ExcelUtil;
import com.poho.stuup.util.ProjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 13:51 2020/9/3
 * @Modified By:
 */
@Api(tags = "用户管理相关接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private PropertiesConfig config;

    /**
     * 分页查询用户
     *
     * @param key
     * @param state
     * @param current
     * @param size
     * @return
     */
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true)})
    @ApiOperation(value = "获取所有用户", httpMethod = "GET")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list(String key, String state, String current, String size) {
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        return userService.findDataPageResult(key, state, page, pageSize);
    }

    /**
     * 查询所有的数据
     *
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseModel all() {
        return userService.queryList();
    }

    @GetMapping("/info")
    public ResponseModel<CusUser> getUserInfo() {
        String userId = ProjectUtil.obtainLoginUser(request);
        return ResponseModel.ok(userService.getUserInfo(Long.valueOf(userId)));
    }

    @PostMapping("/queryUserAuthority")
    public ResponseModel<List<Menu>> queryUserAuthority() {
        String userId = ProjectUtil.obtainLoginUser(request);
        return userService.queryUserAuthority(Long.parseLong(userId));
    }


    /**
     * @return
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public ResponseModel data() {
        ResponseModel model = new ResponseModel();
        String userId = ProjectUtil.obtainLoginUser(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (MicrovanUtil.isNotEmpty(user)) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMsg("获取成功");
            model.setData(ProjectUtil.convertCusUser(user));
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMsg("获取失败，请稍后重试");
        }
        return model;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/userData", method = RequestMethod.GET)
    public ResponseModel userData(Long oid, Long yearId) {
        return userService.findUserData(oid, yearId);
    }

    /**
     * @param user
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseModel save(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }

    /**
     * @param params
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public ResponseModel del(@RequestBody Map params) {
        String ids = params.get("ids").toString();
        return userService.del(ids);
    }

    /**
     * @param params
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public ResponseModel updatePassword(@RequestBody Map params) {
        String userId = ProjectUtil.obtainLoginUser(request);
        return userService.updatePassword(userId, params);
    }

    /**
     * 导入用户信息
     *
     * @param importFile
     * @return
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseModel importData(@RequestParam("importFile") MultipartFile importFile) {
        ResponseModel<Map<String, Object>> model = new ResponseModel<>();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMsg("导入失败，请稍后重试");
        try {
            String path = config.getBaseDoc() + File.separator + ProjectConstants.PROJECT_TEMP;
            MicrovanUtil.createFolder(path);

            String fix = importFile.getOriginalFilename().substring(importFile.getOriginalFilename().lastIndexOf(".")).toLowerCase();
            if (".xls".equals(fix) || ".xlsx".equals(fix)) {
                String fileName = System.currentTimeMillis() + "_user" + fix;
                File file = new File(path, fileName);
                FileCopyUtils.copy(importFile.getBytes(), file);
                List<User> userList = new ExcelUtil().readUserExcel(file.getPath());
                if (MicrovanUtil.isNotEmpty(userList)) {
                    Map<String, Object> result = userService.importUserList(userList);
                    model.setCode(CommonConstants.CODE_SUCCESS);
                    model.setMsg("导入成功");
                    model.setData(result);
                } else {
                    model.setMsg("未读取到数据");
                    model.setCode(CommonConstants.CODE_EXCEPTION);
                }
            } else {
                model.setMsg("请选择Excel文件");
                model.setCode(CommonConstants.CODE_EXCEPTION);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @GetMapping("/updateAvatar")
    public ResponseModel<String> updateUserAvatar(@RequestParam("avatarId") Long avatarId) {
        String userId = ProjectUtil.obtainLoginUser(request);
        return userService.updateUserAvatar(Long.valueOf(userId), avatarId);
    }
}
