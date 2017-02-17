package com.voyageone.common.configs.beans;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dell on 2017/1/16.
 */
public class ShopSalesOfChannel010Bean {
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
    private Integer UV;          //13
    private Integer PV;            //14
    private Integer buys;//15
    private Integer orders;//16
    private BigDecimal tranRate; //17 transfer_rate;
    private BigDecimal customerAverageAmt;//18;
    private Integer addFavirate;//19
    private Integer uvRegular;//20
    //goods data
    private Integer onSale;//21
    private Integer inStock;//22
    private BigDecimal salesRate ;//23动销率
    //express data
    private BigDecimal shipingRate ;//24 发货率
    private BigDecimal directExpressRate;//25直邮订单比
    private BigDecimal notExpress72hRate; //26 72小时未发货率;
    //service data
    private BigDecimal dsrService;//27 服务动态评分;
    private BigDecimal dsrDescribe; //28 描述评分
    private BigDecimal dsrExpress; //29 物流评分
    private BigDecimal  responseRate48h ;//30 48 小时回复率
    private BigDecimal  returnDurationRanking;//31 退款完结时长排名
    private BigDecimal returnQualityRanking ;//32  品质退款率排名
    private BigDecimal return_rate   ;//33 退款率
    private BigDecimal returnDisputesRate;//34  退款纠纷率
    private BigDecimal Conversion_rate    ;//35   询单转化率


    public ShopSalesOfChannel010Bean(
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
            Integer UV,          //13
            Integer PV,            //14
            Integer buys,//15
            Integer orders,//16
            BigDecimal tranRate, //17 transfer_rate,
            BigDecimal customerAverageAmt,//18,
            Integer addFavirate,//19
            Integer uvRegular,//20
            //goods data
            Integer onSale,//21
            Integer inStock,//22
            BigDecimal salesRate ,//23动销率
            //express data
            BigDecimal shipingRate ,//24发货率
            BigDecimal directExpressRate,//25直邮订单比
            BigDecimal notExpress72hRate, //26 72小时未发货率,
            //service data
            BigDecimal dsrService,//27 服务动态评分,
            BigDecimal dsrDescribe, //28 描述评分
            BigDecimal dsrExpress, //29 物流评分
            BigDecimal  responseRate48h ,//30 48 小时回复率
            BigDecimal  returnDurationRanking,//31 退款完结时长排名
            BigDecimal returnQualityRanking ,//32  品质退款率排名
            BigDecimal return_rate   ,//33 退款率
            BigDecimal returnDisputesRate,//34  退款纠纷率
            BigDecimal Conversion_rate    //35   询单转化率
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
        this.  UV=UV;          //13
        this.  PV=PV;            //14
        this.  buys=buys;//15
        this.  orders=orders;//16
        this.  tranRate=tranRate; //17 transfer_rate;
        this.  customerAverageAmt=customerAverageAmt;//18;
        this.  addFavirate=addFavirate;//19
        this.  uvRegular=uvRegular;//20
        //goods data
        this.  onSale=onSale;//21
        this.  inStock=inStock;//22
        this.  salesRate=salesRate ;//23动销率
        //express data
        this.  shipingRate =shipingRate;//24发货率
        this.  directExpressRate=directExpressRate;//25直邮订单比
        this.  notExpress72hRate=notExpress72hRate; //26 72小时未发货率;
        //service data
        this.  dsrService=dsrService;//27 服务动态评分;
        this.  dsrDescribe=dsrDescribe; //28 描述评分
        this.  dsrExpress=dsrExpress; //29 物流评分
        this.   responseRate48h=responseRate48h ;//30 48 小时回复率
        this.   returnDurationRanking=returnDurationRanking;//31 退款完结时长排名
        this.  returnQualityRanking=returnQualityRanking ;//32  品质退款率排名
        this.  return_rate=return_rate   ;//33 退款率
        this.  returnDisputesRate=returnDisputesRate;//34  退款纠纷率
        this.  Conversion_rate=Conversion_rate    ;//35   询单转化率
    }

    /*public ShopSalesOfChannel010Bean(BiReportSalesShop010Model bssm)
    {
        this.date=bssm.getDate();          //2 日期
        this. amt=bssm.getAmt();        //3 销售额
        this.  qty=bssm.getQty();          //4销售量
        this.  UV=bssm.getUv();          //13
        this.  PV=bssm.getPv();            //14
        this.  buys=bssm.getBuyers();//15
        this.  orders=bssm.getOrders();//16
        this.  addFavirate=bssm.getAddFavortite();//19
        this.  uvRegular=bssm.getUvRegular();//20
        //goods data
        this.  onSale=bssm.getOnSale();//21
        this.  inStock=bssm.getInStock();//22
        //express data
//        this.  shipingRate =bssm.shi;//24 发货率
//        this.directExpressRate=bssm.getDirectExpress();//25直邮订单比
//        this.notExpress72hRate=bssm.getNotExpress72h(); //26 72小时未发货率;
        //service data
        this.  dsrService=bssm.getDsrService();//27 服务动态评分;
        this.  dsrDescribe=bssm.getDsrDescribe(); //28 描述评分
        this.  dsrExpress=bssm.getDsrExpress(); //29 物流评分
        this.   responseRate48h=bssm.getResponseRate48h() ;//30 48 小时回复率
        this.   returnDurationRanking=bssm.getReturnDurationRanking();//31 退款完结时长排名
        this.  returnQualityRanking=bssm.getReturnQualityRanking() ;//32  品质退款率排名
        this.  return_rate=bssm.getreturnOreders()   ;//33 退款率
        this.  returnDisputesRate=bssm.getReturnDisputesRate();//34  退款纠纷率
//        this.  Conversion_rate=bssm.    ;//35   询单转化率
    }*/

