package com.voyageone.service.bean.cms.businessmodel.PromotionProduct;

import java.util.List;

/**
 * Created by dell on 2016/6/27.
 */
public class UpdatePromotionProductTagParameter {
    int id;
   List<ProductTagInfo> tagList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ProductTagInfo> getTagList() {
        return tagList;
    }

    public void setTagList(List<ProductTagInfo> tagList) {
        this.tagList = tagList;
    }


}


