package com.poho.stuup.model.vo;

import com.poho.stuup.model.Menu;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class UserInfoPermissionVO {

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 菜单权限
     */
    private List<Menu> menus;

    /**
     * 用户角色
     */
    private List<String> roles;

    /**
     * 用户按钮权限
     */
    private List<String> permissions;

    /**
     * 花朵兑换配置
     */
    private FlowerVO flowerConfig;

    /**
     * 成长信息
     */
    private GrowthInfo growthInfo;

    /**
     * 学生信息
     */
    private OtherInfo otherInfo;

    @Data
    @Builder
    public static class UserInfo {

        /**
         * 用户id
         */
        private Long userId;

        /**
         * 用户名
         */
        private String userName;

        /**
         * 用户类型
         */
        private Integer userType;

        /**
         * 用户头像
         */
        private String avatarUrl;
    }

    @Data
    @Builder
    public static class GrowthInfo {

        /**
         * 总积分
         */
        private BigDecimal totalScore;

        /**
         * 当前排名
         */
        private Integer rank;

    }

    @Data
    @Builder
    public static class OtherInfo {

        /**
         * 学年id
         */
        private Integer studentId;

        /**
         * 学年id
         */
        private Long yearId;

        /**
         * 学期id
         */
        private Long semesterId;

    }

}
