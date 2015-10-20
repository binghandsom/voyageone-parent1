package com.voyageone.batch.cms.bean;

/**
 * 映射商品更新提交到 CMS 接口的响应
 * Created by Jonas on 10/20/15.
 */
public class WsdlProductUpdateResponse extends WsdlResponseBean {
    /**
     * 数据体信息
     */
    private ProductUpdateResponseBean resultInfo;

    /**
     * @return the resultInfo
     */
    public ProductUpdateResponseBean getResultInfo() {
        return resultInfo;
    }

    /**
     * @param resultInfo the resultInfo to set
     */
    public void setResultInfo(ProductUpdateResponseBean resultInfo) {
        this.resultInfo = resultInfo;
    }
}
