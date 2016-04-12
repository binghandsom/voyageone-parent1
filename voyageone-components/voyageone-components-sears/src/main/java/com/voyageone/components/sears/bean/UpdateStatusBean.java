package com.voyageone.components.sears.bean;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by jack.zhao on 2015/12/01.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name ="order")
public class UpdateStatusBean {

    private String orderId;
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<UpdateStatusItem> items;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<UpdateStatusItem> getItems() {
        return items;
    }

    public void setItems(List<UpdateStatusItem> items) {
        this.items = items;
    }
}
