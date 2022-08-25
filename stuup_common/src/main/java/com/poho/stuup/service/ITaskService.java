package com.poho.stuup.service;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 15:21 2020/10/21
 * @Modified By:
 */
public interface ITaskService {
    /**
     * 处理开始提醒短信
     */
    void remindStart();

    /**
     * 处理结束提醒短信
     */
    void remindEnd();
}
