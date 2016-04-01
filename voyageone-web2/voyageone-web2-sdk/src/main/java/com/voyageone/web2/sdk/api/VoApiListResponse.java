package com.voyageone.web2.sdk.api;


/**
 * Respose Entity
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class VoApiListResponse extends VoApiResponse {

    /**
     * 结果总数
     */
    protected Long totalCount;

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
