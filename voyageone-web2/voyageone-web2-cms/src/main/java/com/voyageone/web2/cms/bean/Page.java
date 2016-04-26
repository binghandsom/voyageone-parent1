package com.voyageone.web2.cms.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/19 14:10
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class Page implements Serializable {
    int curr = 1;//当前页
    int size = 20;//页大小
    List data; //总数据
    int total = 0;//总条数

    private Page() {
    }

    public static Page fromMap(Map<String, Object> params) {
        Page page = new Page();
        int curr = params.containsKey("curr") ? Integer.valueOf(params.get("curr")+"") : 1;
        int size = params.containsKey("size") ? Integer.valueOf(params.get("size")+"") : 20;
        page.setCurr(curr);
        page.setSize(size);
        return page;
    }

    public Page withData(List data) {
        this.total = (data != null && data.size() > 0) ? data.size() : 0;
        this.setData(data);
        return this;
    }

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public List getData() {
        int fromIndex = (curr - 1) * size;
        int toIndex = fromIndex + size;
        return data.subList(fromIndex, toIndex > data.size() ? data.size() : toIndex);
    }

    public void setData(List data) {
        this.data = data;
    }
}
