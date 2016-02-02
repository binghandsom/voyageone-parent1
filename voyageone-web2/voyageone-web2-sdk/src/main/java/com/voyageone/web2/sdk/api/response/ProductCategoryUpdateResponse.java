package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiUpdateResponse;

/**
 * @author aooer 2016/1/21.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductCategoryUpdateResponse extends VoApiUpdateResponse {

    private int updFeedInfoCount;

    private int updProductCount;

    public int getUpdFeedInfoCount() {
        return updFeedInfoCount;
    }

    public void setUpdFeedInfoCount(int updFeedInfoCount) {
        this.updFeedInfoCount = updFeedInfoCount;
    }

    public int getUpdProductCount() {
        return updProductCount;
    }

    public void setUpdProductCount(int updProductCount) {
        this.updProductCount = updProductCount;
    }
}
