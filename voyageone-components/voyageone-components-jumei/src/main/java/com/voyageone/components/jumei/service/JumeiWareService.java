package com.voyageone.components.jumei.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.JmBase;
import com.voyageone.components.jumei.bean.HtMallStatusUpdateInfo;
import com.voyageone.components.jumei.enums.JmMallStatusType;
import com.voyageone.components.jumei.reponse.HtMallStatusUpdateBatchResponse;
import com.voyageone.components.jumei.request.HtMallStatusUpdateBatchRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚美商城-商品上下架
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
@Service
public class JumeiWareService extends JmBase {

    /**
     * 上架商品
     */
    public HtMallStatusUpdateBatchResponse doWareUpdateListing(ShopBean shopBean, String mallId) {
        return doWareUpdate(shopBean, mallId, JmMallStatusType.ToOnSale.getVal());
    }

    /**
     * 下架商品
     */
    public HtMallStatusUpdateBatchResponse doWareUpdateDelisting(ShopBean shopBean, String mallId) {
        return doWareUpdate(shopBean, mallId, JmMallStatusType.ToInStock.getVal());
    }

    /**
     * 上下架商品
     */
    private HtMallStatusUpdateBatchResponse doWareUpdate(ShopBean shopBean, String mallId, String opeType) {
        List<HtMallStatusUpdateInfo> goodsJson = new ArrayList<>();
        HtMallStatusUpdateInfo mallStatusUpdateInfo = new HtMallStatusUpdateInfo();
        mallStatusUpdateInfo.setJumeiMallId(mallId);
        mallStatusUpdateInfo.setStatus(opeType);
        goodsJson.add(mallStatusUpdateInfo);

        HtMallStatusUpdateBatchRequest request = new HtMallStatusUpdateBatchRequest();
        request.setGoodsJson(goodsJson);
        try {
            String reqResult = reqJmApi(shopBean, request.getUrl(), request.getParameter());
            HtMallStatusUpdateBatchResponse response = new HtMallStatusUpdateBatchResponse();
            response.setBody(reqResult);
            return response;
        } catch (Exception e) {
            logger.error("MallId[" + mallId + "]:调用API时发生异常! ", e);
        }
        return null;
    }
}
