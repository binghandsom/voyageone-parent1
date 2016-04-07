package com.voyageone.components.jumei.Bean;

/**
 * Created by sn3 on 2015-07-20.
 */
public class GetOrderIdsReq extends JmBaseBean {

    private String start_date;
    private String end_date;
    private String status;

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
