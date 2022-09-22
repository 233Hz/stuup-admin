package com.poho.stuup.model.dto;


import com.poho.common.constant.CommonConstants;

import java.io.Serializable;

public class PageDTO implements Serializable {

    private int currPage = 1;

    private int pageSize = CommonConstants.PAGE_SIZE;

    public int getCurrPage() {
        return currPage > 1 ? currPage : 1;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getPageSize() {
        return pageSize > CommonConstants.PAGE_SIZE ? pageSize : CommonConstants.PAGE_SIZE;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
