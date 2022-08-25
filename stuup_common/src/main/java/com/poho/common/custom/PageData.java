package com.poho.common.custom;

import java.util.List;

/**
 * @Author wupeng
 * @Description 数据分页对象类
 * @Date 2020-08-09 16:04
 * @return
 */
public class PageData<T> {
    private int size;
    private int current;
    private int total;
    private int pages;
    private List<T> records;

    /**
     * 开始的数据的下标
     */
    private int start;

    public PageData() {

    }

    public PageData(int current, int size, int total) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.pages = (total % size == 0) ? (total / size) : (total / size + 1);
        if (current < 1) {
            this.current = 1;
        }
        if (current > pages && pages > 0) {
            this.current = pages;
        }
        this.start = (current - 1) * this.size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
