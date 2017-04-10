package com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/11/1.
 */
public class InitParameter {

    Date activityStart;//activityStart activityEnd
    Date activityEnd;
    int cartId;
    int isSelAll;
    Map<String, Object> searchInfo;
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

    public Date getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(Date activityStart) {
        this.activityStart = activityStart;
    }

    public Date getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(Date activityEnd) {
        this.activityEnd = activityEnd;
    }

    public Map<String, Object> getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(Map<String, Object> searchInfo) {
        this.searchInfo = searchInfo;
    }
}
