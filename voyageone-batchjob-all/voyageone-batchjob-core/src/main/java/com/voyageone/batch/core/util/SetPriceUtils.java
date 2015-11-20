package com.voyageone.batch.core.util;

import com.voyageone.batch.core.dao.SetPriceDao;
import com.voyageone.batch.core.modelbean.SetPriceBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2015/8/10.
 */
public class SetPriceUtils {

    private static SetPriceDao setPriceDao;

    @Autowired
    public void setSetPriceDao(SetPriceDao setPriceDao) {
        SetPriceUtils.setPriceDao = setPriceDao;
    }

    /**
     * @description 根据order_number，以SKU汇总取得价格
     * @param order_channel_id  渠道ID
     * @param fileFlg 0，3 正常订单
     *                  1，2 退货订单
     *                  4 不含运费订单 正常订单
     *                  5 不含运费订单 Cancel,Return订单
     * @return List<SpaldingPriceBean>
     */
    public static  List<SetPriceBean> setPrice(String order_number,String order_channel_id,String cart_id,int fileFlg)  throws Exception{
        List<SetPriceBean>  priceReportDatas  = new ArrayList<SetPriceBean>();
        double priceCount = 0;
        double priceCountAfter = 0;
        int lastIndex = 0;
        //销售订单基本价格数据取得
        //order_number = "1000009175";
        //order_channel_id = "001";
//        order_number = "1000036633";
//        order_channel_id = "005";
        if (fileFlg == 0 ){
            priceReportDatas = setPriceDao.getVirtualPriceData(order_number, order_channel_id, cart_id);
        } else if( fileFlg == 3) {
            priceReportDatas = setPriceDao.getPriceData(order_number, order_channel_id, cart_id);
        //退货订单基本价格数据取得
        }else if(fileFlg == 1 || fileFlg == 2) {
            priceReportDatas = setPriceDao.getReturnedPriceData(order_number, order_channel_id, cart_id);
        } else if(fileFlg == 4) {
            priceReportDatas = setPriceDao.getPriceDataNotIncludeShipping(order_number, order_channel_id, cart_id);
        } else if(fileFlg == 5) {
            priceReportDatas = setPriceDao.getPriceDataNotIncludeShippingForCancelOrReturn(order_number, order_channel_id, cart_id);
        }

        if (priceReportDatas != null && priceReportDatas.size() > 0){
            //分类合计
            //订单折扣和运费合计为0，不需要计算
            if (Double.valueOf(priceReportDatas.get(0).getShipping_price()) == 0 ){
                return priceReportDatas;
            } else {
                //合计金额算出
                for (SetPriceBean priceReportData : priceReportDatas ){
                    priceCount = Double.valueOf(priceReportData.getPrice()) + priceCount;
                }
                //按金额占合计金额百分比，分摊订单折扣和运费
                int index = 0;
                for (SetPriceBean priceReportData : priceReportDatas ){
                    if (!(Double.valueOf(priceReportData.getPrice())== 0)) {
                        double percentPrice = Double.valueOf(priceReportData.getPrice()) / priceCount;
                        double percentPriceShip = percentPrice * Double.valueOf(priceReportData.getShipping_price()) + Double.valueOf(priceReportData.getPrice());
                        BigDecimal bigDecimal = new BigDecimal(percentPriceShip).setScale(2, BigDecimal.ROUND_HALF_UP);
                        priceReportData.setPrice(String.valueOf(bigDecimal.toString()));
                        //分摊订单折扣和运费后的总计金额
                        priceCountAfter = bigDecimal.doubleValue() + priceCountAfter;
                        lastIndex = index;
                    }
                    index =index +1;
                }
                //为防止最后有余数，合计金额 + 订单折扣和运费合计 - 分摊订单折扣和运费后的总计金额 = 剩余金额全部归入最后一个sku合计数据
                double lastPrice = priceCount + Double.valueOf(priceReportDatas.get(0).getShipping_price()) - priceCountAfter + Double.valueOf(priceReportDatas.get(lastIndex).getPrice());
                BigDecimal bigLastPrice = new BigDecimal(lastPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
                priceReportDatas.get(lastIndex).setPrice(String.valueOf(bigLastPrice.doubleValue()));
            }
        }
        return priceReportDatas;
    }
}
