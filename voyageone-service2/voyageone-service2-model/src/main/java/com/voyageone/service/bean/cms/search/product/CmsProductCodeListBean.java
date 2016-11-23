package com.voyageone.service.bean.cms.search.product;

import com.voyageone.common.util.JacksonUtil;

import java.util.List;

/**
 * CmsProductCodeListBean
 *
 * @author chuanyu.liang, 2016/10/09
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsProductCodeListBean {

    private long totalCount;

    private List<String> productCodeList;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<String> getProductCodeList() {
        return productCodeList;
    }

    public void setProductCodeList(List<String> productCodeList) {
        this.productCodeList = productCodeList;
    }

    public String toString() {
        return JacksonUtil.bean2Json(this);
    }
}
