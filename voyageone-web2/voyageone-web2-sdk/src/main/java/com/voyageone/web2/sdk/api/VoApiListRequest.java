package com.voyageone.web2.sdk.api;

/**
 * TOP请求接口。
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public abstract class VoApiListRequest<T extends VoApiResponse> extends VoApiRequest {

    /**
     * 页码.传入值为1代表第一页,传入值为2代表第二页,依此类推.默认返回的数据是从第一页开始.
     */
    protected Integer pageNo;

    /**
     * 每页条数.每页返回最多返回100条,默认值为40
     */
    protected Integer pageSize;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
