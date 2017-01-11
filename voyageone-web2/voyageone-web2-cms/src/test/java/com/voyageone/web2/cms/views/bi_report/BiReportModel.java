package com.voyageone.web2.cms.views.bi_report;

/**
 * Created by dell on 2017/1/11.
 */
public class BiReportModel {
    private Double goodsId;
    private String goodsName;
    private String goodsCode;
    private Integer id;
    private String skuName;
    private  Integer sale_number;
    private Float sale_account;
    private Integer buier_number;
    private Float buerAvaItem;
    private Float cusPrice;
    private Integer PV;
    private Integer UV ;
    private Float tranRate;
    private Integer addFav;
    private Float pageStayTime;
    private Float loseRate;
    private Float visitorValue;
    private String category;
    private String brand;
    private String color;
    private String origin;
    private String material;
    private Integer weight;
    private Integer size;

    public BiReportModel(
            Double goodsId,
            String goodsName,
            String goodsCode,
            Integer id,
             String skuName,
              Integer sale_number,
             Float sale_account,
             Integer buier_number,
             Float buerAvaItem,
             Float cusPrice,
             Integer PV,
             Integer UV ,
             Float tranRate,
             Integer addFav,
             Float pageStayTime,
             Float loseRate,
             Float visitorValue,
             String category,
             String brand,
             String color,
             String origin,
             String material,
             Integer weight,
             Integer size)
    {
        this.goodsCode=goodsCode;
        this.goodsId=goodsId;
        this.goodsName=goodsName;
        this.skuName=skuName;
        this.id=id;
        this.skuName=skuName;
        this.sale_number=sale_number;
        this. sale_account=sale_account;
        this. buier_number=buier_number;
        this. buerAvaItem=buerAvaItem;
        this. cusPrice=cusPrice;
        this. PV=PV;
        this. UV =UV;
        this. tranRate=tranRate;
        this. addFav=addFav;
        this. pageStayTime=pageStayTime;
        this.loseRate=loseRate;
        this. visitorValue=visitorValue;
        this. category=category;
        this. brand=brand;
        this. color=color;
        this. origin=origin;
        this. material=material;
        this. weight=weight;
        this. size=size;
    }

    public Double getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Double goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public Float getBuerAvaItem() {
        return buerAvaItem;
    }

    public void setBuerAvaItem(Float buerAvaItem) {
        this.buerAvaItem = buerAvaItem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getSale_number() {
        return sale_number;
    }

    public void setSale_number(Integer sale_number) {
        this.sale_number = sale_number;
    }

    public Float getSale_account() {
        return sale_account;
    }

    public void setSale_account(Float sale_account) {
        this.sale_account = sale_account;
    }

    public Integer getBuier_number() {
        return buier_number;
    }

    public void setBuier_number(Integer buier_number) {
        this.buier_number = buier_number;
    }

    public Float getbuerAvaItem() {
        return buerAvaItem;
    }

    public void setbuerAvaItem(Float buerAvaItem) {
        this.buerAvaItem = buerAvaItem;
    }

    public Float getCusPrice() {
        return cusPrice;
    }

    public void setCusPrice(Float cusPrice) {
        this.cusPrice = cusPrice;
    }

    public Integer getPV() {
        return PV;
    }

    public void setPV(Integer PV) {
        this.PV = PV;
    }

    public Integer getUV() {
        return UV;
    }

    public void setUV(Integer UV) {
        this.UV = UV;
    }

    public Float getTranRate() {
        return tranRate;
    }

    public void setTranRate(Float tranRate) {
        this.tranRate = tranRate;
    }

    public Integer getAddFav() {
        return addFav;
    }

    public void setAddFav(Integer addFav) {
        this.addFav = addFav;
    }

    public Float getPageStayTime() {
        return pageStayTime;
    }

    public void setPageStayTime(Float pageStayTime) {
        this.pageStayTime = pageStayTime;
    }

    public Float getLoseRate() {
        return loseRate;
    }

    public void setLoseRate(Float loseRate) {
        this.loseRate = loseRate;
    }

    public Float getVisitorValue() {
        return visitorValue;
    }

    public void setVisitorValue(Float visitorValue) {
        this.visitorValue = visitorValue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
