package com.poho.stuup.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.dao.RoleMapper;
import com.poho.stuup.dao.UserRoleMapper;
import com.poho.stuup.model.dto.TimePeriod;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@UtilityClass
public class Utils {

    // 验证字符串日期格式为yyyy-MM-dd
    public boolean isDate(String date) {
        if (StrUtil.isBlank(date)) return false;
        String regex = "\\d{4}-\\d{2}-\\d{2}";

        if (!date.matches(regex)) {
            return false; // 格式不正确
        }

        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            return true; // 日期有效
        } catch (DateTimeParseException e) {
            return false; // 日期无效
        }
    }

    // 验证字符串是否为数字
    public boolean isNumber(String number) {
        if (StrUtil.isBlank(number)) return false;
        return number.matches("\\d+");
    }

    /**
     * @description: 获取当前时间周期范围
     * @param: periodEnum
     * @return: com.poho.stuup.model.dto.TimePeriod
     * @author BUNGA
     * @date: 2023/5/30 14:12
     */
    public TimePeriod getCurrentTimePeriod(PeriodEnum periodEnum) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = periodEnum.getStartTime(now, periodEnum);
        LocalDateTime endTime = periodEnum.getEndTime(now, periodEnum);
        if (startTime == null) return null;
        if (endTime == null) return null;
        return TimePeriod.builder().startTime(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant())).endTime(Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant())).build();
    }

    /**
     * @description: 判读用户是否是超级管理员
     * @param: userId
     * @return: boolean
     * @author BUNGA
     * @date: 2023/5/30 18:52
     */
    public boolean isSuperAdmin(Long userId) {
        String superAdminName = "系统管理员";
        RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
        UserRoleMapper userRoleMapper = SpringContextHolder.getBean(UserRoleMapper.class);
        Long roleId = roleMapper.findRoleIdByName(superAdminName);
        if (roleId == null) {
            log.error("未设置系统管理员");
            return false;
        }
        List<Long> roleIds = userRoleMapper.queryUserRoleId(userId);
        if (CollUtil.isEmpty(roleIds)) {
            log.error("当前角色未设置角色");
            return false;
        }
        return roleIds.contains(roleId);
    }

}
