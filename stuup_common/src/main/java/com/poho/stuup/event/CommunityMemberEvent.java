package com.poho.stuup.event;

import org.springframework.context.ApplicationEvent;

public class CommunityMemberEvent extends ApplicationEvent {

    private final Integer infoId;


    public CommunityMemberEvent(Integer infoId) {
        super(infoId);
        this.infoId = infoId;
    }

    public Integer getInfoId(){
        return infoId;
    }
}
