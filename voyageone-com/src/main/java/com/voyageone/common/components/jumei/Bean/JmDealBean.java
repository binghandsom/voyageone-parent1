package com.voyageone.common.components.jumei.Bean;

import org.springframework.util.Assert;

/**
 * @author aooer 2016/1/28.
 * @version 2.0.0
 * @since 2.0.0
 */
public class JmDealBean extends JmBaseBean {

    /**
     * productId 商品Id
     * */
    private Integer productId;

    /**
     *  需要查询的字段.；可选字段见“返回字段”列表
     *  默认值: product_id,categorys,brand_id,brand_name,name,foreign_language_name
     *  参数范围: 多个参数以","隔开，不存在的字段将被忽略
     * */
    private String fields;

    public void check() throws Exception {
        Assert.notNull(productId,"product must not null!");
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}
