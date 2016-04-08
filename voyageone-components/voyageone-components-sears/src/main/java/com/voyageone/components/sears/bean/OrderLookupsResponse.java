package com.voyageone.components.sears.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by james.li on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name ="orders")
public class OrderLookupsResponse {

    private List<OrderLookupResponse> order;

    public List<OrderLookupResponse> getOrder() {
        return order;
    }

    public void setOrder(List<OrderLookupResponse> order) {
        this.order = order;
    }
}
