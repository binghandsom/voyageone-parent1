package com.voyageone.service.bean.cms.producttop;

import com.voyageone.common.CmsConstants;

public class ProductInfo {
    String image1;                  //图片
    String pNumIId;
    String model;                   //款号
    String productName;             //商品名称
    String brand;                   //品牌
    String code;                    //商品编码
    double pPriceSaleSt;            //中国最终售价  最小
    double pPriceSaleEd;            //中国最终售价  最大
    int quantity;                   //库存
    int skuCount;                   //sku数
    String created;                 //创建时间
    int salesSum7;                  //7天销量
    int salesSum30;                 //30天销量
    int salesSumYear;               //年销量
    int salesSumAll;                //总销量

    //新增字段
    String status;                  //cms状态
    String pStatus;                 //平台状态
    String pReallyStatus;           //真实平台状态
    int isMain;                  //是否为主商品
    String pPublishError;           // 上新错误

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getpNumIId() {
        return pNumIId;
    }

    public void setpNumIId(String pNumIId) {
        this.pNumIId = pNumIId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getpPriceSaleSt() {
        return pPriceSaleSt;
    }

    public void setpPriceSaleSt(double pPriceSaleSt) {
        this.pPriceSaleSt = pPriceSaleSt;
    }

    public double getpPriceSaleEd() {
        return pPriceSaleEd;
    }

    public void setpPriceSaleEd(double pPriceSaleEd) {
        this.pPriceSaleEd = pPriceSaleEd;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSkuCount() {
        return skuCount;
    }

    public void setSkuCount(int skuCount) {
        this.skuCount = skuCount;
    }

    public int getSalesSumYear() {
        return salesSumYear;
    }

    public void setSalesSumYear(int salesSumYear) {
        this.salesSumYear = salesSumYear;
    }

    public int getSalesSumAll() {
        return salesSumAll;
    }

    public void setSalesSumAll(int salesSumAll) {
        this.salesSumAll = salesSumAll;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getSalesSum7() {
        return salesSum7;
    }

    public void setSalesSum7(int salesSum7) {
        this.salesSum7 = salesSum7;
    }

    public int getSalesSum30() {
        return salesSum30;
    }

    public void setSalesSum30(int salesSum30) {
        this.salesSum30 = salesSum30;
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

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
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
}
