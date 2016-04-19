package com.voyageone.service.model.jumei.businessmodel;

/**
 * Created by dell on 2016/4/15.
 */
//{"jmShippingStock":"2813","releaseDate":"11","productionArea":"22"
//        ,"suitCrowds":"33","whiteSquareModel":"44","productReallyModel":"55","productUprightModel":"66","SpecialNote":"77"}
public class JMDefaultSet {
      //聚美仓库id
       int  jmShippingStock;
        String releaseDate;
        String productionArea;
        String suitCrowds;
        String whiteSquareModel;
        String productReallyModel;
        String productUprightModel;
        String  specialNote;

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getProductionArea() {
        return productionArea;
    }

    public void setProductionArea(String productionArea) {
        this.productionArea = productionArea;
    }

    public String getSuitCrowds() {
        return suitCrowds;
    }

    public void setSuitCrowds(String suitCrowds) {
        this.suitCrowds = suitCrowds;
    }

    public String getWhiteSquareModel() {
        return whiteSquareModel;
    }

    public void setWhiteSquareModel(String whiteSquareModel) {
        this.whiteSquareModel = whiteSquareModel;
    }

    public String getProductReallyModel() {
        return productReallyModel;
    }

    public void setProductReallyModel(String productReallyModel) {
        this.productReallyModel = productReallyModel;
    }

    public String getProductUprightModel() {
        return productUprightModel;
    }

    public void setProductUprightModel(String productUprightModel) {
        this.productUprightModel = productUprightModel;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public int getJmShippingStock() {
        return jmShippingStock;
    }

    public void setJmShippingStock(int jmShippingStock) {
        this.jmShippingStock = jmShippingStock;
    }
}
