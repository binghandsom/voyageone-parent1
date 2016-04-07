package com.voyageone.components.sears.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/21.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name ="categories")
public class CategoryResponse {

    @XmlElement(name = "category")
    private List<CategoryBean> categoryBean;

    public List<CategoryBean> getCategoryBean() {
        if(categoryBean == null){
            categoryBean = new ArrayList<>();
        }
        return categoryBean;
    }

    public void setCategoryBean(List<CategoryBean> categoryBean) {
        this.categoryBean = categoryBean;
    }
}
