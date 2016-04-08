package com.voyageone.components.sears.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Administrator on 2015/10/22.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PriceBean {
    private Double sellPrice;
    private Double intlRegPrice;

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Double getIntlRegPrice() {
        return intlRegPrice;
    }

    public void setIntlRegPrice(Double intlRegPrice) {
        this.intlRegPrice = intlRegPrice;
    }
}