    public ShopSalesOfChannel010Bean() {}

    public String getnameCn() {
        return nameCn;
    }

    public void setnameCn(String nameCn) {
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

    public BigDecimal getyamtRate() {
        return yamtRate;
    }

    public void setyamtRate(BigDecimal yamtRate) {
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

    public Integer getUV() {
        return UV;
    }

    public void setUV(Integer UV) {
        this.UV = UV;
    }

    public Integer getPV() {
        return PV;
    }

    public void setPV(Integer PV) {
        this.PV = PV;
    }

    public Integer getBuys() {
        return buys;
    }

    public void setBuys(Integer buys) {
        this.buys = buys;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public BigDecimal gettranRate() {
        return tranRate;
    }

    public void settranRate(BigDecimal tranRate) {
        this.tranRate = tranRate;
    }

    public BigDecimal getcustomerAverageAmt() {
        return customerAverageAmt;
    }

    public void setcustomerAverageAmt(BigDecimal customerAverageAmt) {
        this.customerAverageAmt = customerAverageAmt;
    }

    public Integer getaddFavirate() {
        return addFavirate;
    }

    public void setaddFavirate(Integer addFavirate) {
        this.addFavirate = addFavirate;
    }

    public Integer getUvRegular() {
        return uvRegular;
    }

    public void setUvRegular(Integer uvRegular) {
        this.uvRegular = uvRegular;
    }

    public Integer getonSale() {
        return onSale;
    }

    public void setonSale(Integer onSale) {
        this.onSale = onSale;
    }

    public Integer getinStock() {
        return inStock;
    }

    public void setinStock(Integer inStock) {
        this.inStock = inStock;
    }

    public BigDecimal getsalesRate() {
        return salesRate;
    }

    public void setsalesRate(BigDecimal salesRate) {
        this.salesRate = salesRate;
    }

    public BigDecimal getshipingRate() {
        return shipingRate;
    }

    public void setshipingRate(BigDecimal shipingRate) {
        this.shipingRate = shipingRate;
    }

    public BigDecimal getdirectExpressRate() {
        return directExpressRate;
    }

    public void setdirectExpressRate(BigDecimal directExpressRate) {
        this.directExpressRate = directExpressRate;
    }

    public BigDecimal getnotExpress72hRate() {
        return notExpress72hRate;
    }

    public void setnotExpress72hRate(BigDecimal notExpress72hRate) {
        this.notExpress72hRate = notExpress72hRate;
    }

    public BigDecimal getdsrService() {
        return dsrService;
    }

    public void setdsrService(BigDecimal dsrService) {
        this.dsrService = dsrService;
    }

    public BigDecimal getdsrDescribe() {
        return dsrDescribe;
    }

    public void setdsrDescribe(BigDecimal dsrDescribe) {
        this.dsrDescribe = dsrDescribe;
    }

    public BigDecimal getdsrExpress() {
        return dsrExpress;
    }

    public void setdsrExpress(BigDecimal dsrExpress) {
        this.dsrExpress = dsrExpress;
    }

    public BigDecimal getresponseRate48h() {
        return responseRate48h;
    }

    public void setresponseRate48h(BigDecimal responseRate48h) {
        this.responseRate48h = responseRate48h;
    }

    public BigDecimal getreturnDurationRanking() {
        return returnDurationRanking;
    }

    public void setreturnDurationRanking(BigDecimal returnDurationRanking) {
        this.returnDurationRanking = returnDurationRanking;
    }

    public BigDecimal getreturnQualityRanking() {
        return returnQualityRanking;
    }

    public void setreturnQualityRanking(BigDecimal returnQualityRanking) {
        this.returnQualityRanking = returnQualityRanking;
    }

    public BigDecimal getReturn_rate() {
        return return_rate;
    }

    public void setReturn_rate(BigDecimal return_rate) {
        this.return_rate = return_rate;
    }

    public BigDecimal getreturnDisputesRate() {
        return returnDisputesRate;
    }

    public void setreturnDisputesRate(BigDecimal returnDisputesRate) {
        this.returnDisputesRate = returnDisputesRate;
    }

    public BigDecimal getConversion_rate() {
        return Conversion_rate;
    }

    public void setConversion_rate(BigDecimal conversion_rate) {
        Conversion_rate = conversion_rate;
    }
}
