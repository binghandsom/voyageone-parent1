package com.voyageone.components.sears.feed;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 2015/10/21.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(name = "entry", propOrder = { "id", "published", "updated", "title", "productFeedsItems"})
public class ProductFeedsEntryBean {

    private Integer id;
    private XMLGregorianCalendar published;
    private XMLGregorianCalendar updated ;
    private String title;

    @XmlElementWrapper(name = "content")
    @XmlElement(name = "item")
    private List<ProductFeedsItem> productFeedsItems;

    public List<ProductFeedsItem> getProductFeedsItems() {
        if(productFeedsItems == null){
            productFeedsItems = new ArrayList<>();
        }
        return productFeedsItems;
    }

    public void setProductFeedsItems(List<ProductFeedsItem> productFeedsItems) {
        this.productFeedsItems = productFeedsItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public XMLGregorianCalendar getPublished() {
        return published;
    }

    public void setPublished(XMLGregorianCalendar published) {
        this.published = published;
    }

    public XMLGregorianCalendar getUpdated() {
        return updated;
    }

    public void setUpdated(XMLGregorianCalendar updated) {
        this.updated = updated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
