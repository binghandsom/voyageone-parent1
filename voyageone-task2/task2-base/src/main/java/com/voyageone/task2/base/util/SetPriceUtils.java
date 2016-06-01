package com.voyageone.task2.base.util;

import com.voyageone.common.spring.SpringContext;
import com.voyageone.task2.base.dao.SetPriceDao;
import com.voyageone.task2.base.modelbean.SetPriceBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2015/8/10.
 */
public class SetPriceUtils {

    public static int APPROVED_ORDER_INCLUDE_SHIPPING = 6;

    public static SetPriceDao getSetPriceDao() {
        return SpringContext.getBean(SetPriceDao.class);
    }

    /**
     * 根据order_number，以SKU汇总取得价格
     * @param order_channel_id  渠道ID
     * @param fileFlg 0，3 正常订单
     *                  1，2 退货订单
     *                  4 不含运费订单 正常订单
     *                  5 不含运费订单 Cancel,Return订单
     * @return List<SpaldingPriceBean>
     */
    public static  List<SetPriceBean> setPrice(String order_number,String order_channel_id,String cart_id,int fileFlg)  throws Exception{
        List<SetPriceBean>  priceReportDatas  = new ArrayList<>();
        // 物品总金额（含物品折扣）
        double priceCount = 0;
        // 物品总金额（含订单折扣）
        double priceCountAfter = 0;
        int lastIndex = 0;
        //销售订单基本价格数据取得
        //order_number = "1000009175";
        //order_channel_id = "001";
//        order_number = "1000036633";
//        order_channel_id = "005";
        if (fileFlg == 0 ){
            priceReportDatas = getSetPriceDao().getVirtualPriceData(order_number, order_channel_id, cart_id);
        } else if( fileFlg == 3) {
            priceReportDatas = getSetPriceDao().getPriceData(order_number, order_channel_id, cart_id);
        //退货订单基本价格数据取得
        }else if(fileFlg == 1 || fileFlg == 2) {
            priceReportDatas = getSetPriceDao().getReturnedPriceData(order_number, order_channel_id, cart_id);
        // Approved正常订单推送（不含运费）
        } else if(fileFlg == 4) {
            priceReportDatas = getSetPriceDao().getPriceDataNotIncludeShipping(order_number, order_channel_id, cart_id);
        // Cancel，Return订单推送（不含运费）
        } else if(fileFlg == 5) {
            priceReportDatas = getSetPriceDao().getPriceDataNotIncludeShippingForCancelOrReturn(order_number, order_channel_id, cart_id);
        // Approved正常订单推送（含运费）
        } else if(fileFlg == APPROVED_ORDER_INCLUDE_SHIPPING) {
            priceReportDatas = getSetPriceDao().getPriceDataIncludeShipping(order_number, order_channel_id, cart_id);
        }

        if (priceReportDatas != null && priceReportDatas.size() > 0){
            //分类合计
            //订单折扣和运费合计为0，不需要计算
            if (Double.valueOf(priceReportDatas.get(0).getShipping_price()) == 0){
                return priceReportDatas;
            } else {
                if (fileFlg == APPROVED_ORDER_INCLUDE_SHIPPING && Double.valueOf(priceReportDatas.get(0).getShipping_fee()) == 0) {
                    return priceReportDatas;
                }

                //合计金额算出
                for (SetPriceBean priceReportData : priceReportDatas ){
                    priceCount = Double.valueOf(priceReportData.getPrice()) + priceCount;
                }
                //按金额占合计金额百分比，分摊订单折扣和运费
                int index = 0;

                // 计算前运费总金额
                double priceCountBeforeShipping = 0;
                // 计算后运费总金额
                double priceCountAfterShipping = 0;

                for (SetPriceBean priceReportData : priceReportDatas ){
                    if (!(Double.valueOf(priceReportData.getPrice())== 0)) {
                        // 百分比取得
                        double percentPrice = Double.valueOf(priceReportData.getPrice()) / priceCount;

                        // 折扣计算
                        double percentPriceShip = percentPrice * Double.valueOf(priceReportData.getShipping_price()) + Double.valueOf(priceReportData.getPrice());
                        BigDecimal bigDecimal = new BigDecimal(percentPriceShip).setScale(2, BigDecimal.ROUND_HALF_UP);
                        priceReportData.setPrice(String.valueOf(bigDecimal.toString()));

                        //分摊订单折扣和运费后的总计金额
                        priceCountAfter = bigDecimal.doubleValue() + priceCountAfter;

                        // Approved正常订单推送（含运费）
                        if (fileFlg == APPROVED_ORDER_INCLUDE_SHIPPING) {
                            if (priceCountBeforeShipping == 0) {
                                priceCountBeforeShipping = Double.valueOf(priceReportData.getShipping_fee());
                            }

                            // 运费计算
                            double percentPriceShippingFee = percentPrice *  Double.valueOf(priceReportData.getShipping_fee());
                            BigDecimal bigDecimalShippingFee = new BigDecimal(percentPriceShippingFee).setScale(2, BigDecimal.ROUND_HALF_UP);
                            priceReportData.setShipping_fee(String.valueOf(bigDecimalShippingFee));

                            priceCountAfterShipping = bigDecimalShippingFee.doubleValue() + priceCountAfterShipping;
                        }

                        lastIndex = index;
                    }
                    index =index +1;
                }

                //为防止最后有余数，合计金额 + 订单折扣和运费合计 - 分摊订单折扣和运费后的总计金额 = 剩余金额全部归入最后一个sku合计数据
                double lastPrice = priceCount + Double.valueOf(priceReportDatas.get(0).getShipping_price()) - priceCountAfter + Double.valueOf(priceReportDatas.get(lastIndex).getPrice());
                BigDecimal bigLastPrice = new BigDecimal(lastPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
                priceReportDatas.get(lastIndex).setPrice(String.valueOf(bigLastPrice.doubleValue()));

                if (fileFlg == APPROVED_ORDER_INCLUDE_SHIPPING) {
                    double lastPriceShippingFee = priceCountBeforeShipping -  priceCountAfterShipping + Double.valueOf(priceReportDatas.get(lastIndex).getShipping_fee());
                    BigDecimal bigLastPriceShippingFee = new BigDecimal(lastPriceShippingFee).setScale(2, BigDecimal.ROUND_HALF_UP);
                    priceReportDatas.get(lastIndex).setShipping_fee(String.valueOf(bigLastPriceShippingFee.doubleValue()));
                }
            }
        }
        return priceReportDatas;
    }

    /**
     * 根据order_number，以SKU汇总取得价格
     * @param order_channel_id  渠道ID
     * @param fileFlg 0，3 正常订单
     *                  1，2 退货订单
     *                  4 不含运费订单 正常订单
     *                  5 不含运费订单 Cancel,Return订单
     * @return List<SpaldingPriceBean>
     */
    public static  List<SetPriceBean> setPriceForSP(String order_number,String order_channel_id,String cart_id,int fileFlg)  throws Exception{
        List<SetPriceBean>  priceReportDatas  = new ArrayList<>();
        double priceCount = 0;
        double priceCountAfterShip = 0;
        double priceCountAfterDiscount = 0;
        double unitCount = 0;
        double discount;
        double shipping;
        int lastIndex = 0;
        //销售订单基本价格数据取得
        //order_number = "1000009175";
        //order_channel_id = "001";
//        order_number = "1000036633";
//        order_channel_id = "005";
        //order_number = "1000000994";
        //order_channel_id = "005";
        if (fileFlg == 0 ){
            priceReportDatas = getSetPriceDao().getVirtualPriceData(order_number, order_channel_id, cart_id);
        } else if( fileFlg == 3) {
            priceReportDatas = getSetPriceDao().getPriceData(order_number, order_channel_id, cart_id);
            //退货订单基本价格数据取得
        }else if(fileFlg == 1 || fileFlg == 2) {
            priceReportDatas = getSetPriceDao().getReturnedPriceData(order_number, order_channel_id, cart_id);
        }

        if (priceReportDatas != null && priceReportDatas.size() > 0){

            //订单折扣设定
            discount  = Double.valueOf(priceReportDatas.get(0).getDiscount());
            discount = 0 - discount;
//            if (discount < 0){
//                discount = 0 - discount;
//            }
            //运费设定
            shipping  = Double.valueOf(priceReportDatas.get(0).getShipping_price());
            //分类合计
            //订单折扣和运费为0，不需要计算
            if (discount == 0 && shipping == 0 ){
                for (SetPriceBean priceReportData : priceReportDatas) {
                    //商品价格不为零
                    if (!(Double.valueOf(priceReportData.getPrice()) == 0)) {
                        // 	SalesPrices(按sku合计金额  /  个数)
                        double salesPrice = Double.valueOf(priceReportData.getPrice()) / Double.valueOf(priceReportData.getSum_unit());
                        BigDecimal bigDecimalSalesPrice =  new BigDecimal(salesPrice).setScale(3, BigDecimal.ROUND_HALF_UP);
                        priceReportData.setPrice(String.valueOf(bigDecimalSalesPrice.toString()));
                    }
                }
                return priceReportDatas;
            } else {
                //合计金额算出
                for (SetPriceBean priceReportData : priceReportDatas) {
                    priceCount = Double.valueOf(priceReportData.getPrice()) + priceCount;
                    unitCount = Double.valueOf(priceReportData.getSum_unit()) + unitCount;
                }
                int index = 0;
                //特殊订单：总计价格为0
                if (priceCount == 0) {
                    //合计件数算出
//                    for (SetPriceBean priceReportData : priceReportDatas) {
//                        unitCount = Double.valueOf(priceReportData.getSum_unit()) + unitCount;
//                    }
                    //按件数拆分
                    for (SetPriceBean priceReportData : priceReportDatas) {
                        //件数占总件数的比例
                        double percentUnit = Double.valueOf(priceReportData.getSum_unit()) / unitCount;
                        //运费大于0
                        if (shipping != 0) {
                            //运费按SKU分摊
                            double percentUnitShip = percentUnit * shipping;
                            BigDecimal bigDecimalUnit = new BigDecimal(percentUnitShip).setScale(2, BigDecimal.ROUND_HALF_UP);
                            priceReportData.setShipping_price(String.valueOf(bigDecimalUnit.toString()));
                            //分摊运费后的总计金额
                            priceCountAfterShip = bigDecimalUnit.doubleValue() + priceCountAfterShip;
                            lastIndex = index;
                        }
                        priceReportData.setDiscount("0.00");
                        index = index + 1;
                    }

                    //运费大于0
                    if (shipping != 0) {
                        //为防止最后有余数，运费合计 - 分摊运费后的运费总计金额 = 剩余金额全部归入最后一个sku合计数据
                        double lastPriceShip = shipping - priceCountAfterShip + Double.valueOf(priceReportDatas.get(lastIndex).getShipping_price());
                        BigDecimal bigLastPriceShip = new BigDecimal(lastPriceShip).setScale(2, BigDecimal.ROUND_HALF_UP);
                        priceReportDatas.get(lastIndex).setShipping_price(String.valueOf(bigLastPriceShip.doubleValue()));
                    }

                } else {
                    //按金额占合计金额百分比，分摊订单折扣和运费

                    for (SetPriceBean priceReportData : priceReportDatas) {
                        //商品价格不为零
                        if (!(Double.valueOf(priceReportData.getPrice()) == 0)) {

                            //金额占总金额的比例
                            double percentPrice = Double.valueOf(priceReportData.getPrice()) / priceCount;
                            //运费大于0
                            if (shipping != 0) {
                                //运费按SKU分摊
                                double percentPriceShip = percentPrice * shipping;
                                BigDecimal bigDecimalShip = new BigDecimal(percentPriceShip).setScale(2, BigDecimal.ROUND_HALF_UP);
                                priceReportData.setShipping_price(String.valueOf(bigDecimalShip.toString()));
                                //分摊运费后的总计金额
                                priceCountAfterShip = bigDecimalShip.doubleValue() + priceCountAfterShip;
                            }

                            //折扣大于0
                            if (discount != 0) {
                                //折扣按SKU分摊
                                double percentPriceDiscount = percentPrice * discount;
                                BigDecimal bigDecimalDiscount = new BigDecimal(percentPriceDiscount).setScale(2, BigDecimal.ROUND_HALF_UP);
                                priceReportData.setDiscount(String.valueOf(bigDecimalDiscount.toString()));
                                //分摊订单折扣的总计金额
                                priceCountAfterDiscount = bigDecimalDiscount.doubleValue() + priceCountAfterDiscount;
                            }

                            // 	SalesPrices(按sku合计金额  /  个数)
                            double salesPrice = Double.valueOf(priceReportData.getPrice()) / Double.valueOf(priceReportData.getSum_unit());
                            BigDecimal bigDecimalSalesPrice =  new BigDecimal(salesPrice).setScale(3, BigDecimal.ROUND_HALF_UP);
                            priceReportData.setPrice(String.valueOf(bigDecimalSalesPrice.toString()));

                            lastIndex = index;
                        //商品价格为零
                        } else {
                            //运费设为0
                            priceReportData.setShipping_price("0.00");
                            //折扣设为0
                            priceReportData.setDiscount("0.00");
                        }
                        index = index + 1;
                    }
                    //运费大于0
                    if (shipping != 0) {
                        //为防止最后有余数，运费合计 - 分摊运费后的运费总计金额 = 剩余金额全部归入最后一个sku合计数据
                        double lastPriceShip = shipping - priceCountAfterShip + Double.valueOf(priceReportDatas.get(lastIndex).getShipping_price());
                        BigDecimal bigLastPriceShip = new BigDecimal(lastPriceShip).setScale(2, BigDecimal.ROUND_HALF_UP);
                        priceReportDatas.get(lastIndex).setShipping_price(String.valueOf(bigLastPriceShip.toString()));
                    }
                    //折扣大于0
                    if (discount != 0) {
                        //为防止最后有余数，订单折扣 - 分摊订单折扣后的总计金额 = 剩余金额全部归入最后一个sku合计数据
                        double lastPriceDiscount = discount - priceCountAfterDiscount + Double.valueOf(priceReportDatas.get(lastIndex).getDiscount());
                        BigDecimal bigLastPriceDiscount = new BigDecimal(lastPriceDiscount).setScale(2, BigDecimal.ROUND_HALF_UP);
                        priceReportDatas.get(lastIndex).setDiscount(String.valueOf(bigLastPriceDiscount.toString()));
                    }
                }
            }
        }
        return priceReportDatas;
    }
}
