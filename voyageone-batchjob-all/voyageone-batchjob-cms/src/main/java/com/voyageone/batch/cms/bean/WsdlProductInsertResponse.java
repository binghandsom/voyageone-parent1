package com.voyageone.batch.cms.bean;

/**
 * 映射新商品提交到 CMS 接口的响应
 * Created by Jonas on 10/20/15.
 */
public class WsdlProductInsertResponse extends WsdlResponseBean {
    /**
     * 数据体信息
     */
    private ProductFeedResponseBean resultInfo;

    /**
     * @return the resultInfo
     */
    public ProductFeedResponseBean getResultInfo() {
        return resultInfo;
    }

    /**
     * @param resultInfo the resultInfo to set
     */
    public void setResultInfo(ProductFeedResponseBean resultInfo) {
        this.resultInfo = resultInfo;
    }
}
