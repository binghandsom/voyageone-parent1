package com.voyageone.service.model.report;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dell on 2017/1/16.
 */
public class ShopSalesOfChannel010Model extends VmShopModel{
    private String nameCn; //1 店铺名称
    private Date date;          //2 日期
    private BigDecimal amt;        //3 销售额
    private Integer qty;          //4销售量
    private BigDecimal yamt ;               //5 yesterday销售环比
    private BigDecimal yamtRate;      //6 销售额环比增幅
    private BigDecimal lyamt;        //7 this day in last year  同比增幅
    private BigDecimal lyamtRate;   //8
    private BigDecimal YTD;          //9 last year to today
    private BigDecimal YTDFinishRate ;     //10
    private BigDecimal MTD;        //11  LAST MONTH TO TODAY
    private BigDecimal WTD;         //12 last week to today
    private Integer uv;          //13
    private Integer pv;            //14
    private Integer buyers;//15
    private Integer orders;//16
    private BigDecimal tranRate; //17 transfer_rate;
    private BigDecimal customerAverageAmt;//18;
    private Integer addFavortite;//19粉絲數
    private Integer uvRegular;//20
    //goods data
    private Integer onSale;//21
    private Integer inStock;//22
    private BigDecimal salesRate ;//23动销率
    //express data
    private BigDecimal shippingRate ;//24 发货率
    private BigDecimal directExpressRate;//25直邮订单比
    private BigDecimal notExpress72hRate; //26 72小时未发货率;
    //service data
    private BigDecimal dsrService;//27 服务动态评分;
    private BigDecimal dsrDescribe; //28 描述评分
    private BigDecimal dsrExpress; //29 物流评分
    private BigDecimal  responseRate48h ;//30 48 小时回复率
    private BigDecimal  returnDurationRanking;//31 退款完结时长排名
    private BigDecimal returnQualityRanking ;//32  品质退款率排名
    private BigDecimal returnOrders  ;//33 退款率
    private BigDecimal returnDisputesRate;//34  退款纠纷率
    private BigDecimal conversionRate    ;//35   询单转化率


    public ShopSalesOfChannel010Model(
              String nameCn, //1 店铺名称
              Date date,          //2 日期
              BigDecimal amt,        //3 销售额
              Integer qty,          //4销售量
              BigDecimal yamt,             //5 yesterday销售环比
              BigDecimal yamtRate,      //6 销售额环比增幅
              BigDecimal lyamt,       //7 this day in last year  同比增幅
              BigDecimal lyamtRate,   //8
              BigDecimal YTD,          //9 last year to today
              BigDecimal YTDFinishRate ,     //10
              BigDecimal MTD,        //11  LAST MONTH TO TODAY
              BigDecimal WTD,         //12 last week to today
              Integer uv,          //13
              Integer pv,            //14
              Integer buyers,//15
              Integer orders,//16
              BigDecimal tranRate, //17 transfer_rate,
              BigDecimal customerAverageAmt,//18,
              Integer addFavortite,//19
              Integer uvRegular,//20
            //goods data
              Integer onSale,//21
              Integer inStock,//22
              BigDecimal salesRate ,//23动销率
            //express data
              BigDecimal shippingRate ,//24发货率
              BigDecimal directExpressRate,//25直邮订单比
              BigDecimal notExpress72hRate, //26 72小时未发货率,
            //service data
              BigDecimal dsrService,//27 服务动态评分,
              BigDecimal dsrDescribe, //28 描述评分
              BigDecimal dsrExpress, //29 物流评分
              BigDecimal  responseRate48h ,//30 48 小时回复率
              BigDecimal  returnDurationRanking,//31 退款完结时长排名
              BigDecimal returnQualityRanking ,//32  品质退款率排名
              BigDecimal returnOrders   ,//33 退款率
              BigDecimal returnDisputesRate,//34  退款纠纷率
              BigDecimal conversionRate    //35   询单转化率
    )
    {
        this.nameCn=nameCn; //1 店铺名称
        this.date=date;          //2 日期
        this. amt=amt;        //3 销售额
        this.  qty=qty;          //4销售量
        this.  yamt=yamt;               //5 yesterday销售环比
        this.  yamtRate=yamtRate;      //6 销售额环比增幅
        this.  lyamt=lyamt;        //7 this day in last year  同比增幅
        this.  lyamtRate=lyamtRate;   //8
        this.  YTD=YTD;          //9 last year to today
        this.  YTDFinishRate=YTDFinishRate ;     //10
        this.  MTD=MTD;        //11  LAST MONTH TO TODAY
        this.  WTD=WTD;         //12 last week to today
        this.  uv=uv;          //13
        this.  pv=pv;            //14
        this.  buyers=buyers;//15ge
        this.  orders=orders;//16
        this.  tranRate=tranRate; //17 transfer_rate;
        this.  customerAverageAmt=customerAverageAmt;//18;
        this.  addFavortite=addFavortite;//19
        this.  uvRegular=uvRegular;//20
        //goods data
        this.  onSale=onSale;//21
        this.  inStock=inStock;//22
        this.  salesRate=salesRate ;//23动销率
        //express data
        this.  shippingRate =shippingRate;//24发货率
        this.  directExpressRate=directExpressRate;//25直邮订单比
        this.  notExpress72hRate=notExpress72hRate; //26 72小时未发货率;
        //service data
        this.  dsrService=dsrService;//27 服务动态评分;
        this.  dsrDescribe=dsrDescribe; //28 描述评分
        this.  dsrExpress=dsrExpress; //29 物流评分
        this.   responseRate48h=responseRate48h ;//30 48 小时回复率
        this.   returnDurationRanking=returnDurationRanking;//31 退款完结时长排名
        this.  returnQualityRanking=returnQualityRanking ;//32  品质退款率排名
        this.  returnOrders=returnOrders   ;//33 退款率
        this.  returnDisputesRate=returnDisputesRate;//34  退款纠纷率
        this.  conversionRate=conversionRate    ;//35   询单转化率
    }

