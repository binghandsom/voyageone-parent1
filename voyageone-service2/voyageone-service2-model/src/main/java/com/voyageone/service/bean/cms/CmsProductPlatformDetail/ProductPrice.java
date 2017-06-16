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
    double priceRetail;

    //最终售价
    double priceSaleSt;
    double priceSaleEd;
    //平台 id
    int cartId;
    String cartName;
    //客户建议零售价
    String clientMsrpPrice;
    //客户成本价
    String clientNetPrice;
    int checked;//0:未选中 1半选中   2选中
    private String autoSyncPriceMsrp; // channelId and cartId 中国建议售价配置的选项值
    private String autoSyncPriceSale; // channelId and cartId 中国最终售价配置的选项值

    private Integer quantity;

    public String getClientMsrpPrice() {
        return clientMsrpPrice;
    }

    public void setClientMsrpPrice(String clientMsrpPrice) {
        this.clientMsrpPrice = clientMsrpPrice;
    }

    public String getClientNetPrice() {
        return clientNetPrice;
    }

    public void setClientNetPrice(String clientNetPrice) {
        this.clientNetPrice = clientNetPrice;
    }

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

    public double getPriceRetail() {
        return priceRetail;
    }

    public void setPriceRetail(double priceRetail) {
        this.priceRetail = priceRetail;
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

    public String getAutoSyncPriceSale() {
        return autoSyncPriceSale;
    }

    public void setAutoSyncPriceSale(String autoSyncPriceSale) {
        this.autoSyncPriceSale = autoSyncPriceSale;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
