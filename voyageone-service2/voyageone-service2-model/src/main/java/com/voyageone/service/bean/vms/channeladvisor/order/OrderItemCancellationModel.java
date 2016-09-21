package com.voyageone.service.bean.vms.channeladvisor.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.service.bean.vms.channeladvisor.CABaseModel;
import com.voyageone.service.bean.vms.channeladvisor.enums.CancellationReasonEnum;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OrderItemCancellationModel extends CABaseModel {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("SellerSku")
    private String sellerSku;

    @JsonProperty("Quantity")
    private Integer quantity;

    @JsonProperty("Reason")
    private CancellationReasonEnum reason;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSellerSku() {
        return sellerSku;
    }

    public void setSellerSku(String sellerSku) {
        this.sellerSku = sellerSku;
    }

    public CancellationReasonEnum getReason() {
        return reason;
    }

    public void setReason(CancellationReasonEnum reason) {
        this.reason = reason;
    }
}
