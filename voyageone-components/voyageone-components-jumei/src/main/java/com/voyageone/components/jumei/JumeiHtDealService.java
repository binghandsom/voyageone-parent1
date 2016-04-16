package com.voyageone.components.jumei;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.Reponse.HtDealCopyDealResponse;
import com.voyageone.components.jumei.Reponse.HtDealUpdateDealEndTimeResponse;
import com.voyageone.components.jumei.Reponse.HtDealUpdateResponse;
import com.voyageone.components.jumei.Request.HtDealCopyDealRequest;
import com.voyageone.components.jumei.Request.HtDealUpdateDealEndTimeRequest;
import com.voyageone.components.jumei.Request.HtDealUpdateRequest;
import org.springframework.stereotype.Service;

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
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
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
}