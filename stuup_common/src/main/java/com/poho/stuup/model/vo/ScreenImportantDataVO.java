package com.poho.stuup.model.vo;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 可视化重点突出的数据
 * @date 2023/8/11 14:23
 */
@Data
public class ScreenImportantDataVO {

    /**
     * 在校生人数
     */
    private Integer atSchoolNum;

    /**
     * 男女比例
     */
    private String sexRatio;

    /**
     * 班级总数
     */
    private Integer classNum;

    /**
     * 社团总数
     */
    private Integer clubNum;

    /**
     * 专业总数
     */
    private Integer majorNum;

    /**
     * 成长项目总数
     */
    private Long growthNum;

    /**
     * 举办活动次数
     */
    private Long activityNum;

    /**
     * 合格率
     */
    private String passRate;

    /**
     * 获得奖学金人数
     */
    private Long scholarshipNum;
}
