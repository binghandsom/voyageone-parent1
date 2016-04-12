package com.voyageone.components.sears.feed;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 2015/10/21.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "feed")
@XmlType(name = "feed", propOrder = { "id", "title", "updated","entry" })
public class ProductPriceResponse {


    private String id;
    private String title;
    private XMLGregorianCalendar updated;
    private List<ProductPriceEntryBean> entry;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public XMLGregorianCalendar getUpdated() {
        return updated;
    }

    public void setUpdated(XMLGregorianCalendar updated) {
        this.updated = updated;
    }

    public List<ProductPriceEntryBean> getEntry() {
        if(entry == null ){
            entry = new ArrayList<>();
        }
        return entry;
    }

    public void setEntry(List<ProductPriceEntryBean> entry) {
        this.entry = entry;
    }
}
