package com.voyageone.task2.cms.bean;
import java.util.List;

/**
 * ProductUpdateDetailBean
 * 用于返回 ProductUpdate webservice请求的结果数据
 * Created by zero on 9/9/2015.
 *
 * @author zero
 */

public class ProductUpdateResponseBean {

    /**
     * 成功部分
     */
    private List<ProductUpdateDetailBean> success;

    /**
     * 失败部分
     */
    private List<ProductUpdateDetailBean> failure;

    /**
     * @return the success
     */
    public List<ProductUpdateDetailBean> getSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(List<ProductUpdateDetailBean> success) {
        this.success = success;
    }

    /**
     * @return the failure
     */
    public List<ProductUpdateDetailBean> getFailure() {
        return failure;
    }

    /**
     * @param failure the failure to set
     */
    public void setFailure(List<ProductUpdateDetailBean> failure) {
        this.failure = failure;
    }

}
