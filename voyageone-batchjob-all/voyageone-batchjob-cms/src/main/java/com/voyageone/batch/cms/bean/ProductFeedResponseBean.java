package com.voyageone.batch.cms.bean;
import java.util.List;

/**
 * ProductFeedResponseBean
 * Created by zero on 7/28/2015.
 *
 * @author zero
 */

public class ProductFeedResponseBean {

    /**
     * 成功部分
     */
    private List<ProductFeedDetailBean> success;

    /**
     * 失败部分
     */
    private List<ProductFeedDetailBean> failure;

    /**
     * @return the success
     */
    public List<ProductFeedDetailBean> getSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(List<ProductFeedDetailBean> success) {
        this.success = success;
    }

    /**
     * @return the failure
     */
    public List<ProductFeedDetailBean> getFailure() {
        return failure;
    }

    /**
     * @param failure the failure to set
     */
    public void setFailure(List<ProductFeedDetailBean> failure) {
        this.failure = failure;
    }
}
