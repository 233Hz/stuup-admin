package com.poho.stuup.api.controller;


import cn.dev33.satoken.annotation.SaCheckSafe;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.common.util.TeachEvaUtil;
import com.poho.stuup.model.Class;
import com.poho.stuup.service.IClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 班级信息维护
 * 提供对班级信息的新增、修改、查询、删除等功能
 */

@Api(tags = "班级信息相关接口")
@RestController
@RequestMapping("/class")
public class ClassController {

    private final static Logger logger = LoggerFactory.getLogger(ClassController.class);
    @Autowired
    private IClassService classService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list(String key, String current, String size, Integer facultyId, Integer gradeId) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }

        return classService.findDataPageResult(key, page, pageSize, facultyId, gradeId);
    }

    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true)})
    @ApiOperation(value = "全部班级信息", httpMethod = "GET")
    @GetMapping("/all")
    public ResponseModel all() {
        return ResponseModel.newSuccessData(classService.findAllClass(null));
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public ResponseModel saveOrUpdate(@RequestBody Class clazz) {
        return classService.saveOrUpdate(clazz);
    }

    @SaCheckSafe("class_del")
    @RequestMapping(value = "/delMultiClass", method = RequestMethod.POST)
    public ResponseModel deleteClass(@RequestBody Map params) {
        //$$班级考试科目表、考试试卷表
        String ids = params.get("ids").toString();
        ResponseModel model = new ResponseModel();
        try {
            boolean ret = classService.deleteClass(ids);
            if (ret) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMsg("删除成功");
            } else {
                model.setCode(CommonConstants.CODE_EXCEPTION);
                model.setMsg("删除失败");
            }
        } catch (Exception e) {
            logger.error("删除班级信息失败，原因：", e);
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMsg("系统异常，请联系管理员");
        }
        return model;
    }

    /**
     * 班级信息导入模板
     *
     * @return
     */
    @RequestMapping(value = "/templateDownload")
    public void templateDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletContext().getRealPath("");
        String filePath = path + "/template/classTemplate.xls";
        String fileName = new String("班级信息导入模板.xls".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        TeachEvaUtil.downLoadExcel(response, in, out, filePath, fileName);
    }

    /**
     * 导入班级信息
     *
     * @param request
     * @return
     */
   /* @ResponseBody
    @RequestMapping("/importClass")
    public AjaxResponse importClass(HttpServletRequest request) {
        AjaxResponse<Map<String, Object>> response = new AjaxResponse<>(true);
        try {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                String path = request.getSession().getServletContext().getRealPath("/upload");
                MultipartFile importFile = multiRequest.getFile("classFile");
                String fix = importFile.getOriginalFilename().substring(importFile.getOriginalFilename().lastIndexOf(".")).toLowerCase();
                if (".xls".equals(fix) || ".xlsx".equals(fix)) {
                    String fileName = System.currentTimeMillis() + "_class" + fix;
                    File file = new File(path, fileName);
                    FileCopyUtils.copy(importFile.getBytes(), file);
                    List<Class> classes = new ExcelUtil().readClassExcel(file.getPath());
                    if (classes != null && classes.size() > 0) {
                        Map<String, Object> result = classService.addImportClasses(classes);
                        response.setSuccess(true);
                        response.setMessage("导入成功");
                        response.setData(result);
                    } else {
                        response.setMessage("未读取到班级信息");
                        response.setSuccess(false);
                    }
                } else {
                    response.setMessage("请选择Excel文件");
                    response.setSuccess(false);
                }
            } else {
                response.setMessage("请选择文件");
                response.setSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
*/

}
