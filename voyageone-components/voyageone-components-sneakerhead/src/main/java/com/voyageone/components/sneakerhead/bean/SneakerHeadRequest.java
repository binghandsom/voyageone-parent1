package com.voyageone.components.sneakerhead.bean;

import java.util.Date;

/**
 * SneakerHeadRequest
 *
 * @author gjl on 2016/11/15.
 * @version 0.0.1
 */
public class SneakerHeadRequest {

    private int PageSize = 10;

    private int PageNumber = 1;

    private Date Time;

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public int getPageNumber() {
        return PageNumber;
    }

    public void setPageNumber(int pageNumber) {
        PageNumber = pageNumber;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
