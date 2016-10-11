package com.voyageone.web2.vms.bean.order;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.web2.vms.VmsConstants;
import com.voyageone.web2.vms.bean.SortParamBean;

import java.util.Date;

import static com.voyageone.web2.vms.VmsConstants.DEFAULT_PAGE_SIZE;

/**
 * 订单检索条件
 * Created by vantis on 16-7-7.
 */
public class OrderSearchInfoBean {
    private String status = VmsConstants.STATUS_VALUE.PRODUCT_STATUS.OPEN;
    private String consolidationOrderId;
    private String sku;
    private int size = DEFAULT_PAGE_SIZE;
    private int curr = 1;
    private SortParamBean sortParamBean;

    private Date orderDateFrom = null;

    private Date orderDateTo = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConsolidationOrderId() {
        return consolidationOrderId;
    }

    public void setConsolidationOrderId(String consolidationOrderId) {
        if (StringUtil.isEmpty(consolidationOrderId)) this.consolidationOrderId = null;
        else this.consolidationOrderId = consolidationOrderId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        if (StringUtil.isEmpty(sku)) this.sku = null;
        else this.sku = sku;
    }

    public Date getOrderDateFrom() {
        return orderDateFrom;
    }

    public void setOrderDateFrom(Date orderDateFrom) {
        this.orderDateFrom = orderDateFrom;
    }

    public Date getOrderDateTo() {
        return orderDateTo;
    }

    public void setOrderDateTo(Date orderDateTo) {
        this.orderDateTo = orderDateTo;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }

    public SortParamBean getSortParamBean() {
        return sortParamBean;
    }

    public void setSortParamBean(SortParamBean sortParamBean) {
        this.sortParamBean = sortParamBean;
    }


}
