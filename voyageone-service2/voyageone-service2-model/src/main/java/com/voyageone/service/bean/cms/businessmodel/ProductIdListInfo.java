package com.voyageone.service.bean.cms.businessmodel;
import java.io.Serializable;
import java.util.List;
public class ProductIdListInfo  implements Serializable {
    int promotionId;
    List<Integer> productIdList;
    List<Integer> productCodeList;
    public List<Integer> getProductCodeList() {
        return productCodeList;
    }
    public void setProductCodeList(List<Integer> productCodeList) {
        this.productCodeList = productCodeList;
    }
    public List<Integer> getProductIdList() {
        return productIdList;
    }
    public void setProductIdList(List<Integer> productIdList) {
        this.productIdList = productIdList;
    }
    public int getPromotionId() {
        return promotionId;
    }
    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }
}
