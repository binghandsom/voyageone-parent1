package com.voyageone.service.bean.cms.CmsProductPlatformDetail;

/**
 * Created by dell on 2016/11/18.
 */
public class ProductPrice {

    String status;
    String pStatus;
    String pReallyStatus;
    String pPublishError;
    String numberId;

    //建议零售价
    double priceMsrpSt;
    double priceMsrpEd;

    //指导价
    double priceRetailSt;
    double priceRetailEd;

    //最终售价
    double priceSaleSt;
    double priceSaleEd;
    //平台 id
    int cartId;
    String cartName;


    int checked;//0:未选中 1半选中   2选中

    private String autoSyncPriceMsrp; // channelId and cartId 配置的选项值

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getpStatus() {
        return pStatus;
    }

    public void setpStatus(String pStatus) {
        this.pStatus = pStatus;
    }

    public String getpReallyStatus() {
        return pReallyStatus;
    }

    public void setpReallyStatus(String pReallyStatus) {
        this.pReallyStatus = pReallyStatus;
    }

    public String getpPublishError() {
        return pPublishError;
    }

    public void setpPublishError(String pPublishError) {
        this.pPublishError = pPublishError;
    }

    public String getNumberId() {
        return numberId;
    }

    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }



    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public double getPriceMsrpSt() {
        return priceMsrpSt;
    }

    public void setPriceMsrpSt(double priceMsrpSt) {
        this.priceMsrpSt = priceMsrpSt;
    }

    public double getPriceMsrpEd() {
        return priceMsrpEd;
    }

    public void setPriceMsrpEd(double priceMsrpEd) {
        this.priceMsrpEd = priceMsrpEd;
    }

    public double getPriceRetailSt() {
        return priceRetailSt;
    }

    public void setPriceRetailSt(double priceRetailSt) {
        this.priceRetailSt = priceRetailSt;
    }

    public double getPriceRetailEd() {
        return priceRetailEd;
    }

    public void setPriceRetailEd(double priceRetailEd) {
        this.priceRetailEd = priceRetailEd;
    }

    public double getPriceSaleSt() {
        return priceSaleSt;
    }

    public void setPriceSaleSt(double priceSaleSt) {
        this.priceSaleSt = priceSaleSt;
    }

    public double getPriceSaleEd() {
        return priceSaleEd;
    }

    public void setPriceSaleEd(double priceSaleEd) {
        this.priceSaleEd = priceSaleEd;
    }

    public String getAutoSyncPriceMsrp() {
        return autoSyncPriceMsrp;
    }

    public void setAutoSyncPriceMsrp(String autoSyncPriceMsrp) {
        this.autoSyncPriceMsrp = autoSyncPriceMsrp;
    }
}
