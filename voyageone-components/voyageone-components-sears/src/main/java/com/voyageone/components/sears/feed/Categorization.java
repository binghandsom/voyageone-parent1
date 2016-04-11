package com.voyageone.components.sears.feed;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by james on 2015/10/21.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Categorization {

    private String id;
    private String vertical;
    private String category;
    private String subcategory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVertical() {
        return vertical;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    @Override
    public String toString(){
//        return String.format("%s-%s-%s",vertical,category,subcategory).toLowerCase().replaceAll(" ", "").replaceAll("'","");
        String str = String.format("%s-%s-%s",vertical,category,subcategory);
//        str = str.replace(" & ","-");
        return str;
    }
}
