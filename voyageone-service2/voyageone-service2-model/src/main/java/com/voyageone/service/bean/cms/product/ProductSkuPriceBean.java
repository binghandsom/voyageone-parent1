package com.voyageone.service.bean.cms.product;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * SKU Price Model
 *
 * @author chuanyu.liang 2015/12/10
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductSkuPriceBean {

    //skuCode
    private String skuCode;
    //msrp价格
    private Double priceMsrp;
    //建议市场价格
    private Double priceRetail;
    //当前销售价格
    private Double priceSale;

    //供应商成本价
    private Double clientNetPrice;
    //供应商MSRP价
    private Double clientMsrpPrice;
    //供应商销售价
    private Double clientRetailPrice;

    // 价格变动符号
    // 由两部分组成: 1. 价格变动标志 2. 差价
    // 价格变动标志: U(涨价) D(降价) X(击穿)
    // 例: U100 就是涨价100元
    // 有可能为空, 空的场合, 说明是初始状态, 没有任何价格变动过
    private String priceChgFlg;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Double getPriceMsrp() {
        return priceMsrp;
    }

    public void setPriceMsrp(Double priceMsrp) {
        this.priceMsrp = priceMsrp;
    }

    public Double getPriceRetail() {
        return priceRetail;
    }

    public void setPriceRetail(Double priceRetail) {
        this.priceRetail = priceRetail;
    }

    public Double getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(Double priceSale) {
        this.priceSale = priceSale;
    }

    public Double getClientNetPrice() {
        return clientNetPrice;
    }

    public void setClientNetPrice(Double clientNetPrice) {
        this.clientNetPrice = clientNetPrice;
    }

    public Double getClientMsrpPrice() {
        return clientMsrpPrice;
    }

    public void setClientMsrpPrice(Double clientMsrpPrice) {
        this.clientMsrpPrice = clientMsrpPrice;
    }

    public Double getClientRetailPrice() {
        return clientRetailPrice;
    }

    public void setClientRetailPrice(Double clientRetailPrice) {
        this.clientRetailPrice = clientRetailPrice;
    }

    public String getPriceChgFlg() {
        return priceChgFlg;
    }

    public void setPriceChgFlg(String priceChgFlg) {
        this.priceChgFlg = priceChgFlg;
    }

    /**
     * 添加一个新的静态工厂方法,简化调用
     * @param sku
     * @return
     */
    public static ProductSkuPriceBean from(CmsBtProductModel_Sku sku) {


        ProductSkuPriceBean bean = new ProductSkuPriceBean();
        try {
            BeanUtils.copyProperties(bean,sku);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
