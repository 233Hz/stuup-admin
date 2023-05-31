package com.poho.stuup.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.dao.GrowthItemMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.handle.excel.RecDefaultListener;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.excel.RecDefaultExcel;
import com.poho.stuup.service.GrowUserService;
import com.poho.stuup.service.RecDefaultService;
import com.poho.stuup.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    default ResponseModel recImport(MultipartFile file, Map<String, Object> params) {
        try {
            StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
            RecDefaultService recDefaultService = SpringContextHolder.getBean(RecDefaultService.class);
            GrowthItemMapper growthItemMapper = SpringContextHolder.getBean(GrowthItemMapper.class);
            GrowUserService growUserService = SpringContextHolder.getBean(GrowUserService.class);
            String recCode = (String) params.get("rec_code");
            GrowthItem growthItem = growthItemMapper.selectOne(Wrappers.<GrowthItem>lambdaQuery()
                    .eq(GrowthItem::getCode, recCode));
            if (growthItem == null) return ResponseModel.failed("导入项目不存在");
            Long growId = growthItem.getId();
            // 查询项目负责人
            String userId = (String) params.get("userId");
            boolean isGrowUser = growUserService.isGrowUser(Long.parseLong(userId), growId);
            if (!isGrowUser) return ResponseModel.failed("不是该项目负责人，无法导入");
            log.info("开始导入");
            long start = System.currentTimeMillis();
            RecDefaultListener recDefaultListener = new RecDefaultListener(start, params, growthItem, studentMapper, recDefaultService);
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
            return ResponseModel.ok("导入成功");
        } catch (IOException e) {
            return ResponseModel.failed("导入失败");
        }
    }

    /**
     * @description: 导出
     * @param: response
     * @param: params   额外参数
     * @return: void
     * @author BUNGA
     * @date: 2023/5/26 11:01
     */
    default void recExport(HttpServletResponse response, Map<String, Object> params) {

    }
}
