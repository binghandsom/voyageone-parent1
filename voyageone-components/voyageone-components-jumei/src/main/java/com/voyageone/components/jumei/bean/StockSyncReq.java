package com.voyageone.components.jumei.bean;

/**
 * Created by jack on 2016-01-12.
 */
public class StockSyncReq extends JmBaseBean {

    private String businessman_code;
    private String enable_num;

    public String getBusinessman_code() {
        return businessman_code;
    }

    public void setBusinessman_code(String businessman_code) {
        this.businessman_code = businessman_code;
    }

    public String getEnable_num() {
        return enable_num;
    }

    public void setEnable_num(String enable_num) {
        this.enable_num = enable_num;
    }
}
