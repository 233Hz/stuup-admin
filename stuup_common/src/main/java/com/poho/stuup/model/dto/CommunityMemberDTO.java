package com.poho.stuup.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor

public class CommunityMemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer memberId;
    private String termName;
    private String communityName;
    private String stuNo;
    private String stuName;

}
