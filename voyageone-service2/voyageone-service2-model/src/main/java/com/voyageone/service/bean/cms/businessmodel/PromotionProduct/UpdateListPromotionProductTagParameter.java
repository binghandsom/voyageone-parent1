package com.voyageone.service.bean.cms.businessmodel.PromotionProduct;

import java.util.List;

/**
 * Created by dell on 2016/6/27.
 */
public class UpdateListPromotionProductTagParameter {
    List<Integer> listPromotionProductId;

    public List<Integer> getListPromotionProductId() {
        return listPromotionProductId;
    }

    public void setListPromotionProductId(List<Integer> listPromotionProductId) {
        this.listPromotionProductId = listPromotionProductId;
    }

    List<ProductTagInfo> tagList;

    public List<ProductTagInfo> getTagList() {
        return tagList;
    }

    public void setTagList(List<ProductTagInfo> tagList) {
        this.tagList = tagList;
    }


}


