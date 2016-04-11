package com.voyageone.web2.sdk.api;

import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * TOP请求接口。
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class VoApiListRequest<T extends VoApiResponse> extends VoApiRequest<T> {

    /**
     * 是否分页
     */
    protected boolean isPage = true;

    /**
     * 页码.传入值为1代表第一页,传入值为2代表第二页,依此类推.默认返回的数据是从第一页开始.
     */
    protected int pageNo = 1;

    /**
     * 每页条数.每页返回最多返回100条,默认值为40
     */
    protected int pageSize = 40;

    public boolean getIsPage() {
        return isPage;
    }

    public void setIsPage(boolean isPage) {
        this.isPage = isPage;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void check() throws ApiRuleException {
        super.check();

        RequestUtils.checkMinValue((long) pageSize, 1, "pageSize");
        // TODO 因为下载不能只限制取100条数据
//        RequestUtils.checkMaxValue((long) pageSize, 100, "pageSize");

        RequestUtils.checkMinValue((long) pageNo, 1, "pageNo");
    }

}
