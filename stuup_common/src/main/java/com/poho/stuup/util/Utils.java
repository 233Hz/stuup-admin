package com.poho.stuup.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.dao.RoleMapper;
import com.poho.stuup.dao.UserRoleMapper;
import com.poho.stuup.model.dto.TimePeriod;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class Utils {

    private static final String NUMBER_PATTERN = "\\d+";
    private static final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final String DATE_TIME_PATTERN = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";


    // 验证字符串日期格式为yyyy-MM-dd
    public boolean isDate(String date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            formatter.setLenient(false);
            formatter.parse(date);
            Pattern pattern = Pattern.compile(DATE_PATTERN);
            return pattern.matcher(date).matches();
        } catch (Exception e) {
            return false;
        }
    }

    // 验证字符串日期格式为yyyy-MM-dd HH:mm:ss
    public boolean isDateTime(String dateTime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setLenient(false);
            formatter.parse(dateTime);
            Pattern pattern = Pattern.compile(DATE_TIME_PATTERN);
            return pattern.matcher(dateTime).matches();
        } catch (Exception e) {
            return false;
        }
    }

    // 验证字符串是否为数字
    public boolean isNumber(String number) {
        if (StrUtil.isBlank(number)) return false;
        return number.matches(NUMBER_PATTERN);
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
