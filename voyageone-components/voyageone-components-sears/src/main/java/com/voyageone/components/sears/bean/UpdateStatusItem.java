package com.voyageone.components.sears.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jack.zhao on 2015/12/01.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class UpdateStatusItem {

    private String itemId;
    private String status;
    private String statusUpdatedAt;
    private String comment;
    private String internationalTrackingNumber;
    private String quantity;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusUpdatedAt() {
        return statusUpdatedAt;
    }

    public void setStatusUpdatedAt(String statusUpdatedAt) {
        this.statusUpdatedAt = statusUpdatedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getInternationalTrackingNumber() {
        return internationalTrackingNumber;
    }

    public void setInternationalTrackingNumber(String internationalTrackingNumber) {
        this.internationalTrackingNumber = internationalTrackingNumber;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
