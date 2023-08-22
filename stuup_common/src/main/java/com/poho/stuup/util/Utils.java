package com.poho.stuup.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.poho.stuup.constant.ConfigKeyEnum;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.constant.RoleEnum;
import com.poho.stuup.dao.RoleMapper;
import com.poho.stuup.dao.UserRoleMapper;
import com.poho.stuup.model.Config;
import com.poho.stuup.model.dto.TimePeriod;
import com.poho.stuup.service.IConfigService;
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
        if (StrUtil.isBlank(number)) return true;
        return !number.matches(NUMBER_PATTERN);
    }

    /**
     * @description: 获得该周期的起止时间
     * @param: periodEnum
     * @param: date
     * @return: com.poho.stuup.model.dto.TimePeriod
     * @author BUNGA
     * @date: 2023/6/30 13:21
     */
    public TimePeriod getDateTimePeriod(PeriodEnum periodEnum, Date date) {
        Date startTime = null, endTime = null;
        int year = DateUtil.year(date);
        IConfigService configService = SpringContextHolder.getBean(IConfigService.class);
        Config config1 = configService.selectByPrimaryKey(ConfigKeyEnum.LAST_SEMESTER_START_TIME.getKey());
        Config config2 = configService.selectByPrimaryKey(ConfigKeyEnum.LAST_SEMESTER_END_TIME.getKey());
        Config config3 = configService.selectByPrimaryKey(ConfigKeyEnum.NEXT_SEMESTER_START_TIME.getKey());
        Config config4 = configService.selectByPrimaryKey(ConfigKeyEnum.NEXT_SEMESTER_END_TIME.getKey());

        switch (periodEnum) {
            case DAY:
                startTime = DateUtil.beginOfDay(date);
                endTime = DateUtil.endOfDay(date);
                break;
            case WEEK:
                startTime = DateUtil.beginOfWeek(date);
                endTime = DateUtil.endOfWeek(date);
                break;
            case MONTH:
                startTime = DateUtil.beginOfMonth(date);
                endTime = DateUtil.endOfMonth(date);
                break;
            case SEMESTER:
                if (config1 == null || config2 == null || config3 == null || config4 == null) return null;
                String configValue1 = config1.getConfigValue();
                String configValue2 = config2.getConfigValue();
                String configValue3 = config3.getConfigValue();
                String configValue4 = config4.getConfigValue();
                LocalDateTime localDateTime1 = LocalDateTime.of(year, Integer.parseInt(configValue2.substring(0, 2)), Integer.parseInt(configValue2.substring(3)), 23, 59, 59);
                LocalDateTime localDateTime2 = LocalDateTime.of(year, Integer.parseInt(configValue4.substring(0, 2)), Integer.parseInt(configValue4.substring(3)), 23, 59, 59);
                Date time1 = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
                Date time2 = Date.from(localDateTime2.atZone(ZoneId.systemDefault()).toInstant());
                if (date.compareTo(time1) <= 0) {
                    LocalDateTime startLocalDateTime = LocalDateTime.of(year - 1, Integer.parseInt(configValue1.substring(0, 2)), Integer.parseInt(configValue1.substring(3)), 0, 0, 0);
                    LocalDateTime endLocalDateTime = LocalDateTime.of(year, Integer.parseInt(configValue2.substring(0, 2)), Integer.parseInt(configValue2.substring(3)), 23, 59, 59);
                    startTime = Date.from(startLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    endTime = Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
                } else if (date.compareTo(time2) <= 0) {
                    LocalDateTime startLocalDateTime = LocalDateTime.of(year, Integer.parseInt(configValue3.substring(0, 2)), Integer.parseInt(configValue3.substring(3)), 0, 0, 0);
                    LocalDateTime endLocalDateTime = LocalDateTime.of(year, Integer.parseInt(configValue4.substring(0, 2)), Integer.parseInt(configValue4.substring(3)), 23, 59, 59);
                    startTime = Date.from(startLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    endTime = Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
                } else {
                    LocalDateTime startLocalDateTime = LocalDateTime.of(year, Integer.parseInt(configValue1.substring(0, 2)), Integer.parseInt(configValue1.substring(3)), 0, 0, 0);
                    LocalDateTime endLocalDateTime = LocalDateTime.of(year + 1, Integer.parseInt(configValue2.substring(0, 2)), Integer.parseInt(configValue2.substring(3)), 23, 59, 59);
                    startTime = Date.from(startLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    endTime = Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
                }
                break;
            case YEAR:
                if (config1 == null || config4 == null) return null;
                // 当前年上学期开学时间
                LocalDateTime localDateTime = LocalDateTime.of(year, Integer.parseInt(config1.getConfigValue().substring(0, 2)), Integer.parseInt(config1.getConfigValue().substring(3)), 0, 0, 0);
                Date time = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                if (date.compareTo(time) < 0) {
                    LocalDateTime startLocalDateTime = LocalDateTime.of(year - 1, Integer.parseInt(config1.getConfigValue().substring(0, 2)), Integer.parseInt(config1.getConfigValue().substring(3)), 0, 0, 0);
                    LocalDateTime endLocalDateTime = LocalDateTime.of(year, Integer.parseInt(config4.getConfigValue().substring(0, 2)), Integer.parseInt(config4.getConfigValue().substring(3)), 0, 0, 0);
                    startTime = Date.from(startLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    endTime = Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
                } else {
                    LocalDateTime startLocalDateTime = LocalDateTime.of(year, Integer.parseInt(config1.getConfigValue().substring(0, 2)), Integer.parseInt(config1.getConfigValue().substring(3)), 0, 0, 0);
                    LocalDateTime endLocalDateTime = LocalDateTime.of(year + 1, Integer.parseInt(config4.getConfigValue().substring(0, 2)), Integer.parseInt(config4.getConfigValue().substring(3)), 0, 0, 0);
                    startTime = Date.from(startLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    endTime = Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
                }
                break;
            default:
                break;
        }
        return TimePeriod.builder().startTime(startTime).endTime(endTime).build();
    }

    /**
     * @description: 判读用户是否是超级管理员
     * @param: userId
     * @return: boolean
     * @author BUNGA
     * @date: 2023/5/30 18:52
     */
    public boolean isSuperAdmin(Long userId) {
        String superAdminName = RoleEnum.ADMIN.getRoleName();
        RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
        UserRoleMapper userRoleMapper = SpringContextHolder.getBean(UserRoleMapper.class);
        Long roleId = roleMapper.findRoleIdByName(superAdminName);
        if (roleId == null) {
            log.error("未设置系统管理员");
            return false;
        }
        List<Long> roleIds = userRoleMapper.queryUserRoleId(userId);
        if (CollUtil.isEmpty(roleIds)) {
            log.error("当前用户未设置角色");
            return false;
        }
        return roleIds.contains(roleId);
    }

    /**
     * @description: 判断某个用户是否拥有某些权限
     * @param: userId
     * @param: roleEnums
     * @return: boolean
     * @author BUNGA
     * @date: 2023/6/9 10:34
     */
    public boolean hasRole(Long userId, RoleEnum... roleEnums) {
        RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
        UserRoleMapper userRoleMapper = SpringContextHolder.getBean(UserRoleMapper.class);
        List<Long> roleIds = userRoleMapper.queryUserRoleId(userId);
        if (CollUtil.isEmpty(roleIds)) {
            log.error("当前用户未设置角色");
            return false;
        }

        for (RoleEnum roleEnum : roleEnums) {
            Long roleId = roleMapper.findRoleIdByName(roleEnum.getRoleName());
            if (roleId != null) {
                if (roleIds.contains(roleId)) {
                    return true;
                }
            } else {
                log.error("{}角色不存在", roleEnum.getRoleName());
            }
        }
        return false;
    }

    public int calculatorGCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }


}
