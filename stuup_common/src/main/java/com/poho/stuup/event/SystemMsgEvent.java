package com.poho.stuup.event;

import com.poho.stuup.model.dto.SystemMagVO;
import org.springframework.context.ApplicationEvent;

/**
 * @author BUNGA
 * @description: 发送消息事件
 * @date 2023/6/26 14:38
 */
public class SystemMsgEvent extends ApplicationEvent {

    private final SystemMagVO systemMagVO;


    public SystemMsgEvent(SystemMagVO systemMagVO) {
        super(systemMagVO);
        this.systemMagVO = systemMagVO;
    }

    public SystemMagVO getSystemMagVO() {
        return systemMagVO;
    }
}
