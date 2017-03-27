package com.voyageone.components.solr.bean;

import java.util.List;

/**
 * Created by james on 2017/3/21.
 */
public class CmsProductSearchPlatformModel {
    String pCatId;
    String pCatPath;
    Integer pIsMain;
    String pNumIId;
    String pStatus;
    String pReallyStatus;
    String pPlatformMallId;
    Integer sale7;
    Integer sale30;
    Integer saleYear;
    Integer saleAll;
    List<String> priceDiffFlg;
    List<String> priceChgFlg;
    List<String> priceMsrpFlg;
    String status;
    Double pPriceMsrpEd;
    Double pPriceRetailEd;
    Double pPriceSaleEd;


    public String getpCatId() {
        return pCatId;
    }

    public void setpCatId(String pCatId) {
        this.pCatId = pCatId;
    }

    public String getpCatPath() {
        return pCatPath;
    }

    public void setpCatPath(String pCatPath) {
        this.pCatPath = pCatPath;
    }

    public Integer getpIsMain() {
        return pIsMain;
    }

    public void setpIsMain(Integer pIsMain) {
        this.pIsMain = pIsMain;
    }

    public String getpNumIId() {
        return pNumIId;
    }

    public void setpNumIId(String pNumIId) {
        this.pNumIId = pNumIId;
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

    public Integer getSale7() {
        return sale7;
    }

    public void setSale7(Integer sale7) {
        this.sale7 = sale7;
    }

    public Integer getSale30() {
        return sale30;
    }

    public void setSale30(Integer sale30) {
        this.sale30 = sale30;
    }

    public Integer getSaleYear() {
        return saleYear;
    }

    public void setSaleYear(Integer saleYear) {
        this.saleYear = saleYear;
    }

    public Integer getSaleAll() {
        return saleAll;
    }

    public void setSaleAll(Integer saleAll) {
        this.saleAll = saleAll;
    }

    public String getpPlatformMallId() {
        return pPlatformMallId;
    }

    public void setpPlatformMallId(String pPlatformMallId) {
        this.pPlatformMallId = pPlatformMallId;
    }

    public List<String> getPriceDiffFlg() {
        return priceDiffFlg;
    }

    public void setPriceDiffFlg(List<String> priceDiffFlg) {
        this.priceDiffFlg = priceDiffFlg;
    }

    public List<String> getPriceChgFlg() {
        return priceChgFlg;
    }

    public void setPriceChgFlg(List<String> priceChgFlg) {
        this.priceChgFlg = priceChgFlg;
    }

    public List<String> getPriceMsrpFlg() {
        return priceMsrpFlg;
    }

    public void setPriceMsrpFlg(List<String> priceMsrpFlg) {
        this.priceMsrpFlg = priceMsrpFlg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getpPriceMsrpEd() {
        return pPriceMsrpEd;
    }

    public void setpPriceMsrpEd(Double pPriceMsrpEd) {
        this.pPriceMsrpEd = pPriceMsrpEd;
    }

    public Double getpPriceRetailEd() {
        return pPriceRetailEd;
    }

    public void setpPriceRetailEd(Double pPriceRetailEd) {
        this.pPriceRetailEd = pPriceRetailEd;
    }

    public Double getpPriceSaleEd() {
        return pPriceSaleEd;
    }

    public void setpPriceSaleEd(Double pPriceSaleEd) {
        this.pPriceSaleEd = pPriceSaleEd;
    }
}
