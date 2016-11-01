package com.voyageone.service.bean.cms.businessmodel.CmsAdvanceSearch;

import java.util.List;

/**
 * Created by dell on 2016/11/1.
 */
public class AddProductToPromotionParameter {

    int cartId;
    int isSelAll;
    List<String> codeList;
    public int getIsSelAll() {
        return isSelAll;
    }

    public void setIsSelAll(int isSelAll) {
        this.isSelAll = isSelAll;
    }
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

}
