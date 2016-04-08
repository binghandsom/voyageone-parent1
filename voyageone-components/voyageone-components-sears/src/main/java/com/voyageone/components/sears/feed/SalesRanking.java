package com.voyageone.components.sears.feed;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by james on 2015/10/21.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class SalesRanking {
    private Integer verticalRanking;
    private Integer categoryRanking;
    private Integer subCategoryRanking;

    public Integer getVerticalRanking() {
        return verticalRanking;
    }

    public void setVerticalRanking(Integer verticalRanking) {
        this.verticalRanking = verticalRanking;
    }

    public Integer getCategoryRanking() {
        return categoryRanking;
    }

    public void setCategoryRanking(Integer categoryRanking) {
        this.categoryRanking = categoryRanking;
    }

    public Integer getSubCategoryRanking() {
        return subCategoryRanking;
    }

    public void setSubCategoryRanking(Integer subCategoryRanking) {
        this.subCategoryRanking = subCategoryRanking;
    }
}