    public ShopSalesOfChannel010Model(BiReportSalesShop010Model bssm)
    {
        this.date=bssm.getDate();          //2 日期
        this. amt=bssm.getAmt();        //3 销售额
        this.  qty=bssm.getQty();          //4销售量
        this.  uv=bssm.getUv();          //13
        this.  pv=bssm.getPv();            //14
        this.  buyers=bssm.getBuyers();//15
        this.  orders=bssm.getOrders();//16
        this.  addFavortite=bssm.getAddFavortite();//19
        this.  uvRegular=bssm.getUvRegular();//20
        //goods data
        this.  onSale=bssm.getOnSale();//21
        this.  inStock=bssm.getInStock();//22
        //express data
//        this.  shippingRate =bssm.shi;//24 发货率
//        this.directExpressRate=bssm.getDirectExpress();//25直邮订单比
//        this.notExpress72hRate=bssm.getNotExpress72h(); //26 72小时未发货率;
        //service data
        this.  dsrService=bssm.getDsrService();//27 服务动态评分;
        this.  dsrDescribe=bssm.getDsrDescribe(); //28 描述评分
        this.  dsrExpress=bssm.getDsrExpress(); //29 物流评分
        this.   responseRate48h=bssm.getResponseRate48h() ;//30 48 小时回复率
        this.   returnDurationRanking=bssm.getReturnDurationRanking();//31 退款完结时长排名
        this.  returnQualityRanking=bssm.getReturnQualityRanking() ;//32  品质退款率排名
        this.  returnOrders=bssm.getreturnOreders()   ;//33 退款率
        this.  returnDisputesRate=bssm.getReturnDisputesRate();//34  退款纠纷率
//        this.  conversionRate=bssm.    ;//35   询单转化率
    }

    public ShopSalesOfChannel010Model() {}

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public BigDecimal getYamt() {
        return yamt;
    }

    public void setYamt(BigDecimal yamt) {
        this.yamt = yamt;
    }

    public BigDecimal getYamtRate() {
        return yamtRate;
    }

    public void setYamtRate(BigDecimal yamtRate) {
        this.yamtRate = yamtRate;
    }

    public BigDecimal getLyamt() {
        return lyamt;
    }

    public void setLyamt(BigDecimal lyamt) {
        this.lyamt = lyamt;
    }

    public BigDecimal getLyamtRate() {
        return lyamtRate;
    }

    public void setLyamtRate(BigDecimal lyamtRate) {
        this.lyamtRate = lyamtRate;
    }

    public BigDecimal getYTD() {
        return YTD;
    }

