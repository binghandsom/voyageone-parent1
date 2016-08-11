package com.voyageone.components.jumei;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.reponse.*;
import com.voyageone.components.jumei.request.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class JumeiHtDealService extends JmBase {
    public HtDealUpdateResponse update(ShopBean shopBean, HtDealUpdateRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("修改Deal(特卖)信息返回：" + reqResult);
        HtDealUpdateResponse response = new HtDealUpdateResponse();
        response.setBody(reqResult);
        return response;
    }

    public HtDealCopyDealResponse copyDeal(ShopBean shopBean, HtDealCopyDealRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult;
        try {
            reqResult = reqJmApi(shopBean, request.getUrl(), params);
        }catch (BusinessException bex) {
            if (bex.getInfo() != null && bex.getInfo().length>0) {
                reqResult = (String) bex.getInfo()[0];
            } else {
                throw bex;
            }
        }
        logger.info("复制Deal(特卖)信息返回：" + reqResult);
        HtDealCopyDealResponse response = new HtDealCopyDealResponse();
        response.setBody(reqResult);
        return response;
    }

    public HtDealUpdateDealEndTimeResponse updateDealEndTime(ShopBean shopBean, HtDealUpdateDealEndTimeRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("延迟Deal的结束时间信息返回：" + reqResult);//商家可以通过该接口延迟Deal的结束时间。如果Deal下关联Sku在其他Deal存在且时间上有交叉，不允许修改.
        HtDealUpdateDealEndTimeResponse response = new HtDealUpdateDealEndTimeResponse();
        response.setBody(reqResult);
        return response;
    }

    public HtDealUpdateDealPriceBatchResponse updateDealPriceBatch(ShopBean shopBean, HtDealUpdateDealPriceBatchRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("批量更新Deal价格信息返回：" + reqResult);//
        HtDealUpdateDealPriceBatchResponse response = new HtDealUpdateDealPriceBatchResponse();
        response.setBody(reqResult);
        return response;
    }

    public HtDealUpdateDealStockBatchResponse updateDealStockBatch(ShopBean shopBean, HtDealUpdateDealStockBatchRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("批量同步deal库存返回：" + reqResult);//
        HtDealUpdateDealStockBatchResponse response = new HtDealUpdateDealStockBatchResponse();
        response.setBody(reqResult);
        return response;
    }

    public HtDealGetDealByHashIDResponse getDealByHashID(ShopBean shopBean, HtDealGetDealByHashIDRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("获取Deal信息返回：" + reqResult);//
        HtDealGetDealByHashIDResponse response = new HtDealGetDealByHashIDResponse();
        response.setBody(reqResult);
        return response;
    }
}