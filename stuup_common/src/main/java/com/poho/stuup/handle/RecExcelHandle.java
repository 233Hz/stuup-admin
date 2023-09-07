package com.poho.stuup.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.SemesterMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.handle.excel.RecDefaultListener;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecDefaultExcel;
import com.poho.stuup.service.RecDefaultService;
import com.poho.stuup.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/5/25 13:38
 */
public interface RecExcelHandle {

    Logger log = LoggerFactory.getLogger(RecExcelHandle.class);

    /**
     * @description: 导入
     * @param: file
     * @param: params   额外参数
     * @return: com.poho.common.custom.ResponseModel
     * @author BUNGA
     * @date: 2023/5/26 11:01
     */
    default ResponseModel<List<ExcelError>> recImport(MultipartFile file, GrowthItem growthItem, Long userId) {
        try {
            StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
            RecDefaultService recDefaultService = SpringContextHolder.getBean(RecDefaultService.class);
            YearMapper yearMapper = SpringContextHolder.getBean(YearMapper.class);
            SemesterMapper semesterMapper = SpringContextHolder.getBean(SemesterMapper.class);
            Year year = yearMapper.getCurrentYear();
            if (year == null) return ResponseModel.failed("不在当前学年时间段内");
            Semester semester = semesterMapper.selectOne(Wrappers.<Semester>lambdaQuery().eq(Semester::getIsCurrent, WhetherEnum.YES.getValue()));
            if (semester == null) return ResponseModel.failed("不在当前学期时间段内");
            log.info("开始导入");
            long start = System.currentTimeMillis();
            RecDefaultListener recDefaultListener = new RecDefaultListener(studentMapper, recDefaultService, growthItem, year.getOid(), semester.getId(), userId, start);
            EasyExcel.read(file.getInputStream(), RecDefaultExcel.class, recDefaultListener).sheet().doRead();
            long end = System.currentTimeMillis();
            log.info("耗时:{}ms", end - start);
            log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
            if (recDefaultListener.total == 0) {
                return ResponseModel.failed("Excel为空！");
            }
            if (CollUtil.isNotEmpty(recDefaultListener.errors)) {
                return ResponseModel.ok(recDefaultListener.errors, StrUtil.format("导入成功[总条数：{}，成功：{}，失败：{}]", recDefaultListener.total, recDefaultListener.success, recDefaultListener.fail));
            }
            return ResponseModel.ok(null, "导入成功");
        } catch (IOException e) {
            return ResponseModel.failed("导入失败");
        }
    }

    /**
     * @description: 导出
     * @param: response
     * @param: params
     * @return: void
     * @author BUNGA
     * @date: 2023/6/19 10:05
     */
    void recExport(HttpServletResponse response, Map<String, Object> params) throws IOException;

}