    public void setYTD(BigDecimal YTD) {
        this.YTD = YTD;
    }

    public BigDecimal getYTDFinishRate() {
        return YTDFinishRate;
    }

    public void setYTDFinishRate(BigDecimal YTDFinishRate) {
        this.YTDFinishRate = YTDFinishRate;
    }

    public BigDecimal getMTD() {
        return MTD;
    }

    public void setMTD(BigDecimal MTD) {
        this.MTD = MTD;
    }

    public BigDecimal getWTD() {
        return WTD;
    }

    public void setWTD(BigDecimal WTD) {
        this.WTD = WTD;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getBuyers() {
        return buyers;
    }

    public void setBuyers(Integer buyers) {
        this.buyers = buyers;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public BigDecimal getTranRate() {
        return tranRate;
    }

    public void setTranRate(BigDecimal tranRate) {
        this.tranRate = tranRate;
    }

    public BigDecimal getCustomerAverageAmt() {
        return customerAverageAmt;
    }

    public void setCustomerAverageAmt(BigDecimal customerAverageAmt) {
        this.customerAverageAmt = customerAverageAmt;
    }

    public Integer getUvRegular() {
        return uvRegular;
    }

    public void setUvRegular(Integer uvRegular) {
        this.uvRegular = uvRegular;
    }

    public Integer getOnSale() {
        return onSale;
    }

    public void setOnSale(Integer onSale) {
        this.onSale = onSale;
    }

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public BigDecimal getSalesRate() {
        return salesRate;
    }

    public void setSalesRate(BigDecimal salesRate) {
        this.salesRate = salesRate;
    }

    public BigDecimal getShippingRate() {
        return shippingRate;
    }

    public void setShippingRate(BigDecimal shippingRate) {
        this.shippingRate = shippingRate;
    }

    public BigDecimal getDirectExpressRate() {
        return directExpressRate;
    }

    public void setDirectExpressRate(BigDecimal directExpressRate) {
        this.directExpressRate = directExpressRate;
    }

    public BigDecimal getNotExpress72hRate() {
        return notExpress72hRate;
    }

    public void setNotExpress72hRate(BigDecimal notExpress72hRate) {
        this.notExpress72hRate = notExpress72hRate;
    }

    public BigDecimal getDsrService() {
        return dsrService;
    }

    public void setDsrService(BigDecimal dsrService) {
        this.dsrService = dsrService;
    }

    public BigDecimal getDsrDescribe() {
        return dsrDescribe;
    }

    public void setDsrDescribe(BigDecimal dsrDescribe) {
        this.dsrDescribe = dsrDescribe;
    }

    public BigDecimal getDsrExpress() {
        return dsrExpress;
    }

    public void setDsrExpress(BigDecimal dsrExpress) {
        this.dsrExpress = dsrExpress;
    }

    public BigDecimal getResponseRate48h() {
        return responseRate48h;
    }

    public void setResponseRate48h(BigDecimal responseRate48h) {
        this.responseRate48h = responseRate48h;
    }

    public BigDecimal getReturnDurationRanking() {
        return returnDurationRanking;
    }

    public void setReturnDurationRanking(BigDecimal returnDurationRanking) {
        this.returnDurationRanking = returnDurationRanking;
    }

    public BigDecimal getReturnQualityRanking() {
        return returnQualityRanking;
    }

    public void setReturnQualityRanking(BigDecimal returnQualityRanking) {
        this.returnQualityRanking = returnQualityRanking;
    }

    public BigDecimal getReturnOrders() {
        return returnOrders;
    }

    public void setReturnOrders(BigDecimal returnOrders) {
        this.returnOrders = returnOrders;
    }

    public BigDecimal getReturnDisputesRate() {
        return returnDisputesRate;
    }

    public void setReturnDisputesRate(BigDecimal returnDisputesRate) {
        this.returnDisputesRate = returnDisputesRate;
    }

    public BigDecimal getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(BigDecimal conversionRate) {
        this.conversionRate = conversionRate;
    }

    public Integer getAddFavortite() {
        return addFavortite;
    }

    public void setAddFavortite(Integer addFavortite) {
        this.addFavortite = addFavortite;
    }

    @Override
    public String toString()
    {
        return "["+this.nameCn+" "+this.date+" "+this.amt+" "+this.qty+" "+this.addFavortite+"]";
    }
}
