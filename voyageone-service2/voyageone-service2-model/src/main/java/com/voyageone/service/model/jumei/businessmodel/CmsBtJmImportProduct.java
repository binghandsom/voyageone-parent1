package com.voyageone.service.model.jumei.businessmodel;
import com.voyageone.service.model.jumei.CmsBtJmProductModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
public class CmsBtJmImportProduct extends CmsBtJmProductModel {
    String appId;
    String pcId;
    int limit;
    String propertyImage;
    String productImageUrlKey1;
    String productImageUrlKey2;
    String productImageUrlKey3;
    String productImageUrlKey4;
    String productImageUrlKey5;
    String productImageUrlKey6;

    public String getSpecialNote() {
        return specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    String specialNote;
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPcId() {
        return pcId;
    }

    public void setPcId(String pcId) {
        this.pcId = pcId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getPropertyImage() {
        return propertyImage;
    }

    public void setPropertyImage(String propertyImage) {
        this.propertyImage = propertyImage;
    }

    public String getProductImageUrlKey1() {
        return productImageUrlKey1;
    }

    public void setProductImageUrlKey1(String productImageUrlKey1) {
        this.productImageUrlKey1 = productImageUrlKey1;
    }

    public String getProductImageUrlKey2() {
        return productImageUrlKey2;
    }

    public void setProductImageUrlKey2(String productImageUrlKey2) {
        this.productImageUrlKey2 = productImageUrlKey2;
    }

    public String getProductImageUrlKey3() {
        return productImageUrlKey3;
    }

    public void setProductImageUrlKey3(String productImageUrlKey3) {
        this.productImageUrlKey3 = productImageUrlKey3;
    }

    public String getProductImageUrlKey4() {
        return productImageUrlKey4;
    }

    public void setProductImageUrlKey4(String productImageUrlKey4) {
        this.productImageUrlKey4 = productImageUrlKey4;
    }

    public String getProductImageUrlKey5() {
        return productImageUrlKey5;
    }

    public void setProductImageUrlKey5(String productImageUrlKey5) {
        this.productImageUrlKey5 = productImageUrlKey5;
    }

    public String getProductImageUrlKey6() {
        return productImageUrlKey6;
    }

    public void setProductImageUrlKey6(String productImageUrlKey6) {
        this.productImageUrlKey6 = productImageUrlKey6;
    }
}